package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.ResourceMap;

/**
 * Class DiscountLeaderCard is a type of LeaderCard which provides the player discount when buying DevelopmentCard.
 */
public class DiscountLeaderCard extends LeaderCard {
    private final ResourceMap discount;

    /**
     * Constructor of the superclass Card takes two parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * Constructor of DiscountLeader takes a ResourceMap containing the discount for each Resource.
     * @param victoryPoints are the card's victory points.
     * @param requirement   is the card's requirement.
     * @param discount      is a ResourceMap, each Resource has an Integer associated, the discount value.
     */
    public DiscountLeaderCard(int victoryPoints, Requirement requirement, ResourceMap discount) {
        super(victoryPoints, requirement);
        this.discount = discount;
    }

    /**
     * Method setAbility adds the discount DiscountLeaderCard provides to the list of available discounts
     */
    @Override
    public void setAbility(Player player) {
        player.addDiscount(discount);
    }
}
