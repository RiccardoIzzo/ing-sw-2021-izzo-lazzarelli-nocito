package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.player.Player;

import java.util.Set;

/**
 * Class WhiteMarbleLeaderCard is a subclass of LeaderCard, when active the player can exchange the white marble for a resource.
 * @author Gabriele Lazzarelli
 */
public class WhiteMarbleLeaderCard extends LeaderCard {
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

    /**
     * Method setAbility adds one (or more) types of possible exchanges for the white marble in the available exchange list.
     * @param player is the Player which gets the ability to exchange the white marble.
     */
    @Override
    public void setAbility(Player player) {
        player.addExchange(exchange);
    }
}
