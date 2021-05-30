package it.polimi.ingsw.events.clientmessages;

/**
 * CheckRequirement message is used by the player to check the requirement of a card.
 */
public class CheckRequirement implements ClientMessage{
    private final int id;

    /**
     * Constructor CheckRequirement creates a new CheckRequirement instance.
     * @param id card id.
     */
    public CheckRequirement(int id) {
        this.id = id;
    }

    /**
     * Method getId returns the card id.
     * @return the card id.
     */
    public int getId() {
        return id;
    }
}
