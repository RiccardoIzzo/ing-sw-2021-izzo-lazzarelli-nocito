package it.polimi.ingsw.model.card;

import java.util.EnumMap;
import java.util.Map;

/**
 * Class CardMap is the ADT used to keep track of the amount (or level) and type in a collection of Card.
 * @author Gabriele Lazzarelli
 */
public class CardMap {

    private final Map<CardColor,Integer> cards;

    /**
     * Constructor CardMap creates a new CardMap instance.
     * All the CardColor counters are set to 0.
     */
    public CardMap() {
        cards = new EnumMap<>(CardColor.class);
        this.flush();
    }

    /**
     * Method getCards returns this.cards.
     * @return an EnumMap, this.cards.
     */
    public Map<CardColor, Integer> getCards() {
        return cards;
    }

    /**
     * Method getCard returns the number of the selected CardColor type.
     * @param type CardColor of interest.
     * @return the value of CardColor type.
     */
    public int getCard(CardColor type){
        return cards.getOrDefault(type, 0);
    }

    /**
     * Method addCard increments the specified CardColor value of this CardMap
     * @param cardColor is the CardColor key which value is to increment
     * @param value the amount to increment
     */
    public void addCard(CardColor cardColor, int value) {
        int toIncrement = this.getCard(cardColor);
        this.put(cardColor, value + toIncrement);
    }

    /**
     * Methods addCards takes another CardMap as input and for each CardColor key increases this CardColor value
     * by the CardColor value of the CardMap parameter.
     * @param cardMap is a CardMap, which contents are added to this CardMap
     */
    public void addCards(CardMap cardMap){
        for (CardColor cardColor : CardColor.values()) {
            int toIncrement = this.getCard(cardColor);
            int increment = cardMap.getCard(cardColor);
            this.put(cardColor, toIncrement + increment);
        }
    }

    /**
     * Method flush sets each CardColor key of this CardMap to 0.
     */
    public void flush(){
        for (CardColor cardColor : CardColor.values()) {
            cards.put(cardColor, 0);
        }
    }

    /**
     * Method addCard sets a value to a specified CardColor of this CardMap.
     * @param cardColor is the CardColor key to set
     * @param value is the CardColor's value to set
     */
    public void put(CardColor cardColor, int value) {
        cards.put(cardColor, value);
    }
}
