package it.polimi.ingsw.events.servermessages;

import java.util.Map;

/**
 * SendLobbies message is used to send to the player the list of lobbies currently available.
 */
public class SendLobbies implements ServerMessage{
    private final Map<String, Integer> lobbies;

    /**
     * Constructor SendLobbies creates a new SendLobbies instance.
     * @param lobbies the list of lobbies.
     */
    public SendLobbies(Map<String, Integer> lobbies){
        this.lobbies = lobbies;
    }

    /**
     * Method getLobbies returns the list of lobbies currently available.
     * @return the list of lobbies.
     */
    public Map<String, Integer> getLobbies(){
        return lobbies;
    }
}
