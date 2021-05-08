package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.ResourceMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests if:
 * <ul>
 * <li>Card() constructor can instantiates a card for each type of Requirement available</li>
 * <li>getVictoryPoints() works as expected</li>
 * <li>getRequirement() works as expected (checks if the type of requirement is the same as the one used in Card() )</li>
 * </ul>
 * @author Gabriele Lazzarelli
 */
public class CardTest {
    private ResourceMap resourceMap;
    private CardMap cardMap;
    private Requirement requirement1;
    private Requirement requirement2;
    private Requirement requirement3;
    private Card card1;
    private Card card2;
    private Card card3;

    @Before
    public void setUp(){
        resourceMap = new ResourceMap();
        cardMap = new CardMap();
        requirement1 = new ResourceRequirement(resourceMap);
        requirement2 = new NumberRequirement(cardMap);
        requirement3 = new LevelRequirement(cardMap);
        card1 = new Card(101, 18, requirement1);
        card2 = new Card(102, 19, requirement2);
        card3 = new Card(103, 20, requirement3);
    }

    @After
    public void tearDown() {
        resourceMap = null;
        cardMap = null;
        requirement1 = null;
        requirement2 = null;
        requirement3 = null;
        card1 = null;
        card2 = null;
        card3 = null;
    }

    @Test
    public void getVictoryPoints() {
        assertEquals(18, card1.getVictoryPoints());
        assertEquals(19, card2.getVictoryPoints());
        assertEquals(20, card3.getVictoryPoints());
    }

    @Test
    public void getRequirement() {
        assertEquals(card1.getRequirement().getClass(), requirement1.getClass());
        assertEquals(card2.getRequirement().getClass(), requirement2.getClass());
        assertEquals(card3.getRequirement().getClass(), requirement3.getClass());
    }

    @Test
    public void getCardID(){
        assertEquals(101, card1.getCardID());
        assertEquals(102, card2.getCardID());
        assertEquals(103, card3.getCardID());
    }
}