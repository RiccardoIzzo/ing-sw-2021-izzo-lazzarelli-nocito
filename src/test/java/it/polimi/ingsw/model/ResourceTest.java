package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * ResourceTest tests Resource class.
 *
 * @author Andrea Nocito
 * */
public class ResourceTest {

    /**
     * Method toStringTest gets the output of toString and checks that the result contains the desired symbol
     * */
    @Test
    public void testToString() {
        assertTrue(Resource.STONE.toString().contains("♠"));
        assertTrue(Resource.SERVANT.toString().contains("♥"));
        assertTrue(Resource.SHIELD.toString().contains("♣"));
        assertTrue(Resource.COIN.toString().contains("♦"));
    }
}