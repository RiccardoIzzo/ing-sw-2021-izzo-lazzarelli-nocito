package it.polimi.ingsw.model.card;

import it.polimi.ingsw.listeners.LeaderCardListener;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Class LeaderCard represents the leader card type in the game.
 * @author Gabriele Lazzarelli
 */
public abstract class LeaderCard extends Card {
    private boolean active;
    private String ACTIVE = "active"; //property name
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Constructor of the superclass Card takes two parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * @param victoryPoints are the card's victory points.
     * @param requirement   is the card's requirement.
     */
    public LeaderCard(int victoryPoints, Requirement requirement) {
        super(victoryPoints, requirement);
        active = false;
    }

    /**
     * Method isActive returns true if the LeaderCard has been activated, false if not.
     * @return true if the selected LeaderCard is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method setActive is called when a LeaderCard is activated.
     * It is called once and sets active=true.
     * @param active is set true for active LeaderCard.
     */
    public void setActive(boolean active) {
        this.active = active;
        pcs.firePropertyChange(ACTIVE, this.active, active);
    }

    public void addListener(VirtualView virtualView){
        pcs.addPropertyChangeListener(ACTIVE, new LeaderCardListener(virtualView));
    }
}
