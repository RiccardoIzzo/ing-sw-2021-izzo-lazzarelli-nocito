package it.polimi.ingsw.model.player;

import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.constants.PlayerConstants.END_TILE;
import static org.junit.Assert.*;

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
        for (int i=0; i<3; i++) {
            singlePlayerFaithTrack.moveBlackFaithMarker();
        }
        assertEquals(singlePlayerFaithTrack.getBlackFaithMarker(), 3);
    }

    /**
     * Method testMoveBlack checks that moveBlackFaithMarker method actually increases the blackFaithMarker
     */
    @Test
    public void testMoveBlack() {
        assertEquals(singlePlayerFaithTrack.getBlackFaithMarker(), 0);
        singlePlayerFaithTrack.moveBlackFaithMarker();
        singlePlayerFaithTrack.moveBlackFaithMarker();
        assertEquals(singlePlayerFaithTrack.getBlackFaithMarker(), 2);
    }
}