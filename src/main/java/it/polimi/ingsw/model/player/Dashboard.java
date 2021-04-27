package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.Card;

import java.util.ArrayList;

/**
 * Dashboard Class represents a player's dashboard
 *
 * @author Andrea Nocito
 */
public class Dashboard {
    private final FaithTrack path;
    private final Warehouse warehouse;
    private final ArrayList<Card> cardSlots;
    private final ResourceMap strongBox;

    public Dashboard(boolean singlePlayer) {
        path = singlePlayer ? new SinglePlayerFaithTrack() : new FaithTrack();
        warehouse = new Warehouse();
        cardSlots = new ArrayList<>();
        strongBox = new ResourceMap();
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
        strongBox.modifyResource(resource, 1);

    }

    public ResourceMap getStrongBox() {
        return strongBox;
    }
    /**
     * Method removeFromWarehouse tries to remove one resource from the shelves inside warehouse.
     * @return true if the operation was successful, false otherwise.
     */
    boolean removeFromWarehouse(Resource resource) {
        return warehouse.removeResource(resource);
    }




    /**
     * Method removeResource removes one resource from the strongBox
     */
    boolean removeResource(Resource resource) {
        return strongBox.modifyResource(resource, -1);

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
