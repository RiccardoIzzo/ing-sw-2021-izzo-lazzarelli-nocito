package it.polimi.ingsw.events.servermessages;

import java.util.ArrayList;

/**
 * GameStarted message is used to notify the player that game has started.
 */
public class GameStarted implements ServerMessage{
    ArrayList<String> players;

    /**
     * Constructor GameStarted creates a new GameStarted instance.
     * @param players list of players registered to that game.
     */
    public GameStarted(ArrayList<String> players) {
        this.players = players;
    }

    /**
     * Method getPlayers returns the list of players.
     * @return the list of players.
     */
    public ArrayList<String> getPlayers() {
        return players;
    }
}
