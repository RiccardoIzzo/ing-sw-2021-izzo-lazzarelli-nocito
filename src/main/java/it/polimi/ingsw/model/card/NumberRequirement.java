package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

public class NumberRequirement extends Requirement {
    private final CardMap number;

    public NumberRequirement(CardMap number) {
        this.number = number;
    }

    public boolean checkRequirement(Player player) {
        CardMap playerNumberOfCard = player.getNumberOfCard();

        for (CardColor value : CardColor.values()) {
            if (playerNumberOfCard.getCard(value) < number.getCard(value)){
                return false;
            }
        }
        return true;
    }
}
