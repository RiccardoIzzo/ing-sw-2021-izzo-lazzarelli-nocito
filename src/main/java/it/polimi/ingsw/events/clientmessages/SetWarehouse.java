package it.polimi.ingsw.events.clientmessages;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;

/**
 * SetWarehouse message is used by the player to update the resource configuration in the warehouse (shelves).
 */
public class SetWarehouse implements ClientMessage{
    ArrayList<Resource> warehouse;

    /**
     * Constructor SetWarehouse creates a new SetWarehouse instance.
     * @param warehouse list of resources in the final configuration.
     */
    public SetWarehouse(ArrayList<Resource> warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * Method getWarehouse returns the new configuration for the warehouse.
     * @return list of resources.
     */
    public ArrayList<Resource> getWarehouse() {
        return warehouse;
    }
}
