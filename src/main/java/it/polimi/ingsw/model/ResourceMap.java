package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResourceMap {
    Map<Resource , Integer> resources;

    /**
     * Constructor ResourceMap create a new ResourceMap instance.
     * All the resources counters are set to 0.
     */
    public ResourceMap() {
        resources = new HashMap<>();

        for (Resource value : Resource.values()) {
            resources.put(value, 0);
        }
    }

    /**
     * Method getResource returns the available quantity of the specified Resource.
     * @return the associated Integer.
     */
    public Integer getResource(Resource type) {
        return resources.get(type);
    }

    /**
     * Method getResources returns an ArrayList of Integers with all the available quantities of each Resource.
     * @return the an ArrayList of the associated Integers.
     */
    public ArrayList<Integer> getResources() {
        return (ArrayList<Integer>) resources.values();
    }

    /**
     * Method addResources receives an ArrayList of Integers with all the quantities to add on each Resource.
     */
    public void addResources(ArrayList<Integer> toAdd) {
        for (int i = 0; i < toAdd.size(); i++) {
            Resource resTemp = Resource.values()[i];
            resources.replace(resTemp, resources.get(resTemp)+toAdd.get(i));
        }
    }
    /**
     * Method addResource receives the resource type and the Integer value to add to such resource.
     */
    public void addResource(Resource type, Integer value) {
        resources.replace(type, resources.get(type)+value);
    }
}
