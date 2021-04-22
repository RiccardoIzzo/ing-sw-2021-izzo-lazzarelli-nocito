package it.polimi.ingsw.model.token;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.card.DevelopmentCard;

/**
 * RemoveCardsToken class implements SoloActionToken interface and represents the token that discards two development cards of the indicated type
 * from the bottom of the grid.
 *
 * @author Riccardo Izzo
 */
public class RemoveCardsToken implements SoloActionToken {
    private final CardColor color;
    private final int NUM_DISCARD = 2;

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
    public void playToken(Game game) {
        Deck[][] deck = game.getGrid();
        int row = 0, col, n = NUM_DISCARD;

        for (col = 0; col < 4; col++) {
            if(((DevelopmentCard) deck[2][col].getTopCard()).getType().equals(getColor())) break;
        }

        while(n > 0) {
            if(deck[row][col].getCards().empty()){
                if(row == 2) break; //One type of DevelopmentCard is no longer available -> player has lost. Call a method (to implement) that ends the game.
                else row++;
            }
            else{
                deck[row][col].getCards().pop();
                n--;
            }
        }
    }

}
