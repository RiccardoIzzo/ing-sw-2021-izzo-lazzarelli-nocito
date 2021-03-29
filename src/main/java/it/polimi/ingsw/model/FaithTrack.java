package it.polimi.ingsw.model;

import java.util.Arrays;

/**
 * Shelf Class represents a player's faith track
 *
 * @author Andrea Nocito
 */
public class FaithTrack {
    private int posFaithMarker;
    private Boolean[] popesFavorTiles;

    // TILE_POS : position of tiles in the game.
    private static final int[] TILE_POS = {8,16,24};
    // INITIAL OFFSET: offset from the first tile which defines the "point zone"
    private static final int INITIAL_OFFSET = 4;
    // END: last tile of the game
    static final int END = 24;

    public FaithTrack() {
        posFaithMarker = 0;
        popesFavorTiles = new Boolean[3];
        Arrays.fill(popesFavorTiles, false);

    }
    public void moveForward() {
        posFaithMarker++;
        for(int i = 0; i < TILE_POS.length; i++) {
            if ( posFaithMarker == TILE_POS[i] ) {
                popesFavorTiles[i] = true;

            }
        }
    }
    public void isInVaticanSpace(Integer space) {

    }
    public void popeTilePass() {
    }
    public int getPlayerPos() {
        return posFaithMarker;
    }

}