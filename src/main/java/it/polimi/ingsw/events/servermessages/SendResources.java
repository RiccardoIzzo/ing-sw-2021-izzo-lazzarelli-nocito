package it.polimi.ingsw.events.servermessages;

import it.polimi.ingsw.model.ResourceMap;

/**
 * SendResources message is used to send to the player the resources taken from the market.
 */
public class SendResources implements ServerMessage{
    private final ResourceMap resources;

    /**
     * Constructor SendResources creates a new SendResources instance.
     * @param resourceMap the resources obtained.
     */
    public SendResources(ResourceMap resourceMap){
        this.resources = resourceMap;
    }

    /**
     * Method getResources returns the resources.
     * @return the resources.
     */
    public ResourceMap getResources(){
        return resources;
    }
}
