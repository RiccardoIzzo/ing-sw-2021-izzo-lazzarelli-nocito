package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

/**
 * Class LevelRequirement has a CardMap attribute whose values represent the level of Card required for each CardColor.
 * @author Gabriele Lazzarelli
 */
public class LevelRequirement implements Requirement {
    private final CardMap level;

    /**
     * Constructor LevelRequirement creates an instances of LevelRequirement
     * @param level is a CardMap which values represents the level required for each CardColor
     */
    public LevelRequirement(CardMap level) {
        this.level = level;
    }

    /**
     * Method checkRequirement checks if the selected player meets the requirement.
     * @param player the Player to check
     * @return true if the requirement is met, else false.
     */
    public boolean checkRequirement(Player player) {
        CardMap playerLevelOfCard = player.getLevelOfCard();

        for (CardColor value : CardColor.values()) {
            if (playerLevelOfCard.getCard(value) < level.getCard(value)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "LevelRequirement{" +
                "level=" + level.toString() +
                '}';
    }
}
