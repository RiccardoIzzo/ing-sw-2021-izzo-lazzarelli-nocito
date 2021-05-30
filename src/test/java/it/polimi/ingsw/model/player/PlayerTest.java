package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MultiplayerGame;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.LeaderCard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import static org.junit.Assert.*;

/**
 * PlayerTest tests Player class.
 *
 * @author Riccardo Izzo
 */
public class PlayerTest {
    Game game;
    Player player;

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
            assertEquals(0, player.getDashboard().getFaithTrack().getPlayerPos());
            player.discardLeaderCard(card.get().getCardID());
            assertEquals(3, player.getLeaders().size());
            assertEquals(0, player.getDashboard().getFaithTrack().getPlayerPos());
            card = player.getLeaders().stream().findAny();
            if(card.isPresent()){
                player.discardLeaderCard(card.get().getCardID());
                assertEquals(2, player.getLeaders().size());
                assertEquals(0, player.getDashboard().getFaithTrack().getPlayerPos());
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
        player.buyCard(0,1,0); //purple - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.PURPLE), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.PURPLE), 1);

        player.buyCard(0,0,0); //green  - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 1);

        player.buyCard(1,1,0); //purple - lv 2
        assertEquals(player.getLevelOfCard().getCard(CardColor.PURPLE), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.PURPLE), 2);


        player.buyCard(0,3,0); //yellow - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.YELLOW), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.YELLOW), 1);
        player.buyCard(1,3,0); //yellow - lv 2
        assertEquals(player.getLevelOfCard().getCard(CardColor.YELLOW), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.YELLOW), 2);
        player.buyCard(2,3,0); //yellow - lv 3
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
        player.buyCard(0,2,0); //blue - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.BLUE), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.BLUE), 1);

        player.buyCard(0,0,0); //green  - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 1);

        player.buyCard(1,2,0); //blue - lv 2
        assertEquals(player.getLevelOfCard().getCard(CardColor.BLUE), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.BLUE), 2);


        player.buyCard(0,0,0); //green - lv 1
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 1);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 2);
        player.buyCard(1,0,0); //green - lv 2
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 3);
        player.buyCard(2,0,0); //green - lv 3
        assertEquals(player.getLevelOfCard().getCard(CardColor.GREEN), 3);
        assertEquals(player.getNumberOfCard().getCard(CardColor.GREEN), 4);
        assertEquals(player.getLevelOfCard().getCard(CardColor.BLUE), 2);
        assertEquals(player.getNumberOfCard().getCard(CardColor.BLUE), 2);

        assertFalse(player.getNumberOfCard().getCard(CardColor.YELLOW) != 0);
    }
}