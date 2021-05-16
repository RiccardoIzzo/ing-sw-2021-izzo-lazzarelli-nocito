package it.polimi.ingsw.model;

import java.util.EnumMap;
import java.util.Map;
/**
 * ResourceMap class represents a set of available resources with associated quantities
 *
 * @author Andrea Nocito
 */

public class ResourceMap {
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
     * @return the associated Integer.
     */
    public Integer getResource(Resource type) {
        return resources.get(type);
    }

    /**
     * Method getMapSize returns  quantity of resources inside ResourceMap
     * @return the associated Integer.
     */
    public Integer getMapSize() {
        int total = 0;
        for (Map.Entry<Resource, Integer> entry : resources.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }


    /**
     * Method addResources receives a Map of Resources and associated Integers to add.
     */
    public void addResources(ResourceMap resourceMap) {
        for (Resource resource : Resource.values()) {
            this.modifyResource(resource, resourceMap.getResource(resource));
        }
    }
    /**
     * Method modifyResource receives the resource type and the Integer value to add to such resource.
     * @return true if the operation was successful
     */
    public boolean modifyResource(Resource type, Integer value) {
        int num = resources.get(type) + value;
        if(num >= 0) {
            resources.replace(type, num);
            return true;
        }
        return false;
    }

    /**
     * Method flush resets resourceMap to its initial state, with value 0 for each Resource
     */
    public void flush() {
        for (Resource value : Resource.values()) {
            resources.put(value, 0);
        }
    }
}
