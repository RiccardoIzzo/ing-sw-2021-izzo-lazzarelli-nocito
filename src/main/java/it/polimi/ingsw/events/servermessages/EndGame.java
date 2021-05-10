package it.polimi.ingsw.events.servermessages;

import java.util.Map;

/**
 * EndGame message is used to notify the player that there is a winner and that consequently the game is over.
 */
public class EndGame implements ServerMessage{
    private final Map<String, Integer> playerPoints;
    private final String winner;

    /**
     * Constructor EndGame creates a new EndGame instance.
     * @param playerPoints map that associates a player nickname with his victory points achieved during the game.
     * @param winner nickname of the winner.
     */
    public EndGame(Map<String, Integer> playerPoints, String winner){
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
