package it.polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MultiplayerGameTest tests MultiplayerGame class.
 *
 * @author Riccardo Izzo
 */
public class MultiplayerGameTest {
    MultiplayerGame game;

    /**
     * Method initialization create an instance of MultiplayerGame, adds three players and choose randomly the first player.
     */
    @Before
    public void initialization(){
        game = new MultiplayerGame();
        game.addPlayer("Riccardo");
        game.addPlayer("Andrea");
        game.addPlayer("Gabriele");
        game.setFirstPlayer();
    }

    /**
     * Method AddRemovePlayers tests addPlayer and removePlayer methods, checks the number of players.
     */
    @Test
    public void AddRemovePlayers(){
        assertEquals("Riccardo", game.getPlayers().get(0).getName());
        assertEquals("Andrea", game.getPlayers().get(1).getName());
        assertEquals("Gabriele", game.getPlayers().get(2).getName());
        assertEquals(3, game.getNumPlayers());
        game.addPlayer("Francesco");
        assertEquals(4, game.getNumPlayers());
        game.removePlayer("Francesco");
        assertEquals(3, game.getNumPlayers());
        game.removePlayer("Riccardo");
        game.removePlayer("Gabriele");
        game.removePlayer("Andrea");
        assertEquals(0, game.getNumPlayers());
    }

    /**
     * Method playerRotation tests the clockwise rotation of the players during the game.
     */
    @Test
    public void playerRotation(){
        assertEquals(game.getCurrPlayer(), game.getFirstPlayer());
        game.nextPlayer();
        game.nextPlayer();
        assertNotEquals(game.getCurrPlayer(), game.getFirstPlayer());
        game.nextPlayer();
        assertEquals(game.getCurrPlayer(), game.getFirstPlayer());
    }
}