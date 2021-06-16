package it.polimi.ingsw.events.clientmessages;

import it.polimi.ingsw.model.ResourceMap;

/**
 * SendBonusResource message is used by the player at the beginning of the game to select the bonus resources based on turn order.
 */
public class SendBonusResources implements ClientMessage{
    private final ResourceMap bonusResource;

    /**
     * Constructor SendBonusResources creates a new SendBonusResources instance.
     * @param bonusResource ResourceMap that contains the amount of selected bonus resources.
     */
    public SendBonusResources(ResourceMap bonusResource) {
        this.bonusResource = bonusResource;
    }

    /**
     * Method getBonusResource returns the bonus resources contained in the ResourceMap.
     * @return the bonus resources.
     */
    public ResourceMap getBonusResource() {
        return bonusResource;
    }
}
