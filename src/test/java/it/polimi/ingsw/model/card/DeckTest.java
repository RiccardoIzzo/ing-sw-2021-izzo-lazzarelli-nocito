package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.JsonCardsCreator;
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
    private List<DevelopmentCard> listOfCards;

    /**
     * Method setUp creates an instance of Deck
     */
    @Before
    public void setUp() {
        listOfCards = new ArrayList<>();
        listOfCards.add(JsonCardsCreator.generateDevelopmentCard(123));
        listOfCards.add(JsonCardsCreator.generateDevelopmentCard(140));
        listOfCards.add(JsonCardsCreator.generateDevelopmentCard(113));
        listOfCards.add(JsonCardsCreator.generateDevelopmentCard(120));
        //add Cards to the Deck
        deck = new Deck(listOfCards);
    }

    /**
     * Method tearDown removes the reference to the Deck instance
     */
    @After
    public void tearDown() {
        deck = null;
    }

    /**
     * Method getCards checks if the Deck contains all cards previously added
     */
    @Test
    public void getCards() {
        assertTrue(deck.getCards().containsAll(listOfCards));
        assertTrue(listOfCards.containsAll(deck.getCards()));
    }

    /**
     * Method draw tests if the method works as expected
     */
    @Test
    public void draw() {
        Card oldTopCard = deck.getTopCard();
        Card card = deck.draw();
        assertSame(oldTopCard, card);
        assertFalse(deck.getCards().contains(card));
    }
}