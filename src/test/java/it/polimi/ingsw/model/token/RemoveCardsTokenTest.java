package it.polimi.ingsw.model.token;

import it.polimi.ingsw.model.SinglePlayerGame;
import it.polimi.ingsw.model.card.CardColor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RemoveCardsTokenTest {
    SinglePlayerGame singlePlayerGame;
    SoloActionToken token;

    /**
     * Method initialization creates an instance of SinglePlayerGame and generates the grid of development cards.
     */
    @Before
    public void initialization(){
        singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.generateGrid();
    }

    /**
     * Method playTokenTest simulates the activation of different tokens.
     */
    @Test
    public void playTokenTest(){
        //activation of 4 tokens that discard green cards (green cards are in the first column of the grid)
        assertEquals(4, singlePlayerGame.getDeck(0,0).getCards().size());
        new RemoveCardsToken(CardColor.GREEN).playToken(singlePlayerGame);
        assertEquals(2, singlePlayerGame.getDeck(0,0).getCards().size());
        assertEquals(4, singlePlayerGame.getDeck(1,0).getCards().size());
        new RemoveCardsToken(CardColor.GREEN).playToken(singlePlayerGame);
        assertTrue(singlePlayerGame.getDeck(0, 0).getCards().isEmpty());
        new RemoveCardsToken(CardColor.GREEN).playToken(singlePlayerGame);
        new RemoveCardsToken(CardColor.GREEN).playToken(singlePlayerGame);
        assertTrue(singlePlayerGame.getDeck(0, 0).getCards().isEmpty());
        assertTrue(singlePlayerGame.getDeck(1, 0).getCards().isEmpty());

        //activation of 3 tokens that discard blue cards (blue cards are in the third column of the grid)
        new RemoveCardsToken(CardColor.BLUE).playToken(singlePlayerGame);
        assertEquals(2, singlePlayerGame.getDeck(0,2).getCards().size());
        new RemoveCardsToken(CardColor.BLUE).playToken(singlePlayerGame);
        new RemoveCardsToken(CardColor.BLUE).playToken(singlePlayerGame);
        assertTrue(singlePlayerGame.getDeck(0, 2).getCards().isEmpty());
        assertEquals(2, singlePlayerGame.getDeck(1,2).getCards().size());

    }

}