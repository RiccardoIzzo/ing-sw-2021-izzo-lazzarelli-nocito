package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Shelf;

public class ExtraShelfLeaderCard extends LeaderCard {
    private Shelf shelf;

    /**
     * Constructor Card takes two parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * Constructor ExtraShelfLeaderCard takes a Shelf as parameter
     * @param victoryPoints are the card's victory points.
     * @param requirement   is the card's requirement.
     */
    public ExtraShelfLeaderCard(int victoryPoints, Requirement requirement, Shelf shelf) {
        super(victoryPoints, requirement);
        this.shelf = shelf;
    }

    @Override
    public void setAbility(Player player) {
        player.getDashboard().addShelf(shelf);
    }
}
