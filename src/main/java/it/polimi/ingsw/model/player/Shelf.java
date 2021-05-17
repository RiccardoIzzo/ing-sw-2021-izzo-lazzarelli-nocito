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
        Collections.addAll(resourcesAllowed, Resource.values());
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
     * Method getResources returns the size of each type of resource stored into the shelf
     * @return the resources values.
     */

    public ResourceMap getResources() {
        return resources;
    }
    /**
     * Method getResourceAllowed returns the type of resources allowed to be stored.
     * @return the resourcesAllowed value.
     */
    public Set<Resource> getResourceAllowed() {
        return resourcesAllowed;
    }


    /**
     * Method addResource add a Resource into the shelf
     * @return true if the operation was successful.
     */
    public boolean addResource(Resource resource) {
        if ((resourcesAllowed.contains(resource) && resources.getMapSize() < capacity)) {
            resourcesAllowed.add(resource);
            return true;
        }
        return false;
    }

    /**
     * Method takeResource tries to remove one unit of the requested Resource.
     * @return true if the operation was successful
     */
    public boolean takeResource(Resource resource) {
        return resourcesAllowed.contains(resource) && resources.modifyResource(resource, -1);
    }

    /**
     * Method placeResources tries to place the desired Resources inside the shelf.
     * This method checks if all the resources are allowed and if there is enough room to store them.
     * @return true if the operation was successful
     */
    public boolean placeResources(ResourceMap resources) {
        //Check if the resource to place are allowed in the shelf
        for(Resource resource : Resource.values()) {
            if (resources.getResource(resource) > 0) {
                if (!resourcesAllowed.contains(resource)) {
                    return false;
                }
            }
        }

        //Check if there's enough room to place the resources
        if (this.resources.getMapSize()+resources.getMapSize() > capacity) {
            return false;
        }

        //? : a single resource is allowed per shelf - to fix
        for(Resource resource : Resource.values()) {
            this.resources.modifyResource(resource, resources.getResource(resource));
        }

        return true;
    }

    /**
     * Method placeResource tries to place the desired Resource inside the shelf
     * @return true if the operation was successful
     */
    public boolean placeResource(Resource resource) {
        if (resourcesAllowed.contains(resource) && resources.getMapSize() < capacity) {
            resources.modifyResource(resource, 1);
            return true;
        }
        return false;
    }
}
