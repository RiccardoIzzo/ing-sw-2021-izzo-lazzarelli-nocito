package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

/**
 * Class Requirement has to be implemented by every Card in the game.
 * It counts as a cost for the DevelopmentCard or as a requirement for the LeaderCard.
 * @author Gabriele Lazzarelli
 */
public interface Requirement {
    boolean checkRequirement(Player player);
}