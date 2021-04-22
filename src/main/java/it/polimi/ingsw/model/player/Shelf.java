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
     * Method getCapacity returns the actual capacity of the Shelf
     * @return the capacity value.
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * Method getResourceAllowed returns the type of resources allowed to be stored.
     * @return the resourcesAllowed value.
     */
    public Set<Resource> getResourceAllowed() {
        return resourcesAllowed;
    }


    /**
     * Method addResourceAllowed add a type of Resource to the allowed ones that can be stored into the shelf
     * @return true if the operation was successful.
     */
    public boolean addResourceAllowed(Resource resourceToAdd) {
        if (resourcesAllowed.contains(resourceToAdd) ) {
            return false;
        }
        else {
            resourcesAllowed.add(resourceToAdd);
            return true;
        }
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
            if (resources.getResource(res) > 0) {
                totalResources += resources.getResource(res);
                if (!resourcesAllowed.contains(res)) {
                    resourceNotAllowed = true;
                    break;
                }
            }
        }
        if (resourceNotAllowed || this.resources.getMapSize()+totalResources > capacity) {
            return false;
        }
        for(Resource res : resources.getResources().keySet()) {
            this.resources.modifyResource(res,  resources.getResource(res));
        }
        return true;
    }

    /**
     * Method placeResource tries to place the desired Resource inside the shelf
     * @return true if the operation was successful
     */
    public boolean placeResource(Resource resource) {
        if ( resourcesAllowed.contains(resource) && resources.getMapSize() < capacity ) {
            resources.modifyResource(resource, 1);
            return true;
        }
        return false;
    }
}
