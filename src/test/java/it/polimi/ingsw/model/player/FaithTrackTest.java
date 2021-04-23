package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * FaithTrackTest tests FaithTrack class.
 *
 * @author Andrea Nocito
 */

public class FaithTrackTest{
    FaithTrack faithTrack;
    /**
     * Method
     */
    @Before
    public void initialization() {
        faithTrack = new FaithTrack();
    }

    /**
     * Method testMoveForward moves posFaithMarker forward and checks its new position to be correct
     */
    @Test
    public void testMoveForward() {
        faithTrack.moveForward();
        assertEquals(faithTrack.getPlayerPos(), 1);
    }

    /**
     * Method testSetTilesUncovered checks that is possible to set to true the available tilesUncovered elements of the array
     */
    @Test
    public void testSetTilesUncovered() {
        assertTrue(faithTrack.setTilesUncovered(0, true));
        assertTrue(faithTrack.setTilesUncovered(1, true));
        assertTrue(faithTrack.setTilesUncovered(2, true));
        assertFalse(faithTrack.setTilesUncovered(3, true));
    }

    /**
     * Method testIsInVaticanSpace checks that the method isInVaticanSpace assigns the correct points to the player if it is in the correct area.
     */
    @Test
    public void testIsInVaticanSpace() {
        assertEquals(faithTrack.getPointsForTiles(), 0);
        for(int i=0; i<5; i++) {
            faithTrack.moveForward();
        }
        faithTrack.isInVaticanSpace(0);
        assertEquals(faithTrack.getPointsForTiles(), 2);
        for(int i= faithTrack.getPlayerPos()-1; i<14; i++) {
            faithTrack.moveForward();
        }
        faithTrack.isInVaticanSpace(1);
        assertEquals(faithTrack.getPointsForTiles(), 5);
        for(int i= faithTrack.getPlayerPos()-1; i<23; i++) {
            faithTrack.moveForward();
        }
        faithTrack.isInVaticanSpace(2);
        assertEquals(faithTrack.getPointsForTiles(), 9);
    }

    /**
     * Method testPopeTilePass checks that PopeTilePass sets the correct points and tilesUncovered value when the player gets to a pope tile.
     */
    @Test
    public void testPopeTilePass() {
        assertFalse(faithTrack.getTilesUncovered(0));
        for(int i=0; i<9; i++) {
            faithTrack.moveForward();
        }
        assertEquals(faithTrack.getPointsForTiles(), 2);
        assertTrue(faithTrack.getTilesUncovered(0));
    }

    /**
     * Method testGetPointsForTiles checks that the method returns the correct sum of points that the player unlocks through pope tiles.
     */
    @Test
    public void testGetPointsForTiles() {
        assertEquals(faithTrack.getPointsForTiles(), 0);
        for (int i=0; i<faithTrack.getEnd(); i++) {
            faithTrack.moveForward();
        }
        assertEquals(faithTrack.getPointsForTiles(), 9);
    }

    /**
     * Method checks that the method returns the correct numbers of points that the player unlocks thourgh moving on the faith track and pope tiles.
     */
    @Test
    public void testGetPosVictoryPoints() {
        assertEquals(faithTrack.getPosVictoryPoints(), 0);
        faithTrack.moveForward();
        faithTrack.moveForward();
        faithTrack.moveForward();
        assertEquals(faithTrack.getPosVictoryPoints(), 1);
        for (int i=faithTrack.getPlayerPos()-1; i<faithTrack.getEnd(); i++) {
            faithTrack.moveForward();
        }
        assertEquals(faithTrack.getPosVictoryPoints(), 79);
    }

    /**
     * Method testGetPlayerPos checks that the method returns the correct position of the player inside the faith track, starting from 0 and then moving forward two times.
     */
    @Test
    public void testGetPlayerPos() {
        assertEquals(faithTrack.getPlayerPos(), 0);
        faithTrack.moveForward();
        faithTrack.moveForward();
        assertEquals(faithTrack.getPlayerPos(), 2);
    }
}