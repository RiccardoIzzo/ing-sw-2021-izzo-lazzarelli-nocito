package it.polimi.ingsw.events.servermessages;

/**
 * SendLeaderCards message is used to send to the player the four leader cards at the beginning of the game.
 */
public class SendLeaderCards implements ServerMessage{
    private final int[] cardsID;

    /**
     * Constructor SendLeaderCards creates a new SendLeaderCards instance.
     * @param cardsID array of leader cards id.
     */
    public SendLeaderCards(int[] cardsID){
        this.cardsID = cardsID;
    }

    /**
     * Method getCardsID returns the ids of the four leader cards.
     * @return an array of leader cards id.
     */
    public int[] getCardsID(){
        return cardsID;
    }
}
