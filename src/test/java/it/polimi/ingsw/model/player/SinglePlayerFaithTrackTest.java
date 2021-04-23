package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.*;

/**
 * SinglePlayerFaithTrackTest tests SinglePlayerFaithTrack class.
 *
 * @author Andrea Nocito
 */


public class SinglePlayerFaithTrackTest {
    SinglePlayerFaithTrack singlePlayerFaithTrack;

    /**
     * Method initialization creates an instance of SinglePlayerFaithTrack
     */
    @Before
    public void initialization() {
        singlePlayerFaithTrack = new SinglePlayerFaithTrack();
    }
    /**
     * Method testGetBlackFaithMarker check that getBlackFaithMarker returns the correct position of blackFaithMarker
     */
    @Test
    public void testGetBlackFaithMarker() {
        assertEquals(singlePlayerFaithTrack.getBlackFaithMarker(), 0);
        for (int i=0; i<singlePlayerFaithTrack.getEnd(); i++) {
            singlePlayerFaithTrack.moveBlack();
        }
        assertEquals(singlePlayerFaithTrack.getBlackFaithMarker(), singlePlayerFaithTrack.getEnd());

    }

    /**
     * Method testMoveBlack checks that moveBlack method actually increases the blackFaithMarker
     */
    @Test
    public void testMoveBlack() {
        assertEquals(singlePlayerFaithTrack.getBlackFaithMarker(), 0);
        singlePlayerFaithTrack.moveBlack();
        singlePlayerFaithTrack.moveBlack();
        assertEquals(singlePlayerFaithTrack.getBlackFaithMarker(), 2);
    }

    /**
     * Method testCheckBlack checks that checkBlack returns true when the blackFaithMarker is on the last tile
    */
    @Test
    public void testCheckBlack() {
        assertFalse(singlePlayerFaithTrack.checkBlack());
        for (int i=0; i<singlePlayerFaithTrack.getEnd(); i++) {
            singlePlayerFaithTrack.moveBlack();
        }
        assertTrue(singlePlayerFaithTrack.checkBlack());
    }
}