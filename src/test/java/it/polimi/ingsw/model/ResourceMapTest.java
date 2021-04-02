package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ResourceMapTest tests ResourceMap class.
 *
 * @author Andrea Nocito
 */
public class ResourceMapTest {
    ResourceMap myResourceMap;
    /**
     * Method initialization create an instance of ResourceMap and generates the market tray.
     */
    @BeforeEach
    public void initialization(){
        myResourceMap = new ResourceMap();
    }

    /**
     * Method generateTrayTest checks that ResourceMap contained a value for each and every Resources
     */
    @Test
    public void getResourcesTest() {
        assertEquals(myResourceMap.getResources().size(), Resource.values().length);
    }

}
