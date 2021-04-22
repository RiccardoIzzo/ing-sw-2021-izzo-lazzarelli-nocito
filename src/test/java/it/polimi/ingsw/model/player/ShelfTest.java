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

    @Test
    public void testGetResourceAllowed() {
        assertEquals(shelf.getResourceAllowed(), resourcesAllowedTest);
    }

    @Test
    public void testTakeResource() {
        assertTrue(shelf.placeResource(Resource.COIN));
        assertTrue(shelf.takeResource(Resource.COIN));
        assertFalse(shelf.takeResource(Resource.SERVANT));
        assertFalse(shelf.takeResource(Resource.SHIELD));
        assertFalse(shelf.takeResource(Resource.STONE));

    }

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

    @Test
    public void testPlaceResource() {
        assertTrue(shelf.placeResource(Resource.COIN));
        assertFalse(shelf.placeResource(Resource.SERVANT));
        assertFalse(shelf.placeResource(Resource.SHIELD));
        assertFalse(shelf.placeResource(Resource.STONE));
    }
}