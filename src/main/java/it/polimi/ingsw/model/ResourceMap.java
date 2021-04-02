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
     * Method addResources receives a Map of Resources and associated Integers to add.
     */
    public void addResources(ResourceMap toAdd) {
        for (Map.Entry<Resource, Integer> entry : toAdd.resources.entrySet()) {
            resources.replace(entry.getKey() , entry.getValue()+resources.get(entry.getKey()));
        }
    }
    /**
     * Method addResource receives the resource type and the Integer value to add to such resource.
     */
    public void addResource(Resource type, Integer value) {
        resources.replace(type, resources.get(type)+value);
    }

    /**
     * Method flush resets resourceMap to its initial state, with value 0 for each Resource
     */
    public void flush() {
        resources.clear();
        for (Resource value : Resource.values()) {
            resources.put(value, 0);
        }
    }
}
