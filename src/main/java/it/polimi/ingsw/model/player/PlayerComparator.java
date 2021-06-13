package it.polimi.ingsw.model.player;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player playerOne, Player playerTwo) {
        if (playerOne.getVictoryPoints() == playerTwo.getVictoryPoints()) {
            return playerTwo.getTotalResources().size().compareTo(playerOne.getTotalResources().size());
        } else if (playerOne.getVictoryPoints() > playerTwo.getVictoryPoints()) {
            return -1;
        } else return 1;
    }
}
