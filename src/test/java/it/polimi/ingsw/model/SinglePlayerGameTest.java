package it.polimi.ingsw.model;

import it.polimi.ingsw.model.token.MoveBlackMarkerToken;
import it.polimi.ingsw.model.token.SoloActionToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * SinglePlayerGameTest tests SinglePlayerGame class.
 *
 * @author Riccardo Izzo
 */
public class SinglePlayerGameTest {
    SinglePlayerGame singlePlayerGame;

    /**
     * Method initialization creates an instance of SinglePlayerGame.
     */
    @Before
    public void initialization(){
        singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.addPlayer("Riccardo");
        singlePlayerGame.generateGrid();
    }

    /**
     * Method drawTokenTest tests the correct activation of NUM_TOKENS tokens.
     */
    @Test
    public void drawTokenTest(){
        // number of tokens to test
        int NUM_TOKENS = 10;
        int stack_size = 7;
        SoloActionToken token;
        for(int i = 0; i < NUM_TOKENS; i++){
            token = singlePlayerGame.getTokenStack().getTokens().peek();
            singlePlayerGame.drawToken(singlePlayerGame);
            if((token instanceof MoveBlackMarkerToken) && ((MoveBlackMarkerToken) token).getSteps() == 1) stack_size = 7;
            else stack_size--;
            assertEquals(stack_size, singlePlayerGame.getTokenStack().numTokens());
        }
    }
}