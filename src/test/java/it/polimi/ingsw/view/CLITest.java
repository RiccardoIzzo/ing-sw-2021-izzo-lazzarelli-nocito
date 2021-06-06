package it.polimi.ingsw.view;

import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.model.player.Warehouse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    Boolean[] popesTiles;

    Map<String, Integer> gameStats;

    ArrayList<Integer> activeDevelopments;

    Map<Integer, Boolean> leaders;

    @Before
    public void setUp() throws Exception {
        /*
        showWarehouse setUp
         */
        warehouse = new Warehouse();
        extraShelfResources = new ArrayList<>();

        extraShelfResources.add(Resource.COIN);
        extraShelfResources.add(Resource.SHIELD);

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
        showMarket setUp
         */
        marketTray = new ArrayList<>();
        slideMarble = MarbleColor.RED;
        for (int i = 0; i < 12; i++){
            marketTray.add(MarbleColor.values()[49*(i+1) % 6]);
        }

        /*
        showFaithTrack setUp
         */
        popesTiles = new Boolean[] {true, false, false};

        /*
        showStats setUp
        */
        gameStats = new HashMap<>();
        gameStats.put("Alpha", 50);
        gameStats.put("Bravo", 55);
        gameStats.put("Charlie", 40);
        gameStats.put("Delta", 23);

        /*
        showActiveDevelopments setUp
        */
        activeDevelopments = new ArrayList<>(Collections.nCopies(3,null));
        activeDevelopments.set(0,101);
        activeDevelopments.set(2,109);

        /*
        showLeaders setUp
        */
        leaders = new HashMap<>();
        leaders.put(201, false);
        leaders.put(216, true);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void show() {
        //CLI.showWarehouse(warehouse.getShelves(), extraShelfResources);
        //CLI.showMarket(marketTray, slideMarble);
        //CLI.showFaithTrack(1,3,popesTiles);
        //CLI.showStrongbox(resourceMapE);
        //CLI.showStats(gameStats);
        //CLI.showActiveDevelopments(activeDevelopments);
        //CLI.showLeaders(leaders);
    }
}