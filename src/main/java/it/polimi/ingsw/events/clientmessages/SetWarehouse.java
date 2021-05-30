package it.polimi.ingsw.events.clientmessages;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;

public class SetWarehouse implements ClientMessage{
    ArrayList<Resource> warehouse;

    public SetWarehouse(ArrayList<Resource> warehouse) {
        this.warehouse = warehouse;
    }

    public ArrayList<Resource> getWarehouse() {
        return warehouse;
    }
}
