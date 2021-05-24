package it.polimi.ingsw.view;

import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.model.player.Warehouse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class CLITest {
    Warehouse warehouse;
    ArrayList<Resource> extraShelfResources;
    ResourceMap resourceMapA;
    ResourceMap resourceMapB;
    ResourceMap resourceMapC;
    ResourceMap resourceMapD;
    ResourceMap resourceMapE;

    ArrayList<MarbleColor> marketTray;
    MarbleColor slideMarble;

    @Before
    public void setUp() throws Exception {
        /*
        showWarehouse setUp
         */
        warehouse = new Warehouse();
        extraShelfResources = new ArrayList<>();

        extraShelfResources.add(Resource.COIN);

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
        resourceMapE.addResources(resourceMapB);
        resourceMapE.modifyResource(Resource.SHIELD, 2);

        warehouse.addResourcesToShelf(1,resourceMapA);
        warehouse.addResourcesToShelf(2,resourceMapB);
        warehouse.addResourcesToShelf(3,resourceMapC);
        warehouse.addResourcesToShelf(4,resourceMapD);
        warehouse.addResourcesToShelf(5,resourceMapE);

        /*
        showMarketSetup
         */
        marketTray = new ArrayList<>();
        slideMarble = MarbleColor.RED;
        for (int i = 0; i < 12; i++){
            marketTray.add(MarbleColor.values()[49*(i+1) % 6]);
        }

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void showWarehouse() {
        //CLI.showWarehouse(warehouse.getShelves(), extraShelfResources);
        CLI.showMarket(marketTray, slideMarble);
    }
}