package it.polimi.ingsw.events.clientmessages;

/**
 * JoinLobby message is used to join a lobby with the chosen id.
 */
public class JoinLobby implements ClientMessage{
    private final String lobbyID;

    /**
     * Constructor JoinLobby creates a new JoinLobby instance.
     * @param id unique id of the lobby.
     */
    public JoinLobby(String id){
        this.lobbyID = id;
    }

    /**
     * Method getLobbyID returns the id of the lobby.
     * @return lobby id.
     */
    public String getLobbyID(){
        return lobbyID;
    }
}
