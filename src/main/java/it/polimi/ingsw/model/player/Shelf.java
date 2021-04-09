package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;

import java.util.*;

/**
 * Shelf Class represents a single shelf where the player can store resources
 *
 * @author Andrea Nocito
 */
public class Shelf {
    private Set<Resource> resourcesAllowed;
    private ResourceMap resources;
    private final Integer capacity;

    public Shelf(Integer shelfCapacity) {
        resourcesAllowed = new HashSet<>();
        resources = new ResourceMap();
        capacity = shelfCapacity;
    }

    public Shelf(Integer shelfCapacity, Set<Resource> allowed) {
        resourcesAllowed = new HashSet<>();
        resourcesAllowed.addAll(allowed);
        resources = new ResourceMap();
        capacity = shelfCapacity;
    }

    /**
     * Method getResourceType returns the first type of resource allowed to be stored.
     * @return the Resource value.
     */
    public Set<Resource> getResourceAllowed() {
        return resourcesAllowed;
    }


    /**
     * Method takeResource tries to remove one unit of the requested Resource.
     * @return true if the operation was successful
     */
    public boolean takeResource(Resource resource) {
        return (resourcesAllowed.contains(resource) && resources.modifyResource(resource , -1));
    }

    /**
     * Method placeResources tries to place the desired Resources inside the shelf.
     * This method checks if all the resources are allowed and if there is enough room to store them.
     * @return true if the operation was successful
     */
    public boolean placeResources(ResourceMap resources) {
        int totalResources = 0;
        boolean resourceNotAllowed = false;
        for(Resource res : resources.getResources().keySet()) {
            totalResources += resources.getResource(res);
            if(!resourcesAllowed.contains(res)) {
                resourceNotAllowed = true;
                break;
            }
        }
        if (resourceNotAllowed || resources.getResources().size()+totalResources< capacity) {
            return false;
        }
        for(Resource res : resources.getResources().keySet()) {
            resources.modifyResource(res,  resources.getResource(res));
        }
        return true;
    }

    /**
     * Method placeResource tries to place the desired Resource inside the shelf
     * @return true if the operation was successful
     */
    public boolean placeResource(Resource resource) {
        if ( resourcesAllowed.contains(resource) && resources.getResources().size() < capacity ) {
            resources.modifyResource(resource, 1);
            return true;
        }
        return false;
    }
}
