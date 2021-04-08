package it.polimi.ingsw.model.card;

import java.util.*;

/**
 * Class Deck represents a stack of Card
 * @author Gabriele Lazzarelli
 */
public class Deck {
    private final Stack<Card> cards;

    /**
     * Constructor Deck creates a new Deck instance with four cards shuffled.
     * @param list a list of four Card to add to the deck.
     */
    public Deck(List<Card> list) {
        this.cards = new Stack<>();
        cards.addAll(list);
        Collections.shuffle(cards);
    }

    /**
     * Method getCards returns this Deck of Card
     * @return a Stack<Card>, this Deck of Card
     */
    public Stack<Card> getCards() {
        return cards;
    }

    /**
     * Method getTopCard returns the Card at the top of this Deck of Card
     * @return a Card, the Card at the top of this Deck
     */
    public Card getTopCard(){
        Card card = null;

        try {
            card = cards.peek();
        } catch (EmptyStackException e) {
            System.out.println(e.toString());
        }

        return card;
    }

    /**
     * Method draw returns the Card at the top of this Deck and removes it from the Deck
     * @return a Card, the Card at the top of this Deck
     */
    public Card draw(){
        Card card = null;

        try {
            card = cards.pop();
        } catch (EmptyStackException e) {
            System.out.println(e.toString());
        }

        return card;
    }

}
