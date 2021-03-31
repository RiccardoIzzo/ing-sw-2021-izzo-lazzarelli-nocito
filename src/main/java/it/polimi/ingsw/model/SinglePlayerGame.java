package it.polimi.ingsw.model;

/**
 * SinglePlayerGame class extends Game class and implements the logic of a single player match.
 *
 * @author Riccardo Izzo
 */
public class SinglePlayerGame extends Game{
    private TokenDeck tokenStack;

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
        tokenStack.draw().playToken(this);
    }
}
