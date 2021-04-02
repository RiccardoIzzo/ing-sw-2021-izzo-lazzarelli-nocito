package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

public class ProductionLeaderCard extends LeaderCard {
    private final ProductionPower production;

    /**
     * Constructor Card takes two parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * Constructor ProductionLeaderCard takes a ProductionPower parameter.
     * @param victoryPoints is the card's victoryPoints.
     * @param requirement   is the card's Requirement.
     * @param production    is the card's productionPower.
     */
    public ProductionLeaderCard(int victoryPoints, Requirement requirement, ProductionPower production) {
        super(victoryPoints, requirement);
        this.production = production;
    }

    /**
     * Method setAbility in ProductionLeaderCard adds this.ProductionPower to availableProduction list in player
     * @param player is the Player which gets the additional ProductionPower
     */
    @Override
    public void setAbility(Player player) {
        production.activatePower(player);
    }
}
