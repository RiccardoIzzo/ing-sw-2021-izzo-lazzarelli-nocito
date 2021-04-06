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
     * Constructor Card takes two parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * Constructor ExtraShelfLeaderCard takes a Shelf as parameter
     * @param victoryPoints are this Card victory points.
     * @param requirement   is this Card requirement.
     * @param shelf         is the Shelf which can be used by the player.
     */
    public ExtraShelfLeaderCard(int victoryPoints, Requirement requirement, Shelf shelf) {
        super(victoryPoints, requirement);
        this.shelf = shelf;
    }

    /**
     * setAbility method adds this.shelf to the list of shelves in the Warehouse.
     * @param player is the Player which gets the additional shelf
     */
    @Override
    public void setAbility(Player player) {
        player.getDashboard().addShelf(shelf);
    }
}
