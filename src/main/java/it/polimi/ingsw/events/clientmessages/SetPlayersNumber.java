package it.polimi.ingsw.events.clientmessages;

/**
 * SetPlayersNumber message is used to set the number of players by the player that creates the lobby.
 */
public class SetPlayersNumber implements ClientMessage{
    private final int numberOfPlayer;

    /**
     * Constructor SetPlayersNumber creates a new SetPlayersNumber instance.
     * @param number the number of players selected by the first player.
     */
    public SetPlayersNumber(int number){
        this.numberOfPlayer = number;
    }

    /**
     * Method getNumberOfPlayer return the number of player.
     * @return the number of players.
     */
    public int getNumberOfPlayer(){
        return numberOfPlayer;
    }
}
