package it.polimi.ingsw.model;

public class NumberRequirement implements Requirement{
    private CardMap number;

    public NumberRequirement(CardMap number) {
        this.number = number;
    }

    @Override
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
