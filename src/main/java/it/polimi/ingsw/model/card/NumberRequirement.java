package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

/**
 * Class NumberRequirement has a CardMap attribute whose values represent the number of Card required for each CardColor.
 * @author Gabriele Lazzarelli
 */
public class NumberRequirement extends Requirement {
    private final CardMap number;

    /**
     * Constructor NumberRequirement creates an instance of this class.
     * @param number is a CardMap, contains the required number of card for each CardColor
     */
    public NumberRequirement(CardMap number) {
        this.number = number;
    }

    /**
     * Method checkRequirement checks if the selected player meets the requirement.
     * @param player the Player to check
     * @return true if the requirement is met, else false.
     */
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
