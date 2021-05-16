package it.polimi.ingsw.constants;

/**
 * PlayerConstants contains all constant values of the Player class and related classes (Dashboard, Warehouse, Shelf, FaithTrack)
 */
public final class PlayerConstants {
    /*Player constant values*/
    public static final int NUMBER_OF_DEVELOPMENTS = 3;
    public static final int NUMBER_OF_LEADERS = 3;

    /*Warehouse constant values*/
    public static final int NUMBER_SHELF = 3;


    /*FaithTrack constant values*/

    // WINNING_TILES : position and value tiles that give points
    public static final int[] WINNING_TILES = {3,6,9,12,15,18,21,24};
    public static final int[] WINNING_VALUES = {1,2,4,6,9,12,16,20};

    // TILE_POS : position of tiles in the game.
    public static final int[] TILE_POS = {8,16,24};

    // POINTS_FOR_TILE : position of tiles in the game.
    public static final int[] POINTS_FOR_TILE = {2,3,4};

    // INITIAL OFFSET: offset from the first tile which defines the "point zone"
    public static final int INITIAL_OFFSET = 4;

    // END: last tile of the game
    public static final int END_TILE = 24;



    /*Listeners property names*/

    //PlayerListener
    public static final String LEADER_ACTIVATION = "leaderActivation";
    public static final String SET_LEADERS = "setLeaders";
    public static final String SELECT_LEADERS = "selectLeaders";
    public static final String DISCARD_LEADER = "discardLeader";

    //DashboardListener
    public static final String STRONGBOX_CHANGE = "strongboxChange";

    //WarehouseListener
    public static final String TEMPORARY_SHELF = "TemporaryShelf";

    //FaithTrackListener
    public static final String FAITH_MARKER_POSITION = "faithMarkerPosition";
    public static final String BLACK_MARKER_POSITION = "blackMarkerPosition";

    //ShelfListener
    public static final String SHELF_CHANGE = "shelfChange";

    //GridListener
    public static final String GRID_CHANGE = "gridChange";
}

