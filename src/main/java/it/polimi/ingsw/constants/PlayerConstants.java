package it.polimi.ingsw.constants;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * PlayerConstants contains all constant values of the Player class and related classes (Dashboard, Warehouse, Shelf, FaithTrack)
 */
public final class PlayerConstants {
    /*Warehouse constant values*/
    public static final int FIRST_SHELF = 1; //Shelf number of the first shelf
    public static final int LAST_SHELF = 4; //Shelf number of the last shelf
    public static final int TEMPORARY_SHELF = 5; //Shelf number of the temporary shelf


    /*FaithTrack constant values*/

    // WINNING_TILES : position and value tiles that give points
    public static final int[] WINNING_TILES = {3,6,9,12,15,18,21,24};
    public static final int[] WINNING_VALUES = {1,2,4,6,9,12,16,20};

    // TILE_POS : position of tiles in the game.
    public static final ArrayList<Integer> TILE_POS = new ArrayList<>(Arrays.asList(8,16,24));

    // POINTS_FOR_TILE : position of tiles in the game.
    public static final int[] POINTS_FOR_TILE = {2,3,4};

    // END: last tile of the game
    public static final int END_TILE = 24;



    /*Listeners property names*/

    //PlayerListener
    public static final String LEADER_ACTIVATION = "leaderActivation";
    public static final String SET_LEADERS = "setLeaders";
    public static final String DISCARD_LEADER = "discardLeader";
    public static final String DEVELOPMENTS_CHANGE = "developmentsChange";
    public static final String ACTIVE_DEVELOPMENTS_CHANGE = "productionsChange";

    //DashboardListener
    public static final String STRONGBOX_CHANGE = "strongboxChange";

    //WarehouseListener
    public static final String TEMPORARY_SHELF_CHANGE = "TemporaryShelf";

    //FaithTrackListener
    public static final String FAITH_MARKER_POSITION = "faithMarkerPosition";
    public static final String BLACK_MARKER_POSITION = "blackMarkerPosition";
    public static final String POPES_TILES_CHANGE = "popesTilesChange";
    public static final String TILES_UNCOVERED_CHANGE = "tilesUncoveredChange";

    //ShelfListener
    public static final String SHELF_CHANGE = "shelfChange";
    public static final String EXTRA_SHELF_CHANGE = "extraShelfChange";

    //GridListener
    public static final String GRID_CHANGE = "gridChange";
}

