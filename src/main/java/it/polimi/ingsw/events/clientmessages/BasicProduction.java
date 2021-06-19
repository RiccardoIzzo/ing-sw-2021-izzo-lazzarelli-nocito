package it.polimi.ingsw.events.clientmessages;

import it.polimi.ingsw.model.ResourceMap;

/**
 * BasicProduction message is used to activate the basic production.
 */
public class BasicProduction implements ClientMessage {
    private final ResourceMap inputResources;
    private final ResourceMap outputResources;

    /**
     * Constructor BasicProduction creates a new BasicProduction instance.
     * @param inputResources input resources for the basic production.
     * @param outputResources output resource for the basic production.
     */
    public BasicProduction(ResourceMap inputResources, ResourceMap outputResources) {
        this.inputResources = inputResources;
        this.outputResources = outputResources;
    }

    /**
     * Method getInputProduction returns the input resources.
     * @return a ResourceMap with the input resources.
     */
    public ResourceMap getInputProduction() {
        return inputResources;
    }

    /**
     * Method getOutputProduction returns the output resources.
     * @return a ResourceMap with the output resources.
     */
    public ResourceMap getOutputProduction() {
        return outputResources;
    }
}
