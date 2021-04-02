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
    private final Set<Resource> resourcesAllowed;
    private ResourceMap resources;
    private final Integer capacity;

    public Shelf(Integer shelfCapacity) {
        resourcesAllowed = new HashSet<>();
        capacity = shelfCapacity;
    }


    public Optional<Resource> getResourceType() {
        return resourcesAllowed.size() > 0 ? Optional.ofNullable(resourcesAllowed.iterator().next()) : Optional.empty();
    }

    public void takeResource(Resource resource) {
        if ( resourcesAllowed.contains(resource) && resources.getResource(resource) > 0 ) {
            resources.addResource(resource , -1);
        }
    }
    public void placeResource(Resource resource) {
        if ( resourcesAllowed.contains(resource) && resources.getResources().size() < capacity ) {
            resources.addResource(resource, 1);
        }
    }
}
