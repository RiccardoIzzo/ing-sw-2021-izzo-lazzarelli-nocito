package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class CardMap is the ADT used to handle collections of Cards.
 */
public class CardMap {

    private Map<CardColor,Integer> cards;

    /**
     * Constructor CardMap creates a new CardMap instance.
     * All the CardColor counters are set to 0.
     */
    public CardMap() {
        cards = new HashMap<>();

        for (CardColor value : CardColor.values()) {
            cards.put(value, 0);
        }
    }

    /**
     * Method getCards returns an ArrayList of Integers with the number of each CardColor.
     * @return an ArrayList of Integers for each CardColor.
     */
    public ArrayList<Integer> getCards() { return (ArrayList<Integer>) cards.values(); }

    /**
     * Method getCard returns the number of the selected CardColor type.
     * @param type CardColor of interest.
     * @return the value of CardColor type.
     */
    public int getCard(CardColor type){
        return cards.getOrDefault(type, 0);
    }
}
