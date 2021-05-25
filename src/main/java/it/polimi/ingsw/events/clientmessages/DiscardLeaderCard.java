package it.polimi.ingsw.events.clientmessages;

/**
 * DiscardLeaderCard message is used by the player to discard a leader card.
 */
public class DiscardLeaderCard implements ClientMessage{
    private final int id;

    /**
     * Constructor DiscardLeaderCard creates a new DiscardLeaderCard instance.
     * @param cardID id of the leader card to discard.
     */
    public DiscardLeaderCard(int cardID){
        this.id = cardID;
    }

    /**
     * Method getId returns the id of the leader card.
     * @return the card id.
     */
    public int getId() {
        return id;
    }
}
