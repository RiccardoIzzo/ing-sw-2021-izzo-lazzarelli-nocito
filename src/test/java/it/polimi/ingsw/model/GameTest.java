package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * GameTest tests Game class.
 *
 * @author Riccardo Izzo
 */
public class GameTest {
    Game game;

    /**
     * Method initialization create an instance of Game and adds three players.
     */
    @BeforeEach
    public void initialization(){
        game = new Game();
        game.addPlayer("Riccardo");
        game.addPlayer("Andrea");
        game.addPlayer("Gabriele");
    }

    /**
     * Method AddRemovePlayers tests addPlayer and removePlayer methods, checks the number of players.
     */
    @Test
    public void AddRemovePlayers(){
        assertEquals("Riccardo", game.getPlayers().get(0).getNickname());
        assertEquals("Andrea", game.getPlayers().get(1).getNickname());
        assertEquals("Gabriele", game.getPlayers().get(2).getNickname());
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
}