package it.polimi.ingsw.model;

import java.util.*;

/**
 * Shelf Class represents a single shelf where the player can store resources
 *
 * @author Andrea Nocito
 */
public class Shelf {
    private Set<Resource> resourcesAllowed;
    private ResourceMap resources;
    private Integer capacity;

    public Shelf(Integer shelfCapacity) {
        resourcesAllowed = new HashSet<>();
        capacity = shelfCapacity;

    }


    private Optional<Resource> getResourceType() {
        return resourcesAllowed.size() > 0 ? Optional.ofNullable(resourcesAllowed.iterator().next()) : Optional.empty();
    }

    public void takeResource(Resource resource) {

    }
    public void placeResource(Resource resource) {

    }
}
