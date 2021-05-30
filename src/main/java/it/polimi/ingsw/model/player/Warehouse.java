package it.polimi.ingsw.model.player;

import it.polimi.ingsw.listeners.WarehouseListener;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * Warehouse Class represents the set of shelves available on the dashboard
 *
 * @author Gabriele Lazzarelli
 */
public class Warehouse {
    /*
                                      Array[index]
    Shelf  I   - shelfNumber = 1           0
    Shelf  II  - shelfNumber = 2          1,2
    Shelf  III - shelfNumber = 3         3,4,5
    Shelf  IV  - shelfNumber = 4        6,7,8,9      -> LeaderCardShelfA & LeaderCardShelfB
    Shelf  V   - shelfNumber = 5     10,11,12,13,14  -> TemporaryShelf

    Note: shelfNumber is both shelf's number and capacity
     */

    private ArrayList<Resource> shelves;
    private ArrayList<Resource> extraShelfResources;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Warehouse() {
        this.shelves = new ArrayList<>();
        flushShelves();
        this.extraShelfResources = new ArrayList<>();
    }

    /**
     * Method getShelves gets the ArrayList of Resource(s) associated with this Warehouse.
     * @return ArrayList of Resource(s), the Resource(s) associated with this Warehouse.
     */
    public ArrayList<Resource> getShelves() {
        return shelves;
    }

    /**
     * Method setShelves sets the ArrayList of Resource(s) associated with this Warehouse.
     * @param shelves the ArrayList of Resource(s) to set.
     */
    public void setShelves(ArrayList<Resource> shelves) {
        this.shelves = shelves;
        pcs.firePropertyChange(SHELF_CHANGE, null, shelves);
    }

    /**
     * Method getExtraShelfResources gets the Arraylist of Resource(s) associated with the active ExtraShelfLeaderCard(s)
     * of the Player who own this Warehouse.
     * @return ArrayList of Resource(s) of the active ExtraShelfLeaderCard.
     */
    public ArrayList<Resource> getExtraShelfResources() {
        return extraShelfResources;
    }

    /**
     * Method addExtraShelfResource adds a Resource to the ArrayList of Resource(s), the Resource is the Resource allowed in
     * in the extra shelf provided by the ExtraShelfLeaderCard.
     * @param resource the resource to add to extraShelfResources.
     */
    public void addExtraShelfResource(Resource resource) {
        extraShelfResources.add(resource);
        pcs.firePropertyChange(SHELF_CHANGE, null, extraShelfResources);
    }

    /**
     * Method getShelfIndex gets the starting index in shelves of the selected shelf
     * @param shelfNumber the selected shelf
     * @return int, the index of the selected shelf
     */
    public int getShelfIndex(int shelfNumber) {
        return (shelfNumber*shelfNumber - shelfNumber +2)/2 - 1;
    }

    /**
     * Method getResourcesFromShelf return a ResourceMap containing all the resources in the specified shelf
     * @param shelfNumber the specified shelf
     * @return ResourceMap, the resources in the specified shelf
     */
    public ResourceMap getResourcesFromShelf(int shelfNumber) {
        ResourceMap resourceMap = new ResourceMap();
        int shelfIndex = getShelfIndex(shelfNumber);
        for (int i = shelfIndex; i < shelfIndex + shelfNumber; i++) {
            if (shelves.get(i) != null) {
                resourceMap.modifyResource(shelves.get(i), 1);
            }
        }
        return resourceMap;
    }

    /**
     * Method getResourcesFromWarehouse return a ResourceMap containing all the resources in this Warehouse
     * @return ResourceMap, the resources in this Warehouse
     */
    public ResourceMap getResourcesFromWarehouse() {
        ResourceMap resourceMap = new ResourceMap();
        for (int shelfNumber = FIRST_SHELF; shelfNumber <= LAST_SHELF; shelfNumber++) {
            resourceMap.addResources(getResourcesFromShelf(shelfNumber));
        }
        return resourceMap;
    }

    /**
     * Method addResourcesToShelf tries to relocate all the Resource(s) in the resourceMap to the selected shelf
     * @param shelfNumber the number of the selected number
     * @param resourceMap the resources to allocate
     * @return true if the resources are allocated, false if there isn't enough place to allocate the resources
     */
    public boolean addResourcesToShelf(int shelfNumber, ResourceMap resourceMap) {
        int shelfIndex = getShelfIndex(shelfNumber);
        if (resourceMap.size() > shelfNumber) return false;
        for (Resource resource: resourceMap.asList()) {
            shelves.set(shelfIndex, resource);
            shelfIndex++;
        }
        pcs.firePropertyChange(TEMPORARY_SHELF_CHANGE, null, shelves);
        return true;
    }

    /**
     * Method removeResourcesFromShelf removes all the resource of the selected shelf
     * @param shelfNumber the number of the selected number
     * @return a ResourceMap containing the resources removed
     */
    public ResourceMap removeResourcesFromShelf(int shelfNumber) {
        ResourceMap resourceMap = new ResourceMap();
        int shelfIndex = getShelfIndex(shelfNumber);
        for (int i = shelfIndex; i < shelfIndex + shelfNumber; i++) {
            resourceMap.modifyResource(shelves.get(i), 1);
            shelves.set(i, null);
        }
        pcs.firePropertyChange(SHELF_CHANGE, null, shelves);
        return resourceMap;
    }

    /**
     * Method removeResourcesFromWarehouse tries to remove from this Warehouse the resources in the ResourceMap
     * @param resourceMap the resources to remove from this Warehouse
     * @return a ResourceMap, the remaining resources to remove
     */
    public ResourceMap removeResourcesFromWarehouse(ResourceMap resourceMap) {
        int firstIndex = getShelfIndex(FIRST_SHELF);
        int lastIndex = getShelfIndex(LAST_SHELF) + LAST_SHELF;
        for (int i = firstIndex; i < lastIndex; i++){
            if(resourceMap.modifyResource(shelves.get(i), -1)){
                shelves.set(i, null);
            }
        }
        pcs.firePropertyChange(SHELF_CHANGE, null, shelves);
        return resourceMap;
    }

    /**
     * Method flushShelves removes all resources from this Warehouse
     */
    public void flushShelves() {
        int firstIndex = getShelfIndex(FIRST_SHELF);
        int lastIndex = getShelfIndex(TEMPORARY_SHELF) + TEMPORARY_SHELF;
        shelves.clear();
        for (int i = firstIndex; i < lastIndex; i++) {
            shelves.add(null);
        }
    }

    /**
     * Method setPropertyChangeSupport sets a PropertyChangeSupport for this Warehouse,
     * it is called by the Dashboard class which passes its own PropertyChangeSupport
     * @param pcs, the PropertyChangeSupport of the Player who owns this Warehouse
     */
    public void setPropertyChangeSupport(PropertyChangeSupport pcs) {
        this.pcs = pcs;
    }

    public void addPropertyListener(VirtualView virtualView){
        WarehouseListener warehouseListener = new WarehouseListener(virtualView);
        pcs.addPropertyChangeListener(SHELF_CHANGE, warehouseListener);
        pcs.addPropertyChangeListener(TEMPORARY_SHELF_CHANGE, warehouseListener);
    }
}
