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
