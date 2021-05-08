package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.ResourceMap;

/**
 * Class DiscountLeaderCard is a subclass of LeaderCard, if active provides the player a discount when buying a DevelopmentCard.
 * @author Gabriele Lazzarelli
 */
public class DiscountLeaderCard extends LeaderCard {
    private final ResourceMap discount;

    /**
     * Constructor of the superclass Card takes three parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * Constructor of DiscountLeader takes a ResourceMap containing the discount for each Resource.
     * @param cardID        is the card's id.
     * @param victoryPoints are the card's victory points.
     * @param requirement   is the card's requirement.
     * @param discount      is a ResourceMap, each Resource has an Integer associated, the discount value.
     */
    public DiscountLeaderCard(int cardID, int victoryPoints, Requirement requirement, ResourceMap discount) {
        super(cardID, victoryPoints, requirement);
        this.discount = discount;
    }

    /**
     * Method getDiscount gets the discount of this LeaderCard
     * @return the ResourceMap representing the discount of this LeaderCard
     */
    public ResourceMap getDiscount() {
        return discount;
    }
}
