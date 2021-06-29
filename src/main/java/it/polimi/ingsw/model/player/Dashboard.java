package it.polimi.ingsw.model.player;

import it.polimi.ingsw.listeners.DashboardListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeSupport;

import static it.polimi.ingsw.constants.PlayerConstants.STRONGBOX_CHANGE;

/**
 * Dashboard Class represents a player's dashboard.
 *
 * @author Andrea Nocito
 */
public class Dashboard {
    private final FaithTrack faithTrack;
    private final Warehouse warehouse;
    private final ResourceMap strongbox;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Dashboard constructor creates a new Dashboard instance.
     * @param game instance of game.
     */
    public Dashboard(Game game) {
        faithTrack = (game instanceof SinglePlayerGame) ? new SinglePlayerFaithTrack() : new FaithTrack();
        warehouse = new Warehouse();
        strongbox = new ResourceMap();
    }

    /**
     * Method getStrongbox returns the strongbox.
     * @return a ResourceMap.
     */
    public ResourceMap getStrongbox() {
        return strongbox;
    }

    /**
     * Method getWarehouse returns the warehouse.
     * @return the warehouse.
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * Method getFaithTrack returns the faith track.
     * @return the faith track.
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Method addResourcesToStrongbox adds the resources into the strongbox
     */
    public void addResourcesToStrongbox(ResourceMap resources) {
        strongbox.addResources(resources);
        pcs.firePropertyChange(STRONGBOX_CHANGE, null, this.strongbox);
    }

    /**
     * Method removeResourcesFromDashboard tries to remove the specified resources from the shelves and eventually from the strongbox.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean removeResourcesFromDashboard(ResourceMap resourceMap) {
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
     * @return true if after increasing the faith marker position a vatican report has occurred, false otherwise.
     */
    public boolean incrementFaith(int steps) {
        boolean vaticanReport = false;
        for(int i=0; i<steps; i++) {
            if(faithTrack.moveForward()) vaticanReport = true;
        }
        return vaticanReport;
    }

    /**
     * Method incrementBlackFaith increments the position of the black faith marker.
     */
    public boolean incrementBlackFaith(int steps){
        boolean vaticanReport = false;
        for(int i=0; i<steps; i++) {
            if(((SinglePlayerFaithTrack) faithTrack).moveBlackFaithMarker()) vaticanReport = true;
        }
        return vaticanReport;
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

    /**
     * Method addPropertyListener register a DashboardListener to the PropertyChangeSupport of this class.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    public void addPropertyListener(VirtualView virtualView){
        DashboardListener dashboardListener = new DashboardListener(virtualView);
        pcs.addPropertyChangeListener(STRONGBOX_CHANGE, dashboardListener);
        warehouse.addPropertyListener(virtualView);
        faithTrack.addPropertyListener(virtualView);
    }
}
