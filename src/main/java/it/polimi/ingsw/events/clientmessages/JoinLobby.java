package it.polimi.ingsw.events.clientmessages;

import java.util.Optional;

/**
 * JoinLobby message is used to join a lobby with the chosen ID or, if the number of player has been selected, to create one.
 */
public class JoinLobby implements ClientMessage{
    private final String lobbyID;
    private final Optional<Integer> numPlayers;

    /**
     * Constructor JoinLobby creates a new JoinLobby instance.
     * @param id unique id of the lobby.
     * @param num number of players selected by the first player that created the lobby.
     */
    public JoinLobby(String id, Integer num){
        this.lobbyID = id;
        this.numPlayers = Optional.ofNullable(num);
    }

    /**
     * Method getLobbyID returns the id of the lobby.
     * @return lobby id.
     */
    public String getLobbyID(){
        return lobbyID;
    }

    /**
     * Method getNumPlayers returns the number of players.
     * @return the number o players.
     */
    public Integer getNumPlayers(){
        return numPlayers.orElse(null);
    }
}
