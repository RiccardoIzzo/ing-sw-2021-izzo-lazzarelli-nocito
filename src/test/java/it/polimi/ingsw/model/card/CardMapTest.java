package it.polimi.ingsw.model.card;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests if:
 * <ul>
 * <li>flush() writes each value to zero (note that flush is part of the constructor)</li>
 * <li>put() works as expected</li>
 * </ul>
 * @author Gabriele Lazzarelli
 */
public class CardMapTest {
    CardMap cardMap;

    /**
     * Method setUp creates an instance of CardMap and adds to it the following values.
     */
    @Before
    public void setUp() {
        cardMap = new CardMap();
        cardMap.put(CardColor.GREEN, 1);
        cardMap.put(CardColor.YELLOW, 3);
        cardMap.put(CardColor.BLUE, 5);
        cardMap.put(CardColor.PURPLE, 7);
    }

    /**
     * Method tearDown removes the reference to the CardMap created.
     */
    @After
    public void tearDown() {
        cardMap = null;
    }

    /**
     * Method flush tests if the method flush brings the CardMap to its original state.
     */
    @Test
    public void flush() {
        cardMap.flush();
        for (CardColor cardColor : CardColor.values()) {
            assertEquals(cardMap.getCard(cardColor), 0);
        }
    }

    /**
     * Method put tests if the method put works as expected.
     */
    @Test
    public void put() {
        assertEquals(cardMap.getCard(CardColor.GREEN), 1);
        assertEquals(cardMap.getCard(CardColor.YELLOW), 3);
        assertEquals(cardMap.getCard(CardColor.BLUE), 5);
        assertEquals(cardMap.getCard(CardColor.PURPLE), 7);
    }
}