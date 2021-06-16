package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * ResourceMap class represents a set of available resources with associated quantities.
 *
 * @author Andrea Nocito
 */
public class ResourceMap implements Serializable {
    private final Map<Resource , Integer> resources;

    /**
     * Constructor ResourceMap creates a new ResourceMap instance.
     * All the resources counters are set to 0.
     */
    public ResourceMap() {
        resources = new EnumMap<>(Resource.class);

        for (Resource value : Resource.values()) {
            resources.put(value, 0);
        }
    }

    /**
     * Method getResources returns a Map with the available quantities for each Resource.
     * @return the Map.
     */
    public Map <Resource, Integer> getResources() {
        return resources;
    }

    /**
     * Method getResource returns the available quantity of the specified Resource.
     * @param resource the specified Resource.
     * @return the quantity for the specified Resource in this ResourceMap.
     */
    public Integer getResource(Resource resource) {
        return resources.get(resource);
    }

    /**
     * Method getAmount returns the total amount of resources inside this ResourceMap.
     * @return the amount of resources.
     */
    public Integer getAmount() {
        int total = 0;
        for (Resource resource: Resource.values()) {
            total += getResource(resource);
        }
        return total;
    }

    /**
     * Method asList return an ArrayList with all the resource this ResourceMap contains.
     * @return an ArrayList containing all the resource of this ResourceMap.
     */
    public ArrayList<Resource> asList() {
        ArrayList<Resource> resourcesList = new ArrayList<>();
        for (Resource resource: Resource.values()){
            for (int i = 0; i < this.getResource(resource); i++) {
                resourcesList.add(resource);
            }
        }
        return resourcesList;
    }

    /**
     * Method modifyResource updates the quantity of the selected resource.
     * @param type resource type.
     * @param value amount of selected resource to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean modifyResource(Resource type, Integer value) {
        if ( type == null ) {
            return false;
        }
        int newValue = this.getResource(type) + value;
        if(newValue >= 0) {
            resources.put(type, newValue);
            return true;
        }
        return false;
    }

    /**
     * Method addResources receives a ResourceMap and adds to this ResourceMap the amount of resources of the ResourceMap received.
     */
    public ResourceMap addResources(ResourceMap resourceMap) {
        for (Resource resource : Resource.values()) {
            this.modifyResource(resource, resourceMap.getResource(resource));
        }
        return this;
    }

    /**
     * Method removeResource removes the resources contained in the resource map received.
     * @param resourceMap the resource map that contains the resources to remove.
     * @return true if the the operation was successful, false if there are not enough resource in this ResourceMap.
     */
    public boolean removeResources(ResourceMap resourceMap) {
        for (Resource resource: Resource.values()) {
            if (this.getResource(resource) < resourceMap.getResource(resource)) {
                return false;
            }
        }
        for (Resource resource: Resource.values()) {
            this.modifyResource(resource, -1 * resourceMap.getResource(resource));
        }
        return true;
    }

    /**
     * Method flush resets this resource map to its initial state.
     */
    public void flush() {
        for (Resource value : Resource.values()) {
            resources.put(value, 0);
        }
    }

    /**
     * Method toString returns the string representation of this ResourceMap.
     * @return the string representation.
     */
    @Override
    public String toString() {
        return resources.toString();
    }
}
