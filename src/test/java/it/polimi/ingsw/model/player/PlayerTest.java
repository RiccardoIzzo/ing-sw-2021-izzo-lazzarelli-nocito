package it.polimi.ingsw.model.player;

import it.polimi.ingsw.constants.PlayerConstants;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import static org.junit.Assert.*;

/**
 * PlayerTest tests Player class.
 *
 * @author Riccardo Izzo, Gabriele Lazzarelli
 */
public class PlayerTest {
    private Game game;
    private Player player;

    /**
     * Method initialization create an instance of Game, adds three players and generates all the leader cards.
     */
    @Before
    public void initialization() {
        game = new MultiplayerGame();
        game.addPlayer("Riccardo");
        player = game.getPlayerByName("Riccardo");
        game.generateGrid();
        game.generateLeaders();
        game.getMarket().generateTray();
    }

    @After
    public void tearDown() {
        game = null;
        player = null;
    }

    /**
     * Method activateLeaderCardTest checks the correct activation of a leader card.
     */
    @Test
    public void activateLeaderCardTest(){
        Optional<LeaderCard> card = player.getLeaders().stream().findAny();
        if(card.isPresent()){
            LeaderCard leaderCard = card.get();
            assertFalse(leaderCard.isActive());
            player.activateLeaderCard(leaderCard);
            assertTrue(leaderCard.isActive());
        }
    }

    /**
     * Method discardLeaderTest checks that the leader card is correctly discarded and that the player's position on the faithtrack is increased.
     */
    @Test
    public void discardLeaderTest(){
        Optional<LeaderCard> card = player.getLeaders().stream().findAny();
        if(card.isPresent()){
            assertEquals(4, player.getLeaders().size());
            assertEquals(0, player.getDashboard().getFaithTrack().getPlayerPosition());
            player.discardLeaderCard(card.get().getCardID());
            assertEquals(3, player.getLeaders().size());
            assertEquals(1, player.getDashboard().getFaithTrack().getPlayerPosition());
            card = player.getLeaders().stream().findAny();
            if(card.isPresent()){
                player.discardLeaderCard(card.get().getCardID());
                assertEquals(2, player.getLeaders().size());
                assertEquals(2, player.getDashboard().getFaithTrack().getPlayerPosition());
            }
        }
    }

    /**
     * Method buyCardTestA test if availableProduction, levelOfCard and numberOfCard are properly updated.
     * The test replicates the case in which a player buys:
     * <ul>
     * <li>lv 1 - purple card</li>
     * <li>lv 1 - green card</li>
     * <li>lv 2 - purple card</li>
     * <li>lv 1 - yellow card</li>
     * <li>lv 2 - yellow card</li>
     * <li>lv 3 - yellow card</li>
     * </ul>
     *
     * Note: buyCard() gets a card from the grid in game, the fact that is available and can be bought is a pre-condition to the method.
     */
    @Test
    public void buyCardTestA(){
        player.buyCard(0,1,1); //purple - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.PURPLE), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.PURPLE), 1);

        player.buyCard(0,0,2); //green  - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 1);

        player.buyCard(1,1,1); //purple - lv 2
        assertEquals(player.getLevelOfCard().getCard(CardColor.PURPLE), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.PURPLE), 2);


