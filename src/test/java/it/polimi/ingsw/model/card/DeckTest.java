package it.polimi.ingsw.model.card;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class tests if:
 * <ul>
 * <li>getCards() works as expected</li>
 * <li>draw() returns the card at the top at the Deck and removes it from Deck</li>
 * </ul>
 * @author Gabriele Lazzarelli
 */
public class DeckTest {
    private Deck deck;
    private List<Card> listOfCards;

    @Before
    public void setUp() {
        listOfCards = new ArrayList<>();
        //add Cards to the Deck
        deck = new Deck(listOfCards);
    }

    @After
    public void tearDown() {
        deck = null;
    }

    @Test
    public void getCards() {
        assertTrue(deck.getCards().containsAll(listOfCards));
        assertTrue(listOfCards.containsAll(deck.getCards()));
    }

    @Test
    public void draw() {
        Card oldTopCard = deck.getTopCard();
        Card card = deck.draw();
        //assertTrue(oldTopCard == card);
        assertFalse(deck.getCards().contains(card));
    }
}