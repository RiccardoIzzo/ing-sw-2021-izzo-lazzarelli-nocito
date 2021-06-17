package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Class PropertyListener is implemented by all the listeners which observe bound properties changes.
 * @author Gabriele Lazzarelli
 */
public abstract class PropertyListener implements PropertyChangeListener {
    final VirtualView virtualView;

    /**
     * Constructor PropertyListener takes a VirtualView as a parameter.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    protected PropertyListener(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * Method translateLeadersToMap takes a set of LeaderCards and returns a map of the corresponding ids and activation status.
     * @param leaderCards a set of LeaderCards.
     * @return a Map containing the corresponding ids and activation status of the LeaderCards.
     */
    public Map<Integer, Boolean> translateLeadersToMap(HashSet<LeaderCard> leaderCards) {
        Map<Integer,Boolean> leaders = new HashMap<>();
        for (LeaderCard leaderCard : leaderCards) leaders.put(leaderCard.getCardID(),leaderCard.isActive());
        return leaders;
    }

    /**
     * Method translateCards takes a collection of DevelopmentCards and return a list containing the corresponding ids.
     * @param cards a collection of DevelopmentCards.
     * @return a list containing the corresponding ids.
     */
    public ArrayList<Integer> translateCards(Collection<DevelopmentCard> cards) {
        ArrayList<Integer> cardIDs = new ArrayList<>();
        for (Card card : cards) {
            if (card != null){
                cardIDs.add(card.getCardID());
            } else {
                cardIDs.add(null);
            }
        }
        return cardIDs;
    }

    /**
     * Method translateGrid takes a double array of Deck and returns a list containing the top card of each Deck.
     * @param grid a double array of Deck.
     * @return the list containing the ids of the top card of each Deck,
     */
    public ArrayList<Integer> translateGrid(Deck[][] grid){
        ArrayList<Integer> cards = new ArrayList<>();
        for (Deck[] decks : grid) {
            for (Deck deck : decks) {
                if (deck.getTopCard() != null) cards.add(deck.getTopCard().getCardID());
                else cards.add(null);
            }
        }
        return cards;
    }
}
