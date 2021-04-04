package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.Optional;

/**
    * Warehouse Class represents the set of sehlves available on a dashboard
    *
    * @author Andrea Nocito
    */
public class Warehouse {
    private final ArrayList<Shelf> shelves;

    public Warehouse(int numberOfShelves) {
        this.shelves = new ArrayList<>();

        for(int i=1; i<=numberOfShelves; i++) {
            shelves.add( new Shelf(i));
        }
    }
    public void addShelf() {
        shelves.add( new Shelf(shelves.size()+1));
    }
    public void addShelf(int shelfCapacity) {
        shelves.add( new Shelf(shelfCapacity));
    }
    public void addResource(Resource resource, int shelfIndex) {
        if (shelves.size() > shelfIndex) {
            shelves.get(shelfIndex).placeResource(resource);
        }
    }
    public void removeResource(int shelfIndex) {
        if (shelves.size() > shelfIndex && shelves.get(shelfIndex).getResourceType().isPresent() )  {
            shelves.get(shelfIndex).takeResource(shelves.get(shelfIndex).getResourceType().get() );
        }
    }

    public void swapRosource(int shelfIndexStart, int shelfIndexEnd) {
        if (shelves.size() > shelfIndexStart && shelves.size() > shelfIndexEnd) {
            Optional<Resource> resourceTemp = shelves.get(shelfIndexStart).getResourceType();
            if (resourceTemp.isPresent()){
                removeResource(shelfIndexStart);
                addResource(resourceTemp.get(), shelfIndexEnd);
            }
        }
    }
}
