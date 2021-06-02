package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.player.Dashboard;
import it.polimi.ingsw.model.token.MoveBlackMarkerToken;
import it.polimi.ingsw.model.token.RemoveCardsToken;
import it.polimi.ingsw.model.token.SoloActionToken;
import it.polimi.ingsw.model.token.TokenDeck;

import static it.polimi.ingsw.constants.GameConstants.TOKEN_DRAWN;

/**
 * SinglePlayerGame class extends Game class and implements the logic of a single player match.
 *
 * @author Riccardo Izzo
 */
public class SinglePlayerGame extends Game {
    private final TokenDeck tokenStack;

    /**
     * Constructor SinglePlayerGame creates a new SinglePlayerGame instance.
     */
    public SinglePlayerGame() {
        super();
        tokenStack = new TokenDeck();
        tokenStack.reset();
    }

    /**
     * Method getTokenStack returns the stack of tokens.
     * @return the stack of tokens.
     */
    public TokenDeck getTokenStack() {
        return tokenStack;
    }

    /**
     * Method drawToken activates the token at the top of the stack.
     */
    public void drawToken(){
        SoloActionToken soloActionToken = tokenStack.draw();
        pcs.firePropertyChange(TOKEN_DRAWN, null, soloActionToken);
        this.playToken(soloActionToken);
    }

    /**
     * Method playToken applies the effect of the selected SoloActionToken.
     * @param soloActionToken the SoloActionToken which effect is applied.
     */
    public void playToken(SoloActionToken soloActionToken){
        if (soloActionToken instanceof RemoveCardsToken) {
            CardColor cardColor = ((RemoveCardsToken) soloActionToken).getColor();
            int toRemove = ((RemoveCardsToken) soloActionToken).getNumber();

            for (int level = 1; level <= 3; level++) {
                while(this.getGrid()[level-1][cardColor.getColumnGrid()].getTopCard() != null && toRemove > 0) {
                    this.getGrid()[level-1][cardColor.getColumnGrid()].draw();
                    toRemove--;
                }
                if (toRemove == 0) break;
            }

        } else if (soloActionToken instanceof MoveBlackMarkerToken) {
            int steps = ((MoveBlackMarkerToken) soloActionToken).getSteps();
            boolean reset = ((MoveBlackMarkerToken) soloActionToken).hasResetStack();

            for(int i = 0; i < steps; i++){
                Dashboard dashboard = this.getPlayers().get(0).getDashboard();
                dashboard.incrementBlackFaith();
            }
            if(reset){
                this.getTokenStack().reset();
            }
        }
    }
}
