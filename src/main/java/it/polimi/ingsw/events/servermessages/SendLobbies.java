package it.polimi.ingsw.events.servermessages;

import java.util.List;

/**
 * SendLobbies message is used to send to the player the list of lobbies currently available.
 */
public class SendLobbies implements ServerMessage{
    private final List<String> lobbies;

    /**
     * Constructor SendLobbies creates a new SendLobbies instance.
     * @param lobbies the list of lobbies.
     */
    public SendLobbies(List<String> lobbies){
        this.lobbies = lobbies;
    }

    /**
     * Method getLobbies returns the list of lobbies currently available.
     * @return the list of lobbies.
     */
    public List<String> getLobbies(){
        return lobbies;
    }
}
