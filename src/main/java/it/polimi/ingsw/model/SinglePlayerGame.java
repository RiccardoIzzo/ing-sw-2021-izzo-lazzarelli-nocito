package it.polimi.ingsw.model;

/**
 * SinglePlayerGame class extends Game class and implements the logic of a single player match.
 *
 * @author Riccardo Izzo
 */
public class SinglePlayerGame extends Game{
    private Player player;
    private TokenDeck tokenStack;

    /**
     * Constructor SinglePlayerGame creates a new SinglePlayerGame instance.
     */
    public SinglePlayerGame(){
        tokenStack = new TokenDeck();
        tokenStack.reset();
    }
}
