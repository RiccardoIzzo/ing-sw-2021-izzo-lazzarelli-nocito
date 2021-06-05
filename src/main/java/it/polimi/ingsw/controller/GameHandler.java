package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.events.servermessages.*;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.*;

import static it.polimi.ingsw.constants.GameConstants.*;


/**
 * GameHandler class represents a game and manages the incoming messages from the client.
 *
 * @author Andrea Nocito, Riccardo Izzo, Gabriele Lazzarelli
 */
public class GameHandler {
    private Game game;
    private final Server server;
    private final String lobbyID;
    private final VirtualView virtualView;

    /**
     * Constructor GameHandler creates a new GameHandler instance.
     * @param server reference to the server that will manage the communications.
     */
    public GameHandler(Server server, String lobbyID) {
        this.server = server;
        this.lobbyID = lobbyID;
        this.virtualView = new VirtualView(server,lobbyID);
    }

    /**
     * Method setGameMode initializes the game as an instance of MultiplayerGame or SinglePlayerGame.
     * @param numberOfPlayers the number of players.
     */
    public void setGameMode(int numberOfPlayers) {
        game = numberOfPlayers > 1 ? new MultiplayerGame() : new SinglePlayerGame();
    }
    public Game getGame() {
        return game;
    }

    /**
     * Method start sets up the game and starts the first turn.
     */
    public void start() {
        /*
        Add all registered players to the game.
         */
        ArrayList<String> players = server.getPlayersNameByLobby(lobbyID);
        for (String player : players) {
            game.addPlayer(player);
            game.getPlayerByName(player).addPropertyListener(virtualView);
        }
        game.addPropertyListener(virtualView);
        game.getMarket().addPropertyListener(virtualView);
        /*
        The game is ready, send a GameStarted message to every player.
        Give each player four LeaderCard.
         */
        server.sendEveryone(new GameStarted(players), lobbyID);
        game.generateGrid();
        game.getMarket().generateTray();
        game.generateLeaders();

        if (game instanceof MultiplayerGame) {
            ((MultiplayerGame) game).setFirstPlayer();
            /*
            Manages bonus resources.
             */
            int index = 0;
            for (String player : players) {
                game.getPlayerByName(player).getDashboard().incrementFaith(BONUS_FAITH_POINTS[index++]);
                int amount = game.getPlayers().indexOf(game.getPlayerByName(player));
                if (amount != 0){
                    virtualView.sendToPlayer(player, new GetBonusResources(BONUS_RESOURCES[amount]));
                }
            }
        }

        String firstPlayer = game.getPlayers().get(0).getNickname();
        if(server.isConnected(firstPlayer)) server.getConnectionByPlayerName(firstPlayer).sendToClient(new StartTurn(firstPlayer));
    }

    /**
     * Method process manages the incoming message from the client and apply the changes to the model.
     * @param nickname the current player
     * @param message ClientMessage received from the client
     */
    public void process(String nickname, ClientMessage message){
        Player player = game.getPlayerByName(nickname);

        if(message instanceof SelectLeaderCards) {
            player.removeLeaders(((SelectLeaderCards) message).getLeadersToDiscard());
        }

        else if(message instanceof SendBonusResources){
            int indexResource = 0;
            for(Resource resource: ((SendBonusResources) message).getBonusResource().asList()) {
                player.getDashboard().getWarehouse().getShelves().set(indexResource, resource);
                indexResource++;
            }
        }

        else if(message instanceof TakeResources) {
            player.takeResourcesFromMarket(((TakeResources) message).getIndex(), ((TakeResources) message).getType());
        }

        else if(message instanceof BuyCard) {
            player.buyCard(((BuyCard) message).getRow(), ((BuyCard) message).getColumn(), ((BuyCard) message).getIndex());
        }

        else if(message instanceof ActivateLeaderCard) {
            for(LeaderCard card : player.getLeaders()) {
                if (card.getCardID() == ((ActivateLeaderCard) message).getCardID()) {
                    player.activateLeaderCard(card);
                }
            }
        }

        else if(message instanceof DiscardLeaderCard){
            player.discardLeaderCard(((DiscardLeaderCard) message).getId());
        }

        else if(message instanceof ActivateProduction) {
            for (Card card: player.getAvailableProduction()) {
                if(((ActivateProduction) message).getCardsID().contains(card.getCardID())){
                    player.activateProduction(card);
                }
            }
        }

        else if(message instanceof CheckRequirement){
            Card card = JsonCardsCreator.generateCard(((CheckRequirement) message).getId());
            boolean result = card.getRequirement().checkRequirement(player);
            server.getConnectionByPlayerName(nickname).sendToClient(new CheckRequirementResult(result, card.getCardID()));
        }

        else if(message instanceof EndTurn) {
            if(game instanceof MultiplayerGame){
                ((MultiplayerGame) game).nextPlayer();
                String currPlayer = ((MultiplayerGame) game).getCurrPlayer().getNickname();
                if(game.isFinalTurn() && currPlayer.equals(((MultiplayerGame) game).getFirstPlayer().getNickname())){
                    //virtualView.sendToEveryone(new GameStats());
                }
                virtualView.sendToPlayer(currPlayer, new StartTurn(currPlayer));
            }
            else if(game instanceof SinglePlayerGame){
                ((SinglePlayerGame) game).drawToken();
            }

        }

        else if(message instanceof SetWarehouse) {
            player.getDashboard().getWarehouse().setShelves(((SetWarehouse) message).getWarehouse());
            int faith = player.getDashboard().getWarehouse().removeResourcesFromShelf(5).size();
            for (Player playerToAdd: game.getPlayers()) {
                if (playerToAdd != player) {
                    if(playerToAdd.getDashboard().incrementFaith(faith)) game.vaticanReport();
                }
            }

        }

        else if(message instanceof SetFinalTurn){
            game.setFinalTurn(true);
        }
    }
}