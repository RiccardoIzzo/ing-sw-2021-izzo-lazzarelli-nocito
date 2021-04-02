package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

public class LevelRequirement extends Requirement {
    private final CardMap level;

    public LevelRequirement(CardMap level) {
        this.level = level;
    }

    public boolean checkRequirement(Player player) {
        CardMap playerLevelOfCard = player.getLevelOfCard();

        for (CardColor value : CardColor.values()) {
            if (playerLevelOfCard.getCard(value) < level.getCard(value)){
                return false;
            }
        }
        return true;
    }
}
