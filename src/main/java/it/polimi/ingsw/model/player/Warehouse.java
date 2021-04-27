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
    public int getShelvesSize() {return shelves.size();}

    public int getNumberShelf() {
        return NUMBER_SHELF;
    }
    public Optional<Shelf> getShelf(int shelfIndex) {
        if (shelves.size() > shelfIndex) {
            return Optional.ofNullable(shelves.get(shelfIndex));
        }
        return Optional.empty();
    }
    /**
     * Method addResourceIntoTemporaryShelf creates a new temporary Shelf and adds the Resources into it.
     */
    public void addResourcesIntoTemporaryShelf(ResourceMap resources) {
        Shelf tempShelf = new Shelf(resources.getResources().size(), resources.getResources().keySet());
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
     * Method removeResource with @param Resource tries to remove the first resource it finds that matches the specificed type.
     * @return true is the operation was successful
     */
    public boolean removeResource(Resource resource) {
        int i = 0;
        while (i < shelves.size()) {
           if (shelves.get(i).takeResource(resource)) {
               return  true;
           }
        }
        return false;
    }

    /**
     * Method getResourcesSize counts the amount of each resource avaiable on the shelves
     * @return ResourceMap with the amount of each resource
     */
    public ResourceMap getResourcesSize() {
        ResourceMap res = new ResourceMap();
        for (Shelf shelf : shelves) {
            res.addResources(shelf.getResources());
        }
        return res;
    }

    /**
     * Method removeResource tries to remove the last resource unit inside the specified shelf.
     * @return true is the operation was successful
     */
    public boolean removeResource(int shelfIndex, Resource resource) {
        if (shelves.size() > shelfIndex && shelves.get(shelfIndex).getResourceAllowed().contains(resource))  {
            Shelf tempShelf = shelves.get(shelfIndex);
            if (tempShelf.takeResource(resource)) {
                shelves.set(shelfIndex, tempShelf);
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    /**
     * Method swapResource tries to swap the position fo two resources from two different shelves
     * @return true is the operation was successful
     */
    public boolean swapResource(int shelfIndexStart, int shelfIndexEnd, Resource firstResource, Resource secondResource) {
        if (shelves.size() > shelfIndexStart && shelves.size() > shelfIndexEnd) {

            if (shelves.get(shelfIndexStart).takeResource(firstResource)){
                if(shelves.get(shelfIndexEnd).takeResource(secondResource)){
                    shelves.get(shelfIndexStart).placeResource(secondResource);
                    shelves.get(shelfIndexEnd).placeResource(firstResource);
                    return true;
                }
                else{
                    shelves.get(shelfIndexStart).placeResource(firstResource);
                }
            }
        }
        return false;
    }
}
