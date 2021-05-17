package it.polimi.ingsw.events.servermessages;

import java.util.Map;

public class GameStats {
    private final Map<String, Integer> playerPoints;
    private final String winner;

    /**
     * Constructor GameStats creates a new GameStats instance.
     * @param playerPoints map that associates a player nickname with his victory points achieved during the game.
     * @param winner nickname of the winner.
     */
    public GameStats(Map<String, Integer> playerPoints, String winner){
        this.playerPoints = playerPoints;
        this.winner = winner;
    }

    /**
     * Method getPlayerPoints returns the map playerPoints.
     * @return a map that associates a player with his victory points.
     */
    public Map<String, Integer> getPlayerPoints() {
        return playerPoints;
    }

    /**
     * Method getWinner returns the winner of the game.
     * @return nickname of the game winner.
     */
    public String getWinner() {
        return winner;
    }
}
