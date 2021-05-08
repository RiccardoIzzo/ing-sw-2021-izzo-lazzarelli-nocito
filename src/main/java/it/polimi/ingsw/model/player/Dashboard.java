package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.Card;

import java.util.ArrayList;
import java.util.Map;

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

    public Dashboard(Game game) {
        path = (game instanceof SinglePlayerGame) ? new SinglePlayerFaithTrack() : new FaithTrack();
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

    /**
     * Method addResources adds the resources into the strongBox
     */
    void addResources(ResourceMap resources) {
        for( Resource res : resources.getResources().keySet()){
            strongBox.modifyResource(res, resources.getResource(res));
        }

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
     * Method removeResources tries to remove the specified resources from the shelves (and eventually the strongbox)
     * @return true if the operation was successful, false otherwise.
     */
    boolean removeResources(ResourceMap resources) {
        ResourceMap res = warehouse.getResourcesSize();
        res.addResources(strongBox);
        Map<Resource, Integer> resourcesToTake = resources.getResources();
        for(Resource resource : resourcesToTake.keySet()) {
            if (resourcesToTake.get(resource) > res.getResource(resource)) {
                return false;
            }
        }
        for(Resource resource : resourcesToTake.keySet()) {
            for (int i = 0; i<resourcesToTake.get(resource); i++) {
                if (!removeFromWarehouse(resource)) {
                    removeResource(resource);
                }
            }
        }
        return true;
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
