package it.polimi.ingsw.events.clientmessages;

/**
 * ActivateLeaderCard message is used by the player to activate a leader card.
 */
public class ActivateLeaderCard implements ClientMessage{
    private final int index;

    /**
     * Constructor ActivateLeaderCard creates a new ActivateLeaderCard instance.
     * @param index index of the leader card to activate.
     */
    public ActivateLeaderCard(int index){
        this.index = index;
    }

    /**
     * Method getIndex returns the index of the selected leader card.
     * @return the index of the select leader card.
     */
    public int getIndex() {
        return index;
    }
}
