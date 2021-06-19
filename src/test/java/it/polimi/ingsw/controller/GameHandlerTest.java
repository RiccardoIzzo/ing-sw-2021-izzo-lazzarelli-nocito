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

/**
 * GameHandlerTest tests GameHandler class.
 *
 * @author Andrea Nocito
 */
public class GameHandlerTest {
    private GameHandler gameHandler;
    private Server server;
    private final String lobbyID = "test";

    /**
     * Method setUp initializes Server and GameHandler before each test.
     */
    @Before
    public void setUp() {
        server = new Server(0);
        gameHandler = new GameHandler(server, lobbyID);
        gameHandler.addPropertyListener(new VirtualView(server, lobbyID), gameHandler.getGame());
    }

    /**
     * Method tearDown sets Server and GameHandler null after each test.
     */
    @After
    public void tearDown() {
        server = null;
        gameHandler = null;
    }

    /**
     * Method testSetGameMode tests two different cases:
     * - setGameMode with 1 player -> checks that Game is an instance of SinglePlayerGame
     * - setGameMode with 2 player -> checks that Game is an instance of MultiplayerGame
     */
    @Test
    public void testSetGameMode() {
        GameHandler gameHandler = new GameHandler(server, lobbyID);
        gameHandler.setGameMode(1);
        assertTrue(gameHandler.getGame() instanceof SinglePlayerGame);

        gameHandler.setGameMode(2);
        assertTrue(gameHandler.getGame() instanceof MultiplayerGame);
    }

