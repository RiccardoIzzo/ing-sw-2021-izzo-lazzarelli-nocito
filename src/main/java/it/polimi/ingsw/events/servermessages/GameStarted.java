package it.polimi.ingsw.events.servermessages;

import java.util.ArrayList;

/**
 * GameStarted message is used to notify the player that game has started.
 */
public class GameStarted implements ServerMessage{
    ArrayList<String> players;

    public GameStarted(ArrayList<String> players) {
        this.players = players;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }
}
