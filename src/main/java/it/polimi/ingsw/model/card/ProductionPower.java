package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.model.player.Player;

/**
 * Class ProductionPower wraps all of what's relevant to each production of resources in the game.
 * @author Gabriele Lazzarelli
 */
public class ProductionPower {
    private final ResourceMap inputResource;
    private final ResourceMap outputResource;
    private final int outputFaith;

    /**
     * Constructor ProductionPower instantiates a particular type of production.
     * @param inputResource is a ResourceMap containing the resources which the power use.
     * @param outputResource is a ResourceMap containing the resources which the power produce.
     * @param outputFaith is an Integer, the faith points the power produce.
     */
    public ProductionPower(ResourceMap inputResource, ResourceMap outputResource, int outputFaith) {
        this.inputResource = inputResource;
        this.outputResource = outputResource;
        this.outputFaith = outputFaith;
    }

    /**
     * Method GetInputResource gets the resources consumed by the power.
     * @return a ResourceMap containing all the resources the power use.
     */
    public ResourceMap getInputResource() {
        return inputResource;
    }

    /**
     * Method GetOutputResource gets the resources produced by the power.
     * @return a ResourceMap containing all the resources the power produce.
     */
    public ResourceMap getOutputResource() {
        return outputResource;
    }

    /**
     * Method getOutputFaith gets the faith produced by the power.
     * @return an Integer, the faith points the power produce.
     */
    public int getOutputFaith() {
        return outputFaith;
    }

    @Override
    public String toString() {
        return  "\n\t inputResource=" + inputResource.asList().toString() +
                ",\n\t outputResource=" + outputResource.asList().toString() +
                ",\n\t outputFaith=" + outputFaith;
    }
}
