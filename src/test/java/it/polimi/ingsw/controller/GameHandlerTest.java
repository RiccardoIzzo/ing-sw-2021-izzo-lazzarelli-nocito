package it.polimi.ingsw.controller;

import it.polimi.ingsw.constants.PlayerConstants;
import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.model.*;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.VirtualView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;


public class GameHandlerTest {
    private GameHandler gameHandler;
    private Game game;
    private Server serverTest;
    private String lobbyID = "test";

    private VirtualView virtualView;
    @Before
    public void setUp() {
        serverTest = new Server(0);
        virtualView = new VirtualView(serverTest, lobbyID);

        gameHandler = new GameHandler(serverTest, lobbyID);
        gameHandler.setGameMode(3);

        game = gameHandler.getGame();

//        game.addPropertyListener(virtualView);

        game.addPlayer("Andrea");
        game.addPlayer("Gabriele");
        game.addPlayer("Riccardo");
        game.generateGrid();
        game.generateLeaders();
        game.getMarket().generateTray();
    }

    @After
    public void tearDown() {
        gameHandler = null;
        serverTest = null;
        game = null;
    }

    /**
     * Method testSetGameMode sets the number of players to 1 and checks that game is set as an instance of SinglePlayerGame,
     * then sets the numbers of players to 2 and checks  that game is set as an instance of MultiplayerGame
     */
    @Test
    public void testSetGameMode() {
        System.out.println("getGame");
        Game game = gameHandler.getGame();
        assertFalse(game instanceof SinglePlayerGame);
        assertTrue(game instanceof MultiplayerGame);

        System.out.println("singlePlayerGame");
        gameHandler.setGameMode(1);
        game = gameHandler.getGame();
        assertTrue(game instanceof SinglePlayerGame);


    }


    /**
     * Method testProcess tests the correct interpretation of the client messages
     * SelectLeaderCard checks that the game starts with 4 cards, and that only 2 are left after the user selection.
     * TakeResources checks that the market is functioning and that the resources taken are put into the temporary shelf inside the warehouse
     * BuyCard checks that there are no cards in player, and that there is the specified one after the buyCard message is sent.
     *
     * EndTurn checks that the first players is the actual current player, then sends a message of EndTurn and checks that the curr player is the next player
     *
     * */
    @Test
    public void testProcess() {

        String nickname = "Andrea";
        Player player = game.getPlayerByName(nickname);
//        player.addPropertyListener(virtualView);

        // message: SelectLeaderCard
        assertEquals(4, player.getLeaders().size());

        int firstID = 0, secondID = 0;
        Optional<LeaderCard> firstLeaderCard = player.getLeaders().stream().findAny();
        if (firstLeaderCard.isPresent()) {
            firstID = firstLeaderCard.get().getCardID();
            Optional<LeaderCard> secondLeaderCard = player.getLeaders().stream().filter(x -> x.getCardID() != firstLeaderCard.get().getCardID()).findAny();
            if (secondLeaderCard.isPresent())
                secondID = secondLeaderCard.get().getCardID();
        }

        gameHandler.process(nickname, new DiscardLeaderCard(firstID, secondID));

        for(LeaderCard leaderCard : player.getLeaders()) {
            assertNotEquals(leaderCard.getCardID(), firstID);
            assertNotEquals(leaderCard.getCardID(), secondID);
        }
        assertEquals(2, player.getLeaders().size());

        // message: TakeResources
        player.getDashboard().getWarehouse().flushShelves();
        Integer numberOfResourcesTaken = player.getDashboard().getWarehouse().getResourcesFromWarehouse().size();
//                getResourcesSize().getMapSize();
        assertEquals(0, (int) numberOfResourcesTaken);
        gameHandler.process(nickname, new TakeResources(1, 1));
        ResourceMap marketResources = game.getMarket().resourceOutput();
        ResourceMap temporaryShelf = player.getDashboard().getWarehouse().getResourcesFromShelf(PlayerConstants.TEMPORARY_SHELF);
        assertEquals(marketResources.getResources(), temporaryShelf.getResources());


        // BuyCard
        assertEquals(0, player.getDevelopments().size());
        DevelopmentCard card = game.getGrid()[1][1].getTopCard();
        player.buyCard(1, 1);
        assertEquals(1, player.getDevelopments().size());
        assertTrue(player.getDevelopments().contains(card));

        // ActivateLeaderCard
        LeaderCard leaderCard = (player.getLeaders().iterator().next());
        assertFalse(leaderCard.isActive());
        gameHandler.process(nickname, new ActivateLeaderCard(leaderCard.getCardID()));
        assertTrue(leaderCard.isActive());

        // ActivateProduction
        player.getDashboard().getWarehouse().flushShelves();
        ResourceMap resourcesAtStart = player.getDashboard().getWarehouse().getResourcesFromWarehouse();
        resourcesAtStart.addResources(player.getDashboard().getStrongbox());

        DevelopmentCard developmentCard = player.getDevelopments().iterator().next();
        ResourceMap resourcesRequired = developmentCard.getProduction().getInputResource();
        player.getDashboard().getStrongbox().addResources(resourcesRequired);

        ResourceMap resourcesToGet = developmentCard.getProduction().getOutputResource();
        gameHandler.process(nickname, new ActivateProduction(Collections.singletonList(developmentCard.getCardID())));

        resourcesAtStart.addResources(resourcesToGet);
        assertEquals(player.getTotalResources().getResources(), resourcesAtStart.getResources());


        // EndTurn - MultiPlayer
        if (game instanceof MultiplayerGame) {
            ((MultiplayerGame) game).setFirstPlayer();
            player = ((MultiplayerGame) game).getCurrPlayer();
            Player firstPlayer = ((MultiplayerGame) game).getFirstPlayer();
            assertEquals(player, firstPlayer);

            gameHandler.process(nickname, new EndTurn());

            assertNotEquals(firstPlayer,  ((MultiplayerGame) game).getCurrPlayer());
        }

        // EndTurn - SinglePlayer
        gameHandler.setGameMode(1);
        game = gameHandler.getGame();
        game.addPlayer("Andrea");
        game.generateGrid();
        game.generateLeaders();
        game.getMarket().generateTray();

        game.addPlayer(nickname);
        int numTokens = ((SinglePlayerGame) game).getTokenStack().numTokens();
        gameHandler.process(nickname, new EndTurn());
        assertEquals(numTokens-1, ((SinglePlayerGame) game).getTokenStack().numTokens() );



    }

}