package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;

/**
 * Class ExtraShelfLeaderCard is a subclass of LeaderCard, when active provides the player an extra Shelf to store resources.
 * @author Gabriele Lazzarelli
 */
public class ExtraShelfLeaderCard extends LeaderCard {
    private final Resource resource;

    /**
     * Constructor Card takes three parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * Constructor ExtraShelfLeaderCard takes a Resource as parameter
     * @param cardID        is this Card id.
     * @param victoryPoints are this Card victory points.
     * @param requirement   is this Card requirement.
     * @param resource      is the allowed resource in the extra shelf provided by this LeaderCard
     */
    public ExtraShelfLeaderCard(int cardID, int victoryPoints, Requirement requirement, Resource resource) {
        super(cardID, victoryPoints, requirement);
        this.resource = resource;
    }

    /**
     * Method getResource gets the resource allowed in the extra shelf provided by this LeaderCard
     * @return a Resource, the resource for the shelf provided
     */
    public Resource getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return "\n cardID " + super.getCardID()+
                ",\n points=" + getVictoryPoints() +
                ",\n " + getRequirement() +
                ",\n additional space=" + "(2)" + resource.toString();
    }
}