        player.buyCard(0,3,3); //yellow - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.YELLOW), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.YELLOW), 1);
        player.buyCard(1,3,3); //yellow - lv 2
        assertEquals(player.getLevelOfCard().getCard(CardColor.YELLOW), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.YELLOW), 2);
        player.buyCard(2,3,3); //yellow - lv 3
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 1);
        assertEquals(player.getLevelOfCard().getCard(CardColor.PURPLE), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.PURPLE), 2);
        assertEquals(player.getLevelOfCard().getCard(CardColor.YELLOW), 3);
        assertEquals(player.getNumberOfCard().getCard(CardColor.YELLOW), 3);

        assertFalse(player.getNumberOfCard().getCard(CardColor.BLUE) != 0);
    }

    /**
     * Method buyCardTestB test if availableProduction, levelOfCard and numberOfCard are properly updated.
     * The test replicates the case in which a player buys:
     * <ul>
     * <li>lv 1 - blue card</li>
     * <li>lv 1 - green card</li>
     * <li>lv 2 - blue card</li>
     * <li>lv 1 - green card</li>
     * <li>lv 2 - green card</li>
     * <li>lv 3 - green card</li>
     * </ul>
     *
     * This case differs from the antecedent since there are two different DevelopmentCard of the same level and type.<br>
     * Note: buyCard() gets a card from the grid in game, the fact that is available and can be bought is a pre-condition to the method.
     */
    @Test
    public void buyCardTestB(){
        player.buyCard(0,2,1); //blue - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.BLUE), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.BLUE), 1);

        player.buyCard(0,0,2); //green  - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 1);

        player.buyCard(1,2,1); //blue - lv 2
        assertEquals(player.getLevelOfCard().getCard(CardColor.BLUE), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.BLUE), 2);


        player.buyCard(0,0,3); //green - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 2);
        player.buyCard(1,0,3); //green - lv 2
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 3);
        player.buyCard(2,0,3); //green - lv 3
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 3);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 4);
        assertEquals(player.getLevelOfCard().getCard(CardColor.BLUE), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.BLUE), 2);

        assertFalse(player.getNumberOfCard().getCard(CardColor.YELLOW) != 0);
    }

    /**
     * Method getAvailableProductionTest tests the correctness of the production's list.
     */
    @Test
    public void getAvailableProductionTest(){
        assertEquals(player.getAvailableProduction().size(), 0);
        player.buyCard(1, 1, 1);
        assertEquals(player.getAvailableProduction().size(), 1);
        player.buyCard(1, 3, 2);
        player.buyCard(1, 3, 3);
        assertEquals(player.getAvailableProduction().size(), 3);
    }

    /**
     * Method takeResourcesFromMarketTest tests the method takeResourcesFromMarket comparing the expected resources with those actually taken.
     */
    @Test
    public void takeResourcesFromMarketTest(){
        Market market = game.getMarket();
        ResourceMap marketResources = new ResourceMap();
        market.resourceConverter(market.getMarketTray().get(0));
        market.resourceConverter(market.getMarketTray().get(1));
        market.resourceConverter(market.getMarketTray().get(2));
        market.resourceConverter(market.getMarketTray().get(3));
        marketResources.addResources(market.resourceOutput());
        market.reset();
        player.takeResourcesFromMarket(1, 1, null);
        assertEquals(player.getDashboard().getWarehouse().getResourcesFromShelf(PlayerConstants.TEMPORARY_SHELF).asList(), marketResources.asList());
    }

    /**
     * Method activateProductionTestA tests the activation of a development card production.
     */
    @Test
    public void activateProductionTestA(){
        DevelopmentCard card = (DevelopmentCard) JsonCardsCreator.generateCard(102);
        player.getDashboard().getStrongbox().modifyResource(Resource.COIN, 1);
        player.activateProduction(card);
        assertEquals((int) player.getDashboard().getStrongbox().getResource(Resource.SERVANT), 1);
    }

    /**
     * Method activateProductionTestB tests the activation of a leader card production.
     */
    @Test
    public void activateProductionTestB(){
        LeaderCard card = (LeaderCard) JsonCardsCreator.generateCard(209);
        player.getDashboard().getStrongbox().modifyResource(Resource.SHIELD, 1);
        player.activateProduction(card);
        assertEquals(player.getDashboard().getFaithTrack().getPlayerPosition(), 1);
    }

    /**
     * Method activateProductionTestB tests the activation of the basic production.
     */
    @Test
    public void activateProductionTestC(){
        ResourceMap input = new ResourceMap();
        ResourceMap output = new ResourceMap();
        input.modifyResource(Resource.COIN, 1);
        input.modifyResource(Resource.SHIELD, 1);
        output.modifyResource(Resource.STONE, 1);
        player.getDashboard().getStrongbox().addResources(input);
        assertEquals((int) player.getDashboard().getStrongbox().getResource(Resource.STONE), 0);
        player.activateProduction(input, output);
        assertEquals((int) player.getDashboard().getStrongbox().getResource(Resource.STONE), 1);
    }

    /**
     * Method removeLeadersTest tests the removal of a leader card from set of leader cards.
     */
    @Test
    public void removeLeadersTest(){
        LeaderCard card = player.getLeaders().iterator().next();
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(card.getCardID());
        assertEquals(player.getLeaders().size(), 4);
        player.removeLeaders(ids);
        assertEquals(player.getLeaders().size(), 3);
        assertFalse(player.getLeaders().contains(card));
    }

    /**
     * Method getVictoryPointsTest tests the correctness of the total amount of VPs after adding resources and having increased the position twice.
     */
    @Test
    public void getVictoryPointsTest(){
        player.getDashboard().incrementFaith(3);
        assertEquals(1, player.getVictoryPoints());
        player.getDashboard().getStrongbox().modifyResource(Resource.COIN, 5);
        assertEquals(2, player.getVictoryPoints());
        player.getDashboard().incrementFaith(3);
        assertEquals(3, player.getVictoryPoints());
    }
}