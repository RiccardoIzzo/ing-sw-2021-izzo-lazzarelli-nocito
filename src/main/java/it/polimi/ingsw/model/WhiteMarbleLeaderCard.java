package it.polimi.ingsw.model;

import java.util.Set;

public class WhiteMarbleLeaderCard extends LeaderCard{
    private final Set<MarbleColor> exchange;

    /**
     * Constructor Card takes two parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * Constructor WhiteMarbleLeaderCard takes a ProductionPower parameter.
     * @param victoryPoints is the card's victoryPoints.
     * @param requirement   is the card's Requirement.
     * @param exchange      is a collection of MarbleColor which can be traded for the WhiteMarble in Market.
     */
    public WhiteMarbleLeaderCard(int victoryPoints, Requirement requirement, Set<MarbleColor> exchange) {
        super(victoryPoints, requirement);
        this.exchange = exchange;
    }

    public Set<MarbleColor> getExchange() {
        return exchange;
    }

    @Override
    public void setAbility(Player player) {
        player.addExchange(exchange);
    }
}
