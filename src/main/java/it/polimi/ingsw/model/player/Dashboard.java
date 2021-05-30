package it.polimi.ingsw.model.player;

import it.polimi.ingsw.listeners.DashboardListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import static it.polimi.ingsw.constants.PlayerConstants.SHELF_CHANGE;
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
    private final ResourceMap strongbox;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Dashboard(Game game) {
        faithTrack = (game instanceof SinglePlayerGame) ? new SinglePlayerFaithTrack() : new FaithTrack();
        warehouse = new Warehouse();
        cardSlots = new ArrayList<>();
        strongbox = new ResourceMap();
    }

    /**
     * Method addResourcesToStrongbox adds the resources into the strongbox
     */
    void addResourcesToStrongbox(ResourceMap resources) {
        ResourceMap oldStrongbox = new ResourceMap();
        oldStrongbox.addResources(strongbox);

        strongbox.addResources(resources);
        pcs.firePropertyChange(STRONGBOX_CHANGE, oldStrongbox, this.strongbox);
    }

    public ResourceMap getStrongbox() {
        return strongbox;
    }

    /**
     * Method removeResourcesFromDashboard tries to remove the specified resources from the shelves (and eventually the strongbox)
     * @return true if the operation was successful, false otherwise.
     */
    boolean removeResourcesFromDashboard(ResourceMap resourceMap) {
        ResourceMap totalResources = new ResourceMap();
        totalResources.addResources(warehouse.getResourcesFromWarehouse());
        totalResources.addResources(strongbox);
        //check if there are enough resources
        for(Resource resource : Resource.values()) {
            if (resourceMap.getResource(resource) > totalResources.getResource(resource)) {
                return false;
            }
        }
        ResourceMap resourcesToRemove = new ResourceMap();
        resourcesToRemove.addResources(resourceMap);
        resourcesToRemove =  warehouse.removeResourcesFromWarehouse(resourcesToRemove);
        strongbox.removeResources(resourcesToRemove);
        pcs.firePropertyChange(STRONGBOX_CHANGE, null, strongbox);
        return true;
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
        DashboardListener dashboardListener = new DashboardListener(virtualView);
        pcs.addPropertyChangeListener(STRONGBOX_CHANGE, dashboardListener);
        warehouse.addPropertyListener(virtualView);
        faithTrack.addPropertyListener(virtualView);
    }
}
