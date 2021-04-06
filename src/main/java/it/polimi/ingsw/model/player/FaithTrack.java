package it.polimi.ingsw.model.player;

import java.util.Arrays;

/**
 * FaithTrack Class represents a player's faith track
 *
 * @author Andrea Nocito
 */
public class FaithTrack {
    private int posFaithMarker;
    private final Boolean[] popesFavorTiles;

    // WINNING_TILES : position and value tiles that give points
    private static final int[] WINNING_TILES = {3,6,9,12,15,18,21,24};
    private static final int[] WINNING_VALUES = {1,2,4,6,9,12,16,20};
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
    public int getNumberOfActiveTiles() {
        int counter = 0;
        for (Boolean tile : popesFavorTiles) {
            counter += tile ? 1 : 0;
        }
        return counter;
    }
    public int getPosVictoryPoints() {
        int victoryPoints = 0;
        int i = 0;
        while (posFaithMarker < WINNING_TILES[i]) {
            victoryPoints += WINNING_VALUES[i];
            i++;
        }
        return victoryPoints;
    }
    public int getPlayerPos() {
        return posFaithMarker;
    }

}