package it.polimi.ingsw.events.clientmessages;

/**
 * ActivateLeaderCard message is used by the player to activate a leader card.
 */
public class ActivateLeaderCard implements ClientMessage{
    private final int cardID;

    /**
     * Constructor ActivateLeaderCard creates a new ActivateLeaderCard instance.
     * @param cardID id of the leader card to activate.
     */
    public ActivateLeaderCard(int cardID){
        this.cardID = cardID;
    }

    /**
     * Method getIndex returns the id of the selected leader card.
     * @return the id of the leader card.
     */
    public int getCardID() {
        return cardID;
    }
}
