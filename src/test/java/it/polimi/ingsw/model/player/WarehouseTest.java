package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * WarehouseTest tests Warehouse class.
 *
 * @author Andrea Nocito
 */
public class WarehouseTest {
    Warehouse warehouse;
    ResourceMap resourceMapA;
    ResourceMap resourceMapB;
    ResourceMap resourceMapC;

    /**
     * Method setUp initializes the warehouse and adds some resources.
     */
    @Before
    public void setUp(){
        warehouse = new Warehouse();

        resourceMapA = new ResourceMap();
        resourceMapA.modifyResource(Resource.STONE, 1);

        resourceMapB = new ResourceMap();
        resourceMapB.modifyResource(Resource.SERVANT, 1);
        resourceMapB.modifyResource(Resource.COIN,1);

        resourceMapC = new ResourceMap();
        resourceMapC.modifyResource(Resource.SHIELD, 3);

        warehouse.addResourcesToShelf(1,resourceMapA);
        warehouse.addResourcesToShelf(2,resourceMapB);
        warehouse.addResourcesToShelf(3,resourceMapC);
    }

    /**
     * Method tearDown sets warehouse and the three resource map null after each test.
     */
    @After
    public void tearDown(){
        warehouse = null;
        resourceMapA = null;
        resourceMapB = null;
        resourceMapC = null;
    }

    /**
     * Method getResourcesFromShelfTest tests the method getResourcesFromShelf by checking the correctness of the resources amount.
     */
    @Test
    public void getResourcesFromShelfTest() {
        assertEquals(resourceMapA.getResources(),warehouse.getResourcesFromShelf(1).getResources());
        assertEquals(resourceMapB.getResources(),warehouse.getResourcesFromShelf(2).getResources());
        assertEquals(resourceMapC.getResources(),warehouse.getResourcesFromShelf(3).getResources());
    }

    /**
     * Method getShelfIndexTest tests the correctness of the first index associated a shelf.
     */
    @Test
    public void getShelfIndexTest() {
        assertEquals(0, warehouse.getShelfIndex(1));
        assertEquals(1,warehouse.getShelfIndex(2));
        assertEquals(3,warehouse. getShelfIndex(3));
        assertEquals(6, warehouse.getShelfIndex(4));
        assertEquals(10, warehouse.getShelfIndex(5));
    }

    /**
     * Method removeResourcesFromShelfTest tests the removal of resources from the selected shelf.
     */
    @Test
    public void removeResourcesFromShelfTest(){
        ResourceMap resourceMap = new ResourceMap();
        warehouse.flushShelves();
        assertEquals((int) warehouse.getResourcesFromWarehouse().getAmount(), 0);
        resourceMap.modifyResource(Resource.SERVANT, 2);
        warehouse.addResourcesToShelf(2, resourceMap);
        assertEquals((int) warehouse.getResourcesFromWarehouse().getAmount(), 2);
        warehouse.removeResourcesFromShelf(2);
        assertEquals((int) warehouse.getResourcesFromWarehouse().getAmount(), 0);
    }

    /**
     * Method addExtraShelfResourceTest tests the addition of a resource to the extra shelf.
     */
    @Test
    public void addExtraShelfResourceTest(){
        assertEquals(warehouse.getExtraShelfResources().size(), 0);
        warehouse.addExtraShelfResource(Resource.SHIELD);
        assertEquals(warehouse.getExtraShelfResources().size(), 1);
        assertTrue(warehouse.getExtraShelfResources().contains(Resource.SHIELD));
    }
}