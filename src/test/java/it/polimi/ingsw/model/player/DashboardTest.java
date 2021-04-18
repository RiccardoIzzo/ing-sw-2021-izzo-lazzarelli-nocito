package it.polimi.ingsw.model.player;

import org.junit.Assert;
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
        dashboard = new Dashboard();
    }

    /**
     * Method testAddShelf tests addPlayer and removePlayer methods, checks the number of players.
     */
    @Test
    public void addShelfTest() {
        final int numberOfShelfAdded = 3;
        for(int i=1; i<numberOfShelfAdded+1; i++) {
            dashboard.addShelf(new Shelf(i));
        }
        Warehouse warehouse = dashboard.getWarehouse();
        Assert.assertEquals(warehouse.getNumberShelf() + numberOfShelfAdded, warehouse.getShelvesSize());
    }

}