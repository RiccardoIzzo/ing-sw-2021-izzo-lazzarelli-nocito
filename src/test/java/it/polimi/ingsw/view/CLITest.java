package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.model.player.Warehouse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CLITest {
    Warehouse warehouse;
    ResourceMap resourceMapA;
    ResourceMap resourceMapB;
    ResourceMap resourceMapC;
    ResourceMap resourceMapD;
    ResourceMap resourceMapE;

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

        resourceMapD = new ResourceMap();
        resourceMapD.addResources(resourceMapC);
        resourceMapD.modifyResource(Resource.SERVANT,1);

        resourceMapE = new ResourceMap();
        resourceMapE.modifyResource(Resource.SHIELD, 4);

        warehouse.addResourcesToShelf(1,resourceMapA);
        warehouse.addResourcesToShelf(2,resourceMapB);
        warehouse.addResourcesToShelf(3,resourceMapC);
        warehouse.addResourcesToShelf(4,resourceMapD);
        warehouse.addResourcesToShelf(5,resourceMapE);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void showWarehouse() {
        //CLI.showWarehouse(warehouse.getShelves());
    }
}