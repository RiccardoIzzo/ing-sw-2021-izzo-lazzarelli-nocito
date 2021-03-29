package it.polimi.ingsw.model;

public class LevelRequirement implements Requirement{
    private CardMap level;

    public LevelRequirement(CardMap level) {
        this.level = level;
    }

    @Override
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
