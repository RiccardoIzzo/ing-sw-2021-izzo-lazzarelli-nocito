package it.polimi.ingsw.model;

import it.polimi.ingsw.model.token.TokenDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TokenDeckTest tests TokenDeck class.
 *
 * @author Riccardo Izzo
 */
public class TokenDeckTest {
    TokenDeck tokenDeck;

    /**
     * Method initialization create an instance of TokenDeck;
     */
    @BeforeEach
    public void initialization(){
        tokenDeck = new TokenDeck();
    }

    /**
     * Method resetTest tests the correct initialization of the stack with 7 tokens.
     */
    @Test
    public void resetTest(){
        tokenDeck.reset();
        assertEquals(7, tokenDeck.numTokens());
    }

    /**
     * Method drawTest tests the draw method and checks the correctness of the number of tokens left in the stack.
     */
    @Test
    public void drawTest(){
        tokenDeck.reset();
        tokenDeck.draw();
        assertEquals(6, tokenDeck.numTokens());
        tokenDeck.draw();
        tokenDeck.draw();
        assertEquals(4, tokenDeck.numTokens());
        tokenDeck.reset();
        assertEquals(7, tokenDeck.numTokens());
    }
}