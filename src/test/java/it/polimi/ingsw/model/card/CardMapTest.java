package it.polimi.ingsw.model.card;

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
    CardMap cardMap1 = new CardMap();
    CardMap cardMap2 = new CardMap();

    @Test
    public void flush() {
        for (CardColor cardColor : CardColor.values()) {
            assertEquals(cardMap1.getCard(cardColor), 0);
        }
    }

    @Test
    public void put() {
        cardMap2.put(CardColor.GREEN, 1);
        cardMap2.put(CardColor.YELLOW, 3);
        cardMap2.put(CardColor.BLUE, 5);
        cardMap2.put(CardColor.PURPLE, 7);

        assertEquals(cardMap2.getCard(CardColor.GREEN), 1);
        assertEquals(cardMap2.getCard(CardColor.YELLOW), 3);
        assertEquals(cardMap2.getCard(CardColor.BLUE), 5);
        assertEquals(cardMap2.getCard(CardColor.PURPLE), 7);
    }
}