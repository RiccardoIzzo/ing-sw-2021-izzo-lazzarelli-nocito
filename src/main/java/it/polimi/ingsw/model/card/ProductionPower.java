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

    /**
     * Method equals is overridden, two ProductionPower are equals if their attributes are the same.
     * @param o is the Object instance to compare
     * @return true if the ProductionPower instances have the same attributes
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionPower that = (ProductionPower) o;
        return getOutputFaith() == that.getOutputFaith() && getInputResource().equals(that.getInputResource()) && getOutputResource().equals(that.getOutputResource());
    }

    /**
     * Method activatePower adds the ProductionPower to the player's availableProduction list.
     */
    public void activatePower(Player player) {
        player.addProduction(this);
    }

    /**
     * Method deactivatePower removes the ProductionPower from the player's availableProduction list.
     */
    public void deactivatePower(Player player) {
        player.removeProduction(this);
    }
}
