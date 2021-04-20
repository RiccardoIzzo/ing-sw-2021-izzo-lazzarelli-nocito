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

    public Dashboard() {
        path = new FaithTrack();
        warehouse = new Warehouse();
        cardSlots = new ArrayList<>();
        strongBox = new ResourceMap();
    }

    public Dashboard(int numberOfPlayers) {
        path = numberOfPlayers > 1 ? new SinglePlayerFaithTrack() : new FaithTrack();
        warehouse = new Warehouse();
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
        strongBox.modifyResource(resource, 1);

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
