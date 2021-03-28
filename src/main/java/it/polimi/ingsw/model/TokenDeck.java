package it.polimi.ingsw.model;

import java.util.Collections;
import java.util.Stack;

/**
 * TokenDeck class represents the stack of seven tokens that is used in a single player game.
 *
 * @author Riccardo Izzo
 */
public class TokenDeck {
    private Stack<SoloActionToken> tokens;

    /**
     * Constructor TokenDeck creates a new TokenDeck instance.
     */
    public TokenDeck(){
        tokens = new Stack<>();
    }

    /**
     * Method reset cleans the stack, inserts the seven types of tokens and shuffles it.
     */
    public void reset(){
        tokens.clear();
        tokens.push(new RemoveCardsToken(CardColor.GREEN));
        tokens.push(new RemoveCardsToken(CardColor.YELLOW));
        tokens.push(new RemoveCardsToken(CardColor.BLUE));
        tokens.push(new RemoveCardsToken(CardColor.PURPLE));
        tokens.push(new MoveBlackMarkerToken(1, true));
        tokens.push(new MoveBlackMarkerToken(2, false));
        tokens.push(new MoveBlackMarkerToken(2, false));
        Collections.shuffle(tokens);
    }

    /**
     * Method draw returns the token at the top of the stack.
     * @return the token.
     */
    public SoloActionToken draw(){
        return tokens.pop();
    }
}
