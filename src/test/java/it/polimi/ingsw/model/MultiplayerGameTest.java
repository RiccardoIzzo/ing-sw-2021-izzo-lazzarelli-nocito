package it.polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * MultiplayerGameTest tests MultiplayerGame class.
 *
 * @author Riccardo Izzo
 */
public class MultiplayerGameTest {
    MultiplayerGame multiplayerGame;

    /**
     * Method initialization create an instance of MultiplayerGame, adds three players and choose randomly the first player.
     */
    @Before
    public void initialization(){
        multiplayerGame = new MultiplayerGame();
        multiplayerGame.addPlayer("Riccardo");
        multiplayerGame.addPlayer("Andrea");
        multiplayerGame.addPlayer("Gabriele");
        multiplayerGame.setFirstPlayer();
    }

    /**
     * Method playerRotation tests the clockwise rotation of the players during the game.
     */
    @Test
    public void playerRotation(){
        assertEquals(multiplayerGame.getCurrPlayer(), multiplayerGame.getFirstPlayer());
        multiplayerGame.nextPlayer();
        multiplayerGame.nextPlayer();
        assertNotEquals(multiplayerGame.getCurrPlayer(), multiplayerGame.getFirstPlayer());
        multiplayerGame.nextPlayer();
        assertEquals(multiplayerGame.getCurrPlayer(), multiplayerGame.getFirstPlayer());
    }
}