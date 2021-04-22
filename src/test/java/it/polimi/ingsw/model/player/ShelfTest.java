package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * ShelfTest tests Shelf class.
 *
 * @author Andrea Nocito
 */

public class ShelfTest {
    Shelf shelf;
    Set<Resource> resourcesAllowedTest;

    /**
     * Method initialization creates an instance of Shelf
     */
    @Before
    public void initialization() {
        resourcesAllowedTest = new HashSet<>();
        resourcesAllowedTest.add(Resource.COIN);
        shelf = new Shelf(2, resourcesAllowedTest);
    }

    /**
     * Method testGetResourceAllowed checks that the resources allowed inside the shelf are the ones set in at initializations
     */
    @Test
    public void testGetResourceAllowed() {
        assertEquals(shelf.getResourceAllowed(), resourcesAllowedTest);
    }

    /**
     * Method testTakeResource places one COIN resource and tries to take one resource for each type
     * Checks that only the method with COIN as a parameter returns a positive value.
     */
    @Test
    public void testTakeResource() {
        assertTrue(shelf.placeResource(Resource.COIN));
        assertTrue(shelf.takeResource(Resource.COIN));
        assertFalse(shelf.takeResource(Resource.SERVANT));
        assertFalse(shelf.takeResource(Resource.SHIELD));
        assertFalse(shelf.takeResource(Resource.STONE));

    }
    /**
     * Method testPlaceResources checks the placeResources method:
     * First it tries add 3 resources to the shelf, and checks that the operation return a negative response, since the capacity of the shelf is set to 2.
     * After it tries to add 2 resource to the shelf, successfully, removes them one by one and check that is not possible to remove another one from the empty shelf.
     */
    @Test
    public void testPlaceResources() {
        ResourceMap resourcesTest = new ResourceMap();
        resourcesTest.modifyResource(Resource.COIN, +3);
        assertFalse(shelf.placeResources(resourcesTest));
        resourcesTest.modifyResource(Resource.COIN, -1);
        assertTrue(shelf.placeResources(resourcesTest));
        assertTrue(shelf.takeResource(Resource.COIN));
        assertTrue(shelf.takeResource(Resource.COIN));
        assertFalse(shelf.takeResource(Resource.COIN));
    }

    /**
     * Method testPlaceResource tries to place one of each type of Resources, and checks that only COIN gets added to shelf, since it is the only Resource type allowed.
     */
    @Test
    public void testPlaceResource() {
        assertTrue(shelf.placeResource(Resource.COIN));
        assertFalse(shelf.placeResource(Resource.SERVANT));
        assertFalse(shelf.placeResource(Resource.SHIELD));
        assertFalse(shelf.placeResource(Resource.STONE));
    }
}