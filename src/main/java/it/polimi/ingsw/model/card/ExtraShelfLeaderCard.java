package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Shelf;

/**
 * Class ExtraShelfLeaderCard is a subclass of LeaderCard, when active provides the player an extra Shelf to store resources.
 * @author Gabriele Lazzarelli
 */
public class ExtraShelfLeaderCard extends LeaderCard {
    private Shelf shelf;

    /**
     * Constructor Card takes three parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * Constructor ExtraShelfLeaderCard takes a Shelf as parameter
     * @param cardID        is this Card id.
     * @param victoryPoints are this Card victory points.
     * @param requirement   is this Card requirement.
     * @param shelf         is the Shelf which can be used by the player.
     */
    public ExtraShelfLeaderCard(int cardID, int victoryPoints, Requirement requirement, Shelf shelf) {
        super(cardID, victoryPoints, requirement);
        this.shelf = shelf;
    }

    /**
     * Method getShelf gets the Shelf of this LeaderCard
     * @return the Shelf of this LeaderCard
     */
    public Shelf getShelf() {
        return shelf;
    }
}
