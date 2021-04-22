package it.polimi.ingsw.model.token;

import it.polimi.ingsw.model.card.CardColor;
import java.util.Collections;
import java.util.Stack;

/**
 * TokenDeck class represents the stack of seven tokens that is used in a single player game.
 *
 * @author Riccardo Izzo
 */
public class TokenDeck {
    private final Stack<SoloActionToken> tokens;

    /**
     * Constructor TokenDeck creates a new TokenDeck instance.
     */
    public TokenDeck(){
        tokens = new Stack<>();
    }

    /**
     * Method getTokens returns the stack of tokens.
     * @return the stack of tokens.
     */
    public Stack<SoloActionToken> getTokens(){
        return tokens;
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

    /**
     * Method numTokens returns the number of the tokens in the stack.
     * @return the number of tokens.
     */
    public int numTokens(){
        return tokens.size();
    }
}
