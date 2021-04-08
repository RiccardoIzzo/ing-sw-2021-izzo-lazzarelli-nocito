package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.Card;

import java.util.ArrayList;

public class Dashboard {
    private final FaithTrack path;
    private final Warehouse warehouse;
    private final ArrayList<Card> cardSlots;
    private final ResourceMap strongBox;

    public Dashboard() {
        path = new FaithTrack();
        warehouse = new Warehouse(3);
        cardSlots = new ArrayList<>();
        strongBox = new ResourceMap();
    }

    void useAbility() {

    }

    /**
     * Method addShelf receives a shelf and adds it to the list
     */
    public void addShelf(Shelf shelf) {
        warehouse.addShelf(shelf);
    }

    /**
     * Method addResource adds one resource into the strongBox
     */
    void addResource(Resource resource) {
        strongBox.addResource(resource, 1);

    }

    /**
     * Method incrementFaith receives the number of steps to take and makes the path move forward.
     */
    void incrementFaith(int steps) {
        for(int i=0; i<steps; i++) {
            path.moveForward();
        }
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public FaithTrack getPath() {
        return path;
    }

    /**
     * Method incrementBlackFaith makes the path move the black cross forward.
     */

    public void incrementBlackFaith(){
        ((SinglePlayerFaithTrack) path).moveBlack();
    }
}
