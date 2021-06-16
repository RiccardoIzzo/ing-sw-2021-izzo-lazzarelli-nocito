package it.polimi.ingsw.model.player;

import java.util.Comparator;

/**
 * PlayerComparator class implements Comparator interface and defines a new compare method for Player.
 */
public class PlayerComparator implements Comparator<Player> {
    /**
     * Method compare compares two Player based on their victory points and their total amount of resources.
     * This second comparison is made only if the two players have the same victory points.
     * @param playerOne first player.
     * @param playerTwo second player.
     * @return one of -1, 0, or 1 according to whether the result of the comparison is negative, zero or positive.
     */
    @Override
    public int compare(Player playerOne, Player playerTwo) {
        if (playerOne.getVictoryPoints() == playerTwo.getVictoryPoints()) {
            return playerTwo.getTotalResources().getAmount().compareTo(playerOne.getTotalResources().getAmount());
        } else if (playerOne.getVictoryPoints() > playerTwo.getVictoryPoints()) {
            return -1;
        } else return 1;
    }
}
