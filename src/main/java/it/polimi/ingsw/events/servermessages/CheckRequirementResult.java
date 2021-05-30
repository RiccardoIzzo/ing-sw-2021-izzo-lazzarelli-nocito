package it.polimi.ingsw.events.servermessages;

/**
 * CheckRequirementResult message is used to notify the player whether the requirement are met or not.
 */
public class CheckRequirementResult implements ServerMessage{
    private final boolean requirementMet;
    private final int id;

    /**
     * Method CheckRequirementResult creates a new CheckRequirementResult instance.
     * @param requirementMet true if the requirement are met, false otherwise.
     * @param id card id.
     */
    public CheckRequirementResult(boolean requirementMet, int id) {
        this.requirementMet = requirementMet;
        this.id = id;
    }

    /**
     * Method isRequirementMet returns the outcome of the requirements check.
     * @return true if the requirement are met, false otherwise.
     */
    public boolean isRequirementMet() {
        return requirementMet;
    }

    /**
     * Method getId returns the card id.
     * @return the card id.
     */
    public int getId() {
        return id;
    }
}
