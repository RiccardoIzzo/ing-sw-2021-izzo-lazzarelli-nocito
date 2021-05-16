package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.constants.PlayerConstants.NUMBER_SHELF;
import static org.junit.Assert.*;

import java.util.*;

/**
 * WarehouseTest tests Warehouse class.
 *
 * @author Andrea Nocito
 */
public class WarehouseTest {
    Warehouse warehouse;
    /**
     * Method initialization creates an instance of Shelf
     */
    @Before
    public void initialization() {
        warehouse = new Warehouse();
    }

    /**
     * Method testAddShelf adds one shelf to the warehoue and checks that the number of shelves has actually been incremented.
     */
    @Test
    public void testAddShelf() {
        int startingSize = warehouse.getShelvesSize();
        warehouse.addShelf(new Shelf(1));
        assertEquals(startingSize+1, warehouse.getShelvesSize());
    }

    /**
     * Method testAddResource adds one shelf and then one resource into that shelf. Then checks if its there.
     */
    @Test
    public void testAddResource() {
        Set<Resource> resourcesAllowedTest = new HashSet<>();
        resourcesAllowedTest.add(Resource.COIN);
        int startingSize = warehouse.getShelvesSize();
        warehouse.addShelf(new Shelf(1, resourcesAllowedTest));
        warehouse.addResource(Resource.COIN, startingSize);
        assertTrue(warehouse.getShelf(startingSize).isPresent());
        Shelf addedShelf = warehouse.getShelf(startingSize).get();
        assertTrue(addedShelf.takeResource(Resource.COIN));
    }

    /**
     * Method testGetShelvesSize checks that the number of shelves of the just initialized warehouse is the same as the numberShelf
     */
    @Test
    public void testGetShelvesSize() {
        assertEquals(NUMBER_SHELF, warehouse.getShelvesSize());
    }

    /**
     * Method testGetNumberShelf checks that the numberShelf of the just initialized warehouse is the same as the number of shelves
     */
    @Test
    public void testGetNumberShelf() {
        assertEquals(NUMBER_SHELF, warehouse.getShelvesSize());
    }

    /**
     * Method testAddResourcesIntoTemporaryShelf add one COIN into a temporary shelf and then removes it to check that is been allocated.
     */
    @Test
    public void testAddResourcesIntoTemporaryShelf() {
        int startingSize = warehouse.getShelvesSize();
        ResourceMap resourcesTest = new ResourceMap();
        resourcesTest.modifyResource(Resource.COIN, +1);
        warehouse.addResourcesIntoTemporaryShelf(resourcesTest);

        assertTrue(warehouse.getShelf(startingSize).isPresent());
        Shelf temporaryShelf = warehouse.getShelf(startingSize).get();
        assertTrue(temporaryShelf.takeResource(Resource.COIN));
        assertFalse(temporaryShelf.takeResource(Resource.COIN));
    }

    /**
     * Method testRemoveResourcesFromTemporaryShelf
     */
    @Test
    public void testRemoveResourcesFromTemporaryShelf() {
        int startingSize = warehouse.getShelvesSize();
        ResourceMap resourcesTest = new ResourceMap();
        resourcesTest.modifyResource(Resource.COIN, +1);
        warehouse.addResourcesIntoTemporaryShelf(resourcesTest);
        warehouse.removeResourcesFromTemporaryShelf();
        assertEquals(startingSize, warehouse.getShelvesSize());
    }

    /**
     * Method testRemoveResource
     */
    @Test
    public void testRemoveResource() {
        Set<Resource> resourcesAllowedTest = new HashSet<>();
        resourcesAllowedTest.add(Resource.COIN);
        int startingSize = warehouse.getShelvesSize();

        warehouse.addShelf(new Shelf(1, resourcesAllowedTest));
        warehouse.addResource(Resource.COIN, startingSize);
        assertTrue(warehouse.removeResource(startingSize, Resource.COIN));
        assertFalse(warehouse.removeResource(startingSize, Resource.COIN));
    }

    /**
     * Method testSwapResource adds two shelvs, each with one resource of a different type, swaps the resources, and checks that everything worked.
     */
    @Test
    public void testSwapResource() {
        Set<Resource> resourcesAllowedTest = new HashSet<>();
        resourcesAllowedTest.add(Resource.COIN);
        resourcesAllowedTest.add(Resource.STONE);
        int startingSize = warehouse.getShelvesSize();

        warehouse.addShelf(new Shelf(1, resourcesAllowedTest));
        warehouse.addShelf(new Shelf(1, resourcesAllowedTest));

        warehouse.addResource(Resource.COIN, startingSize);
        warehouse.addResource(Resource.STONE, startingSize+1);

        warehouse.swapResource(startingSize, startingSize+1, Resource.COIN, Resource.STONE);

        assertTrue(warehouse.getShelf(startingSize).isPresent());
        assertTrue(warehouse.getShelf(startingSize+1).isPresent());
        Shelf firstShelf = warehouse.getShelf(startingSize).get();
        Shelf secondShelf = warehouse.getShelf(startingSize+1).get();

        assertTrue(firstShelf.takeResource(Resource.STONE));
        assertTrue(secondShelf.takeResource(Resource.COIN));


    }
}