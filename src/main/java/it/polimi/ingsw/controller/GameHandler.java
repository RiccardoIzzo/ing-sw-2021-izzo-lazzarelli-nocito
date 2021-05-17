package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.events.servermessages.*;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.*;


/**
 * GameHandler class represents a game and manages the incoming messages from the client.
 *
 * @author Andrea Nocito, Riccardo Izzo
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
    public GameHandler(Server server) {
        this.server = server;
        lobbyID = server.getLobbyID(this);
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
        String firstPlayer;
        for (String player : players) {
            game.addPlayer(player);
            game.getPlayerByName(player).addPropertyListener(virtualView);
        }
        game.generateGrid();
        game.getMarket().generateTray();
        /*
        The game is ready, send a GameStarted message to every player.
        Give each player four LeaderCard.
         */
        server.sendEveryone(new GameStarted(), lobbyID);
        game.generateLeaders();

        if (game instanceof MultiplayerGame) {
            ((MultiplayerGame) game).setFirstPlayer();
            firstPlayer = ((MultiplayerGame) game).getFirstPlayer().getNickname();
            /*
            The first player to get bonus resources is the 2nd.
             */
            int playerOrder = 2;
            int numPlayers = game.getNumPlayers();
            int currIndex = ((MultiplayerGame) game).getPlayerIndex();
            Player player;

            /*
            Manages bonus resources.
             */
            while(playerOrder <= numPlayers){
                if(currIndex + 1 == numPlayers) currIndex = 0;
                else currIndex++;
                player = game.getPlayers().get(currIndex);
                /*
                3rd and 4th player gets bonus faith.
                 */
                if(playerOrder == 3 || playerOrder == 4) {
                    player.getDashboard().incrementFaith(1);
                }
                /*
                2nd and 3rd player gets one bonus resource.
                 */
                if(playerOrder == 2 || playerOrder == 3) server.getConnectionByPlayerName(player.getNickname()).sendToClient(new BonusResources(1));
                /*
                4th player gets two bonus resources.
                 */
                else if(playerOrder == 4) server.getConnectionByPlayerName(player.getNickname()).sendToClient(new BonusResources(2));
                playerOrder++;
            }
        } else {
            firstPlayer = players.get(0);
        }
        //to implement, bonus resource/faith based on the player position at the beginning of the game
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
            LeaderCard[] cards = player.getLeaders().toArray(LeaderCard[]::new);
            player.selectLeaderCard(cards[((SelectLeaderCards) message).getFirstCardIndex()], cards[((SelectLeaderCards) message).getSecondCardIndex()]);
        }

        else if(message instanceof TakeResources) {
            player.getResources(((TakeResources) message).getIndex(), ((TakeResources) message).getType(), game.getMarket());
        }

        else if(message instanceof BuyCard) {
            player.buyCard(((BuyCard) message).getRow(), ((BuyCard) message).getColumn());
        }

        else if(message instanceof ActivateLeaderCard) {
            for(LeaderCard card : player.getLeaders()) {
                if (card.getCardID() == ((ActivateLeaderCard) message).getCardID()) {
                    player.activateLeaderCard(card);
                }
            }
        }

        else if(message instanceof ActivateProduction) {
            for(DevelopmentCard card : player.getDevelopments().toArray(DevelopmentCard[]::new)){
                if(((ActivateProduction) message).getCardsID().contains(card.getCardID())){
                    player.activateProduction(card);
                }
            }
        }

        else if(message instanceof EndTurn) {
            if(game instanceof MultiplayerGame){
                ((MultiplayerGame) game).nextPlayer();
            }
            else if(game instanceof SinglePlayerGame){
                //to fix: drawToken method signature
                ((SinglePlayerGame) game).drawToken((SinglePlayerGame) game);
            }
        }
    }
}