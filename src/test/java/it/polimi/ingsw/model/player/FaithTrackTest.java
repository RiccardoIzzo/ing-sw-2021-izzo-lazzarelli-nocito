package it.polimi.ingsw.model.player;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * FaithTrackTest tests FaithTrack class.
 *
 * @author Andrea Nocito
 */
public class FaithTrackTest{
    private FaithTrack faithTrack;

    /**
     * Method initialization initializes a new FaithTrack and sets the array tilesUncovered
     */
    @Before
    public void initialization() {
        faithTrack = new FaithTrack();
        faithTrack.setTilesUncovered(new Boolean[]{false, false, false});
    }

    /**
     * Method testMoveForward moves posFaithMarker forward and checks the correctness of its new position.
     */
    @Test
    public void testMoveForward() {
        faithTrack.moveForward();
        assertEquals(faithTrack.getPlayerPosition(), 1);
    }



    /*
     * Method testIsInVaticanSpace checks that the method isInVaticanSpace assigns the correct points to the player when in vatican space.
     */
    @Test
    public void testIsInVaticanSpace() {
        assertEquals(faithTrack.getPointsForTiles(), 0);
        for(int i = 0; i < 9; i++) {
            faithTrack.moveForward();
        }
        faithTrack.isInVaticanSpace();
        assertEquals(faithTrack.getPointsForTiles(), 2);

        for(int i = 0; i < 8; i++) {
            faithTrack.moveForward();
        }
        faithTrack.isInVaticanSpace();
        assertEquals(faithTrack.getPointsForTiles(), 5);

        for(int i = 0; i < 7; i++) {
            faithTrack.moveForward();
        }
        faithTrack.isInVaticanSpace();
        assertEquals(faithTrack.getPointsForTiles(), 9);
    }

    /**
     * Method testPopeTilePass checks that the method PopeTilePass uncover the tile when the player reaches a Pope space.
     */
    @Test
    public void testPopeTilePass() {
        assertFalse(faithTrack.getTilesUncovered()[0]);
        for(int i = 0; i < 9; i++) {
            faithTrack.moveForward();
        }
        assertTrue(faithTrack.getTilesUncovered()[0]);
    }

    /**
     * Method checks that the method getPosVictoryPoints returns the correct numbers of points based on the player position.
     */
    @Test
    public void testGetPosVictoryPoints() {
        assertEquals(faithTrack.getPosVictoryPoints(), 0);
        faithTrack.moveForward();
        faithTrack.moveForward();
        faithTrack.moveForward();
        assertEquals(faithTrack.getPosVictoryPoints(), 1);
        for (int i = 0; i < 8; i++) {
            faithTrack.moveForward();
        }
        assertEquals(faithTrack.getPosVictoryPoints(), 4);
    }
}