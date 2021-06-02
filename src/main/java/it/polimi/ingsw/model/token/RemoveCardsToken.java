package it.polimi.ingsw.model.token;

import it.polimi.ingsw.constants.Colors;
import it.polimi.ingsw.model.card.CardColor;

/**
 * RemoveCardsToken class implements SoloActionToken interface and represents the token that discards two development cards of the indicated type
 * from the bottom of the grid.
 *
 * @author Riccardo Izzo
 */
public class RemoveCardsToken implements SoloActionToken {
    private final CardColor color;
    private final int number = 2;

    /**
     * Constructor RemoveCardToken creates a new RemoveCardToken instance.
     * @param color color of the card to be discarded.
     */
    public RemoveCardsToken(CardColor color){
        this.color = color;
    }

    /**
     * Method getColor returns the color of the DevelopmentCard to be discarded.
     * @return the card color.
     */
    public CardColor getColor() {
        return color;
    }

    /**
     * Method getNumber returns the amount of the DevelopmentCard to be removed.
     * @return an integer, the amount of DevelopmentCard.
     */
    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "-" + Colors.ANSI_RED + number + Colors.ANSI_RESET + color;
    }
}
