package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * PlayerTest tests Player class.
 *
 * @author Riccardo Izzo
 */
public class PlayerTest {
    Game game;

    /**
     * Method initialization create an instance of Game, adds three players and generates all the leader cards.
     */
    @Before
    public void initialization() {
        game = new Game();
        game.addPlayer("Riccardo");
        game.addPlayer("Andrea");
        game.addPlayer("Gabriele");
        game.generateLeaders();
    }
}