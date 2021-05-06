package it.polimi.ingsw.events.clientmessages;

/**
 * SetNickname message is used to set the nickname of the player before the beginning of the game.
 */
public class SetNickname implements ClientMessage{
    private final String nickname;

    /**
     * Constructor SetNickname creates a new SetNickname instance.
     * @param nickname player's nickname.
     */
    public SetNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     * Method getNickname returns the player's nickname.
     * @return player's nickname.
     */
    public String getNickname(){
        return nickname;
    }
}
