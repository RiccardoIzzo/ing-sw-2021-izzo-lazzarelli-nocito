package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Dashboard {
    private FaithTrack path;
    private Warehouse warehouse;
    private ArrayList<Card> cardSlots;
    private ResourceMap strongBox;

    public Dashboard() {
        path = new FaithTrack();
        warehouse = new Warehouse(3);
        cardSlots = new ArrayList<>();
        strongBox = new ResourceMap();
    }

    void useAbility() {

    }
    void addResource(Resource resource) {
        strongBox.addResource(resource, 1);

    }
    void incrementFaith(int steps) {
        for(int i=0; i<steps; i++) {
            path.moveForward();
        }
    }
    public void incrementBlackFaith(){
        ((SinglePlayerFaithTrack) path).moveBlack();
    }
}
