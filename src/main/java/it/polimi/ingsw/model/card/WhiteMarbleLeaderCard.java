package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.MarbleColor;

import java.util.Set;

/**
 * Class WhiteMarbleLeaderCard is a subclass of LeaderCard, when active the player can exchange the white marble for a resource.
 * @author Gabriele Lazzarelli
 */
public class WhiteMarbleLeaderCard extends LeaderCard {
    private final Set<MarbleColor> exchange;

    /**
     * Constructor Card takes three parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * Constructor WhiteMarbleLeaderCard takes a ProductionPower parameter.
     * @param cardID        is the card's id.
     * @param victoryPoints is the card's victoryPoints.
     * @param requirement   is the card's Requirement.
     * @param exchange      is a collection of MarbleColor which can be traded for the WhiteMarble in Market.
     */
    public WhiteMarbleLeaderCard(int cardID, int victoryPoints, Requirement requirement, Set<MarbleColor> exchange) {
        super(cardID, victoryPoints, requirement);
        this.exchange = exchange;
    }

    /**
     * Method getExchange gets the exchange option for the white marble of this LeaderCard
     * @return the Set<MarbleColor> of possible exchanges this LeaderCard provides
     */
    public Set<MarbleColor> getExchange() {
        return exchange;
    }

    /**
     * Method toString returns the string representation of this WhiteMarbleLeaderCard.
     * @return the string representation.
     */
    @Override
    public String toString() {
        return "ID: " + super.getCardID() +
                "\n -VP: " + getVictoryPoints() +
                "\n " + getRequirement() +
                "\n -Exchange: " + exchange.toString();
    }
}
