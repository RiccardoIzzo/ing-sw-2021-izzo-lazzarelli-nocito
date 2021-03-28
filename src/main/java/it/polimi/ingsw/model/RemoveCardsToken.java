package it.polimi.ingsw.model;

/**
 * RemoveCardsToken class implements SoloActionToken interface and represents the token that discards two development cards of the indicated type
 * from the bottom of the grid.
 *
 * @author Riccardo Izzo
 */
public class RemoveCardsToken implements SoloActionToken{
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
     * Method getColor returns the color of the development card to be discarded.
     * @return the card color.
     */
    public CardColor getColor() {
        return color;
    }

    /**
     * Method playToken represents the effect of the token.
     */
    @Override
    public void playToken() {

    }

}