    /**
     * Method testProcess tests every message (ClientMessage) received by the Server and processed by the GameHandler.
     */
    @Test
    public void testProcess() {
        gameHandler.setGameMode(3);
        Game game = gameHandler.getGame();
        game.addPlayer("Andrea");
        game.addPlayer("Gabriele");
        game.addPlayer("Riccardo");
        game.generateGrid();
        game.generateLeaders();
        game.getMarket().generateTray();

        String nickname = "Andrea";
        Player player = game.getPlayerByName(nickname);

        // SelectLeaderCard message
        int firstID = 0, secondID = 0;
        Optional<LeaderCard> firstLeaderCard = player.getLeaders().stream().findAny();
        if (firstLeaderCard.isPresent()) firstID = firstLeaderCard.get().getCardID();

        Optional<LeaderCard> secondLeaderCard = player.getLeaders().stream().filter(x -> x.getCardID() != firstLeaderCard.get().getCardID()).findAny();
        if (secondLeaderCard.isPresent()) secondID = secondLeaderCard.get().getCardID();

        assertEquals(4, player.getLeaders().size());
        gameHandler.process(nickname, new SelectLeaderCards(firstID, secondID));
        assertEquals(2, player.getLeaders().size());

        for(LeaderCard leaderCard : player.getLeaders()) {
            assertNotEquals(leaderCard.getCardID(), firstID);
            assertNotEquals(leaderCard.getCardID(), secondID);
        }

        // TakeResources message
        Market market = game.getMarket();
        player.getDashboard().getWarehouse().flushShelves();
        Integer numberOfResourcesTaken = player.getDashboard().getWarehouse().getResourcesFromWarehouse().getAmount();
        assertEquals(0, (int) numberOfResourcesTaken);

        market.resourceConverter(market.getMarketTray().get(0));
        market.resourceConverter(market.getMarketTray().get(1));
        market.resourceConverter(market.getMarketTray().get(2));
        market.resourceConverter(market.getMarketTray().get(3));
        ResourceMap marketResources = new ResourceMap();
        marketResources.addResources(market.resourceOutput());
        market.reset();
        gameHandler.process(nickname, new TakeResources(1, 1));
        ResourceMap temporaryShelf = player.getDashboard().getWarehouse().getResourcesFromShelf(PlayerConstants.TEMPORARY_SHELF);
        assertEquals(marketResources.getResources(), temporaryShelf.getResources());

        // SendBonusResources message
        player.getDashboard().getWarehouse().flushShelves();
        assertEquals(0, (int) player.getDashboard().getWarehouse().getResourcesFromWarehouse().getAmount());
        ResourceMap bonusResources = new ResourceMap();
        bonusResources.modifyResource(Resource.COIN, 1);
        gameHandler.process(nickname, new SendBonusResources(bonusResources));
        assertEquals(1, (int) player.getDashboard().getWarehouse().getResourcesFromWarehouse().getAmount());
        assertTrue(player.getDashboard().getWarehouse().getResourcesFromWarehouse().asList().contains(Resource.COIN));


        // BuyCard message
        assertEquals(0, player.getDevelopments().size());
        DevelopmentCard card = game.getGrid()[1][1].getTopCard();
        gameHandler.process(nickname, new BuyCard(1, 1, 1));
        assertEquals(1, player.getDevelopments().size());
        assertTrue(player.getDevelopments().contains(card));

        // ActivateLeaderCard message
        LeaderCard leaderCardOne = player.getLeaders().iterator().next();
        assertFalse(leaderCardOne.isActive());
        gameHandler.process(nickname, new ActivateLeaderCard(leaderCardOne.getCardID()));
        assertTrue(leaderCardOne.isActive());

        // DiscardLeaderCard message
        int size = player.getLeaders().size();
        LeaderCard leaderCardTwo = null;
        for(LeaderCard leader : player.getLeaders()){
            if(!card.equals(leaderCardOne)) leaderCardTwo = leader;
        }

        if(leaderCardTwo != null){
            assertFalse(leaderCardTwo.isActive());
            gameHandler.process(nickname, new DiscardLeaderCard(leaderCardTwo.getCardID()));
            assertFalse(player.getLeaders().contains(leaderCardTwo));
            assertEquals(size - 1, player.getLeaders().size());
        }

        // ActivateProduction message
        player.getDashboard().getWarehouse().flushShelves();
        ResourceMap resourcesAtStart = player.getDashboard().getStrongbox();
        DevelopmentCard developmentCard = player.getDevelopments().iterator().next();
        ResourceMap resourcesRequired = developmentCard.getProduction().getInputResource();
        ResourceMap resourcesToGet = developmentCard.getProduction().getOutputResource();
        player.getDashboard().getStrongbox().addResources(resourcesRequired);

        gameHandler.process(nickname, new ActivateProduction(Collections.singletonList(developmentCard.getCardID())));

        assertEquals(player.getDashboard().getStrongbox().getResources(), resourcesAtStart.addResources(resourcesToGet).getResources());

        // BasicProduction message
        ResourceMap input = new ResourceMap();
        ResourceMap output = new ResourceMap();
        input.modifyResource(Resource.COIN, 1);
        input.modifyResource(Resource.SHIELD, 1);
        output.modifyResource(Resource.SERVANT, 1);
        player.getDashboard().getStrongbox().flush();
        player.getDashboard().getStrongbox().addResources(input);

        assertFalse(player.getDashboard().getStrongbox().asList().contains(Resource.SERVANT));
        gameHandler.process(nickname, new BasicProduction(input, output));
        assertTrue(player.getDashboard().getStrongbox().asList().contains(Resource.SERVANT));

        // SetWarehouse message
        /*
        ArrayList<Resource> resources = new ArrayList<>();
        resources.add(Resource.STONE);
        resources.add(Resource.SERVANT);
        player.getDashboard().getWarehouse().flushShelves();
        assertEquals(0, (int) player.getDashboard().getWarehouse().getResourcesFromWarehouse().getAmount());
        gameHandler.process(nickname, new SetWarehouse(resources));
        assertEquals(resources.size(), (int) player.getDashboard().getWarehouse().getResourcesFromWarehouse().getAmount());
         */

        // SetFinalTurn message
        gameHandler.process(nickname, new SetFinalTurn());
        assertTrue(game.isFinalTurn());
    }
}