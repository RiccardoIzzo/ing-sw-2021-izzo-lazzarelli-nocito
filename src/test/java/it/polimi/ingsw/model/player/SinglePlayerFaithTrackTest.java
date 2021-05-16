package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.Before;

import static it.polimi.ingsw.constants.PlayerConstants.END_TILE;
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
        for (int i=0; i<END_TILE; i++) {
            singlePlayerFaithTrack.moveBlack();
        }
        assertEquals(singlePlayerFaithTrack.getBlackFaithMarker(), END_TILE);

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
}