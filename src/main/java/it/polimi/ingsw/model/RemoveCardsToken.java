package it.polimi.ingsw.model;

/**
 * RemoveCardsToken class implements SoloActionToken interface and represents the token that discards two development cards of the indicated type
 * from the bottom of the grid.
 *
 * @author Riccardo Izzo
 */
public class RemoveCardsToken implements SoloActionToken{
    private final CardColor color;
    private int numDiscard = 2;

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
    public void playToken(SinglePlayerGame game) {
        Deck[][] deck = game.getGrid();
        DevelopmentCard card;
        int row = 0, col = 0;

        for (int i = 0; i < 4; i++) {
            card = (DevelopmentCard) deck[0][i].getTopCard();
            if(card.getType().equals(getColor())){
                col = i;
            }
        }

        while(numDiscard > 0) {
            if(deck[row][col].getCards().empty()){
                if(row == 2) break; //One type of DevelopmentCard is no longer available -> player has lost. Call a method (to implement) that ends the game.
                else row++;
            }
            else{
                deck[row][col].getCards().pop();
                numDiscard--;
            }
        }
    }

}
