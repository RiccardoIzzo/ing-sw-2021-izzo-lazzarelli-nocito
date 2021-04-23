package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DashboardTest {
    Dashboard dashboard;

    /**
     * Method initialization create an instance of Dashboard
     */
    @Before
    public void initialization() {
        dashboard = new Dashboard(true);
    }

    /**
     * Method testAddShelf tests addPlayer and removePlayer methods, checks the number of players.
     */
    @Test
    public void testAddShelf() {
        final int numberOfShelfAdded = 3;
        for(int i=1; i<numberOfShelfAdded+1; i++) {
            dashboard.addShelf(new Shelf(i));
        }
        Warehouse warehouse = dashboard.getWarehouse();
        assertEquals(warehouse.getNumberShelf() + numberOfShelfAdded, warehouse.getShelvesSize());
    }
    /**
     * Method testAddResource adds one resource in the strongbox and checks if it is there
     */
    @Test
    public void testAddResource() {
        dashboard.addResource(Resource.COIN);
        ResourceMap strongBox = dashboard.getStrongBox();
        int resValue = strongBox.getResource(Resource.COIN);
        assertEquals( resValue,1);
    }

    /**
     * Method testRemoveResource  adds one resource in the strongbox, then removes it and checks that strongbox is empty
     */
    @Test
    public void testRemoveResource() {
        dashboard.addResource(Resource.COIN);
        ResourceMap strongBox = dashboard.getStrongBox();
        int resValue = strongBox.getResource(Resource.COIN);
        assertEquals( resValue,1);
        assertTrue(dashboard.removeResource(Resource.COIN));
        strongBox = dashboard.getStrongBox();
        resValue = strongBox.getResource(Resource.COIN);
        assertEquals( resValue,0);
    }
}