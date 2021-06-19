package it.polimi.ingsw.events.clientmessages;

import it.polimi.ingsw.model.ResourceMap;

public class BasicProduction implements ClientMessage {
    ResourceMap inputResources;
    ResourceMap outputResources;

    public BasicProduction(ResourceMap inputResources, ResourceMap outputResources) {
        this.inputResources = inputResources;
        this.outputResources = outputResources;
    }

    public ResourceMap getInputProduction() {
        return inputResources;
    }

    public ResourceMap getOutputProduction() {
        return outputResources;
    }
}
