package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Class Listener is implemented by all the listeners which observe bound properties changes.
 * @author Gabriele Lazzarelli
 */
public abstract class PropertyListener implements PropertyChangeListener {
    final VirtualView virtualView;

    protected PropertyListener(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    public Map<Integer, Boolean> translateLeadersToMap(HashSet<LeaderCard> leaderCards) {
        Map<Integer,Boolean> leaders = new HashMap<>();
        for (LeaderCard leaderCard : leaderCards) leaders.put(leaderCard.getCardID(),leaderCard.isActive());
        return leaders;
    }

    public ArrayList<Integer> translateGrid(Deck[][] grid){
        ArrayList<Integer> cards = new ArrayList<>();
        for (Deck[] decks : grid) {
            for (Deck deck : decks) {
                cards.add(deck.getTopCard().getCardID());
            }
        }
        return cards;
    }

}
