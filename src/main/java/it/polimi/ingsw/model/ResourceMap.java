package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
/**
 * ResourceMap class represents a set of available resources with associated quantities
 *
 * @author Andrea Nocito
 */

public class ResourceMap implements Serializable {
    Map<Resource , Integer> resources;

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
     * Method getResources returns an ArrayList of Integers with all the available quantities of each Resource.
     * @return the an ArrayList of the associated Integers.
     */
    public Map <Resource, Integer> getResources() {
        return resources;
    }

    /**
     * Method getResource returns the available quantity of the specified Resource.
     * @param resource the specified Resource
     * @return the quantity for the specified Resource in this ResourceMap
     */
    public Integer getResource(Resource resource) {
        return resources.get(resource);
    }

    /**
     * Method size return the amount of resources inside this ResourceMap
     * @return the associated Integer.
     */
    public Integer size() {
        int total = 0;
        for (Resource resource: Resource.values()) {
            total += getResource(resource);
        }
        return total;
    }

    /**
     * Method asList return an ArrayList with all the resource this ResourceMap contains
     * @return an ArrayList containing all the resource of this ResourceMap
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
     * Method modifyResource receives the resource type and the Integer value to add to such resource.
     * @return true if the operation was successful
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
     * Method removeResource receives a ResourceMap and removes from this ResourceMap the amount of resources of the ResourceMap received.
     * @return true if the the operation is successful, false if there are not enough resource in this ResourceMap
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
     * Method flush resets resourceMap to its initial state, with value 0 for each Resource
     */
    public void flush() {
        for (Resource value : Resource.values()) {
            resources.put(value, 0);
        }
    }

    @Override
    public String toString() {
        return resources.toString();
    }
}
