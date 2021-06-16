package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.player.SinglePlayerFaithTrack;
import it.polimi.ingsw.model.token.MoveBlackMarkerToken;
import it.polimi.ingsw.model.token.RemoveCardsToken;
import it.polimi.ingsw.model.token.SoloActionToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * SinglePlayerGameTest tests SinglePlayerGame class.
 *
 * @author Riccardo Izzo
 */
public class SinglePlayerGameTest {
    SinglePlayerGame game;

    /**
     * Method initialization creates an instance of SinglePlayerGame.
     */
    @Before
    public void initialization(){
        game = new SinglePlayerGame();
        game.addPlayer("Riccardo");
        game.generateGrid();
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
            token = game.getTokenStack().getTokens().peek();
            game.drawToken();
            if((token instanceof MoveBlackMarkerToken) && ((MoveBlackMarkerToken) token).getSteps() == 1) stack_size = 7;
            else stack_size--;
            assertEquals(stack_size, game.getTokenStack().numTokens());
        }
    }

    /**
     * Method playTokenTest simulates the activation of different tokens of type RemoveCardsToken.
     */
    @Test
    public void playTokenTestA(){
        //activation of 4 tokens that discard green cards (green cards are in the first column of the grid)
        assertEquals(4, game.getDeck(0,0).getCards().size());
        game.playToken(new RemoveCardsToken(CardColor.GREEN));
        assertEquals(2, game.getDeck(0,0).getCards().size());
        assertEquals(4, game.getDeck(1,0).getCards().size());
        game.playToken(new RemoveCardsToken(CardColor.GREEN));
        assertTrue(game.getDeck(0, 0).getCards().isEmpty());
        game.playToken(new RemoveCardsToken(CardColor.GREEN));
        game.playToken(new RemoveCardsToken(CardColor.GREEN));
        assertTrue(game.getDeck(0, 0).getCards().isEmpty());
        assertTrue(game.getDeck(1, 0).getCards().isEmpty());

        //activation of 3 tokens that discard blue cards (blue cards are in the third column of the grid)
        game.playToken(new RemoveCardsToken(CardColor.BLUE));
        assertEquals(2, game.getDeck(0,2).getCards().size());
        game.playToken(new RemoveCardsToken(CardColor.BLUE));
        game.playToken(new RemoveCardsToken(CardColor.BLUE));
        assertTrue(game.getDeck(0, 2).getCards().isEmpty());
        assertEquals(2, game.getDeck(1,2).getCards().size());
    }

    /**
     * Method playTokenTest simulates the activation of different tokens of type MoveBlackMarkerToken.
     */
    @Test
    public void playTokenTestB() {
        SinglePlayerFaithTrack path = (SinglePlayerFaithTrack) game.getPlayerByName("Riccardo").getDashboard().getFaithTrack();
        assertEquals(0, path.getPlayerPosition());
        game.playToken(new MoveBlackMarkerToken(2, false));
        assertEquals(0, path.getPlayerPosition());
        assertEquals(2, path.getBlackFaithMarker());
        game.playToken(new MoveBlackMarkerToken(2, false));
        assertEquals(4, path.getBlackFaithMarker());
        game.playToken(new MoveBlackMarkerToken(1, true));
        assertEquals(5, path.getBlackFaithMarker());
    }
}