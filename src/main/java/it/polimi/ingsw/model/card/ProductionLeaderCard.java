package it.polimi.ingsw.model.card;

/**
 * Class ProductionLeaderCard is a subclass of LeaderCard, when active which provides the player an extra ProductionPower.
 * @author Gabriele Lazzarelli
 */
public class ProductionLeaderCard extends LeaderCard {
    private final ProductionPower production;

    /**
     * Constructor Card takes three parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * Constructor ProductionLeaderCard takes a ProductionPower parameter.
     * @param cardID        is the card's id.
     * @param victoryPoints is the card's victoryPoints.
     * @param requirement   is the card's Requirement.
     * @param production    is the card's productionPower.
     */
    public ProductionLeaderCard(int cardID, int victoryPoints, Requirement requirement, ProductionPower production) {
        super(cardID, victoryPoints, requirement);
        this.production = production;
    }

    /**
     * Method getProduction gets the productionPower of this LeaderCard
     * @return the productionPower of this LeaderCard
     */
    public ProductionPower getProduction() {
        return production;
    }

    /**
     * Method toString returns the string representation of this ProductionLeaderCard.
     * @return the string representation.
     */
    @Override
    public String toString() {
        return "ID: " + super.getCardID() +
                "\n -VP: " + getVictoryPoints() +
                "\n " + getRequirement() +
                "\n -Production: " + production.toString();
    }
}
