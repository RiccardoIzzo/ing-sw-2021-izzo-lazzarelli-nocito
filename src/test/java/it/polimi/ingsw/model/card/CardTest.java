package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.ResourceMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests:
 * <ul>
 * <li>Card() constructor can instantiates a card for each type of Requirement available</li>
 * <li>getVictoryPoints() works as expected</li>
 * <li>getRequirement() works as expected (checks if the type of requirement is the same as the one used in Card() )</li>
 * </ul>
 */
public class CardTest {
    private ResourceMap resourceMap = new ResourceMap();
    private CardMap cardMap = new CardMap();
    private Requirement requirement1 = new ResourceRequirement(resourceMap);
    private Requirement requirement2 = new NumberRequirement(cardMap);
    private Requirement requirement3 = new LevelRequirement(cardMap);
    private Card card1 = new Card(18, requirement1);
    private Card card2 = new Card(19, requirement2);
    private Card card3 = new Card(20, requirement3);

    @Test
    public void constructor(){
        assertNotNull(card1);
        assertNotNull(card2);
        assertNotNull(card3);
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
}