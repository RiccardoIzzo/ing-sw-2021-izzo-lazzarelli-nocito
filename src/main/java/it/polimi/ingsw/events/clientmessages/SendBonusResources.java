package it.polimi.ingsw.events.clientmessages;

import it.polimi.ingsw.model.ResourceMap;

public class SendBonusResources implements ClientMessage{
    private final ResourceMap bonusResource;

    public SendBonusResources(ResourceMap bonusResource) {
        this.bonusResource = bonusResource;
    }

    public ResourceMap getBonusResource() {
        return bonusResource;
    }
}
