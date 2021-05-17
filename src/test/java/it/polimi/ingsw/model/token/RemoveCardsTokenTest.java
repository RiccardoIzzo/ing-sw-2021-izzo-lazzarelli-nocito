package it.polimi.ingsw.model.token;

import it.polimi.ingsw.model.SinglePlayerGame;
import it.polimi.ingsw.model.card.CardColor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * RemoveCardsTokenTest tests RemoveCardsToken class.
 *
 * @author Riccardo Izzo
 */
public class RemoveCardsTokenTest {
    SinglePlayerGame game;

    /**
     * Method initialization creates an instance of SinglePlayerGame and generates the grid of development cards.
     */
    @Before
    public void initialization(){
        game = new SinglePlayerGame();
        game.generateGrid();
    }

    /**
     * Method playTokenTest simulates the activation of different tokens of type RemoveCardsToken.
     */
    @Test
    public void playTokenTest(){
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
}