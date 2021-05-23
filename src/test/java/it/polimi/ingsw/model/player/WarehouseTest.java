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

    @Before
    public void setUp() throws Exception {
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

    @After
    public void tearDown() throws Exception {
        warehouse = null;
        resourceMapA = null;
        resourceMapB = null;
        resourceMapC = null;
    }

    @Test
    public void getResourcesFromShelf() {
        assertEquals(resourceMapA.getResources(),warehouse.getResourcesFromShelf(1).getResources());
        assertEquals(resourceMapB.getResources(),warehouse.getResourcesFromShelf(2).getResources());
        assertEquals(resourceMapC.getResources(),warehouse.getResourcesFromShelf(3).getResources());
    }

    @Test
    public void getShelfIndex() {
        assertEquals(0, warehouse.getShelfIndex(1));
        assertEquals(1,warehouse.getShelfIndex(2));
        assertEquals(3,warehouse. getShelfIndex(3));
        assertEquals(6, warehouse.getShelfIndex(4));
        assertEquals(10, warehouse.getShelfIndex(5));
    }
}