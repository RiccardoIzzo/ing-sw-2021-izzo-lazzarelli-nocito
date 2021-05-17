package it.polimi.ingsw.model.player;

import it.polimi.ingsw.listeners.DashboardListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import static it.polimi.ingsw.constants.PlayerConstants.STRONGBOX_CHANGE;

/**
 * Dashboard Class represents a player's dashboard
 *
 * @author Andrea Nocito
 */
public class Dashboard {
    private final FaithTrack faithTrack;
    private final Warehouse warehouse;
    private final ArrayList<Card> cardSlots;
    private final ResourceMap strongBox;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Dashboard(Game game) {
        faithTrack = (game instanceof SinglePlayerGame) ? new SinglePlayerFaithTrack() : new FaithTrack();
        warehouse = new Warehouse();
        cardSlots = new ArrayList<>();
        strongBox = new ResourceMap();
    }

    /**
     * Method addShelf receives a shelf and adds it to the list
     */
    public void addShelf(Shelf shelf) {
        warehouse.addShelf(shelf);
    }

    /**
     * Method addResource adds one resource into the strongBox
     */
    void addResource(Resource resource) {
        strongBox.modifyResource(resource, 1);
    }

    /**
     * Method addResources adds the resources into the strongBox
     */
    void addResources(ResourceMap resources) {
        ResourceMap oldStrongbox = new ResourceMap();
        oldStrongbox.addResources(strongBox);

        strongBox.addResources(resources);
        pcs.firePropertyChange(STRONGBOX_CHANGE, oldStrongbox, this.strongBox);
    }

    public ResourceMap getStrongBox() {
        return strongBox;
    }

    /**
     * Method removeFromWarehouse tries to remove one resource from the shelves inside warehouse.
     * @return true if the operation was successful, false otherwise.
     */
    boolean removeFromWarehouse(Resource resource) {
        return warehouse.removeResource(resource);
    }

    /**
     * Method removeResources tries to remove the specified resources from the shelves (and eventually the strongbox)
     * @return true if the operation was successful, false otherwise.
     */
    boolean removeResources(ResourceMap resources) {
        ResourceMap totalResources = warehouse.getResourcesSize();
        totalResources.addResources(strongBox);
        for(Resource resource : Resource.values()) {
            if (resources.getResource(resource) > totalResources.getResource(resource)) {
                return false;
            }
        }
        for(Resource resource : Resource.values()) {
            for (int i = resources.getResource(resource); i > 0; i--) {
                if (!removeFromWarehouse(resource)) {
                    for (int j = i; j > 0; j--) {
                        removeResource(resource);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Method removeResource removes one resource from the strongBox
     */
    boolean removeResource(Resource resource) {
        ResourceMap oldStrongbox = new ResourceMap();
        oldStrongbox.addResources(strongBox);

        if (strongBox.modifyResource(resource, -1)) {
            pcs.firePropertyChange(STRONGBOX_CHANGE, oldStrongbox, this.strongBox);
            return true;
        }
        return false;
    }

    /**
     * Method incrementFaith receives the number of steps to take and makes the path move forward.
     */
    public void incrementFaith(int steps) {
        for(int i=0; i<steps; i++) {
            faithTrack.moveForward();
        }
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Method incrementBlackFaith makes the path move the black cross forward.
     */
    public void incrementBlackFaith(){
        ((SinglePlayerFaithTrack) faithTrack).moveBlack();
    }

    /**
     * Method setPropertyChangeSupport sets a PropertyChangeSupport for this Dashboard,
     * it is called by the Player class which passes its own PropertyChangeSupport
     * @param pcs, the PropertyChangeSupport of the Player who owns this Dashboard
     */
    public void setPropertyChangeSupport(PropertyChangeSupport pcs) {
        this.pcs = pcs;
        warehouse.setPropertyChangeSupport(pcs);
        faithTrack.setPropertyChangeSupport(pcs);
    }

    public void addPropertyListener(VirtualView virtualView){
        pcs.addPropertyChangeListener(new DashboardListener(virtualView));
        warehouse.addPropertyListener(virtualView);
        faithTrack.addPropertyListener(virtualView);
    }
}
