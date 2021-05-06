package it.polimi.ingsw.events.servermessages;

/**
 * StartTurn message is used to notify the player that it's his turn.
 */
public class StartTurn implements ServerMessage{
    private final String nickname;

    /**
     * Constructor StartTurn creates a new instance of StartTurn.
     * @param nickname nickname of the player that can begin his turn.
     */
    public StartTurn(String nickname){
        this.nickname = nickname;
    }

    /**
     * Method getNickname returns the player's nickname.
     * @return the player's nickname.
     */
    public String getNickname() {
        return nickname;
    }
}
