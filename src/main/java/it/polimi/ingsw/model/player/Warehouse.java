package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;

import java.util.ArrayList;
import java.util.Optional;

/**
    * Warehouse Class represents the set of shelves available on a dashboard
    *
    * @author Andrea Nocito
    */
public class Warehouse {
    private ArrayList<Shelf> shelves;
    private static final int NUMBER_SHELF = 3;

    public Warehouse() {
        this.shelves = new ArrayList<>();

        for(int i=1; i<=NUMBER_SHELF; i++) {
            shelves.add( new Shelf(i));
        }
    }
    public void addShelf() {
        shelves.add( new Shelf(shelves.size()+1));
    }
    public void addShelf(int shelfCapacity) {
        shelves.add( new Shelf(shelfCapacity));
    }
    public void addShelf(Shelf shelf) {
        shelves.add(shelf);
    }
    public boolean addResource(Resource resource, int shelfIndex) {
        if (shelves.size() > shelfIndex) {
            shelves.get(shelfIndex).placeResource(resource);
            return true;
        }
        return false;
    }

    /**
     * Method addResourceIntoTemporaryShelf creates a new temporary Shelf and adds the Resources into it.
     */
    public void addResourcesIntoTemporaryShelf(ResourceMap resources) {
        Shelf tempShelf = new Shelf(resources.getResources().size());
        tempShelf.placeResources(resources);
        shelves.add(tempShelf);
    }

    /**
     * Method removeResourcesFromTemporaryShelf removes the temporary shelf created by addResourceIntoTemporaryShelf
     */
    public void removeResourcesFromTemporaryShelf() {
        shelves.remove(shelves.size()-1);
    }
    /**
     * Method removeResource tries  to remove the last resource unit inside the specified shelf.
     * @return true is the operation was successful
     */
    public boolean removeResource(int shelfIndex) {
        if (shelves.size() > shelfIndex && shelves.get(shelfIndex).getResourceType().isPresent() )  {
            shelves.get(shelfIndex).takeResource(shelves.get(shelfIndex).getResourceType().get() );
            return true;
        }
        return false;
    }

    /**
     * Method swapResource tries  to swap the position fo two resources from two differnt shelves
     * @return true is the operation was successful
     */
    public boolean swapResource(int shelfIndexStart, int shelfIndexEnd) {
        if (shelves.size() > shelfIndexStart && shelves.size() > shelfIndexEnd) {
            Optional<Resource> resourceTemp = shelves.get(shelfIndexStart).getResourceType();
            if (resourceTemp.isPresent()){
                removeResource(shelfIndexStart);
                addResource(resourceTemp.get(), shelfIndexEnd);
                return true;
            }
        }
        return false;
    }
}
