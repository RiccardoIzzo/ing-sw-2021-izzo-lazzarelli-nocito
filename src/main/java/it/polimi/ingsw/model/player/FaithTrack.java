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
    // POINTS_FOR_TILE : position of tiles in the game.
    private static final int[] POINTS_FOR_TILE = {2,3,4};
    // INITIAL OFFSET: offset from the first tile which defines the "point zone"
    private static final int INITIAL_OFFSET = 4;
    // END: last tile of the game
    static final int END = 24;


    public FaithTrack() {
        posFaithMarker = 0;
        popesFavorTiles = new Boolean[3];
        Arrays.fill(popesFavorTiles, false);

    }

    /**
     * Method moveForward moves the faith marker and calls popeTilePass
     */
    public void moveForward() {
        posFaithMarker++;
        popeTilePass();
    }

    /**
     * Method isInVaticanSpace checks if the position is eligible for the pope tile points
     */
    public void isInVaticanSpace(Integer space) {
        popesFavorTiles[space] = posFaithMarker <= TILE_POS[space] && posFaithMarker >= TILE_POS[space] - INITIAL_OFFSET - space;
    }

    /**
     * Method popeTilePass checks if the position is the same as a tile pass
     */
    public void popeTilePass() {
        for(int i = 0; i < TILE_POS.length; i++) {
            if ( posFaithMarker == TILE_POS[i] ) {
                popesFavorTiles[i] = true;
            }
        }
    }

    /**
     * Method getPointsForTiles
     * @return the number of points earned by the unlocked pope tiles
     */
    public int getPointsForTiles() {
        int counter = 0;
        for (int i = 0; i < popesFavorTiles.length; i++) {
            counter += popesFavorTiles[i] ? POINTS_FOR_TILE[i] : 0;
        }
        return counter;
    }

    /**
     * Method getPosVictoryPoints
     * @return the number of points earned by the player's position on the faith track and the unlocked pope tiles
     */
    public int getPosVictoryPoints() {
        int victoryPoints = 0;
        int i = 0;
        while (posFaithMarker < WINNING_TILES[i]) {
            victoryPoints += WINNING_VALUES[i];
            i++;
        }
        return victoryPoints+getPointsForTiles();
    }

    public int getPlayerPos() {
        return posFaithMarker;
    }

}