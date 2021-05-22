package it.polimi.ingsw.constants;

import java.util.Map;

/**
 * GameConstants contains all constant values of the Game class and Market class
 */
public final class GameConstants {
    /*Game constant values*/

    /* Initial Bonus
    +--------+----------------------------+--------------+
    | Player | Resources of your choosing | Faith Points |
    +========+============================+==============+
    |    1   |              0             |       0      |
    +--------+----------------------------+--------------+
    |    2   |              1             |       0      |
    +--------+----------------------------+--------------+
    |    3   |              1             |       1      |
    +--------+----------------------------+--------------+
    |    4   |              2             |       1      |
    +--------+----------------------------+--------------+
     */
    public static final Map<Integer, Integer> BONUS_RESOURCES = Map.of(
            1,0,
            2,1,
            3,1,
            4,2
    );
    public static final Map<Integer, Integer> BONUS_FAITH_POINTS = Map.of(
            1,0,
            2,0,
            3,1,
            4,1
    );

    //Amount of DevelopmentCard(s) in the whole game
    public static final int NUM_DEVELOPMENT_CARDS = 48;

    //Amount of LeaderCard(s) in the whole game
    public static final int NUM_LEADER_CARDS = 16;

    //Number of Tokens
    public static final int NUM_TOKENS = 7;

    //Filepath to the list of DevelopmentCard to generate
    public static final String developmentCardsJson = "src/main/resources/json/development_card.json";

    //Filepath to the lists of LeaderCard to generate
    public static final String[] leaderCardsJson = {
            "src/main/resources/json/discount_leadercard.json",
            "src/main/resources/json/extrashelf_leadercard.json",
            "src/main/resources/json/production_leadercard.json",
            "src/main/resources/json/whitemarble_leadercard.json"
    };


    /*Market constant values*/

    //Number of white marble
    public static final int NUM_WHITE_MARBLES = 4;

    //Number of total marbles for each MarbleColor
    public static final int NUM_MARBLES = 2;

    //Number of red marbles
    public static final int NUM_RED_MARBLES = 1;


    /*Listeners property names*/
    //GameListener
    public static final String END_TURN = "endTurn";
    public static final String TOKEN_DRAWN = "tokenDrawn";

    //MarketListener
    public static final String MARKET_CHANGE = "marketChange";
    public static final String SLIDE_MARBLE = "slideMarble";

}
