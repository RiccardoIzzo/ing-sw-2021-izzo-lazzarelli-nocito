package it.polimi.ingsw.events.clientmessages;

/**
 * CreateLobby message is used to create a lobby with the chosen id and to set the number of players for that lobby.
 */
public class CreateLobby implements ClientMessage{
    private final String lobbyID;
    private final int numPlayers;

    /**
     * Constructor CreateLobby creates a new CreateLobby instance.
     * @param id unique id of the lobby.
     * @param numPlayers maximum number of players.
     */
    public CreateLobby(String id, int numPlayers){
        this.lobbyID = id;
        this.numPlayers = numPlayers;
    }

    /**
     * Method getLobbyID returns the id of the lobby.
     * @return lobby id.
     */
    public String getLobbyID(){
        return lobbyID;
    }

    /**
     * Method getNumPlayers returns the maximum number of players.
     * @return the number of players.
     */
    public int getNumPlayers() {
        return numPlayers;
    }
}
