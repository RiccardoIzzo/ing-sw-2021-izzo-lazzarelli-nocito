package it.polimi.ingsw.model.token;

import it.polimi.ingsw.model.SinglePlayerGame;
import it.polimi.ingsw.model.player.SinglePlayerFaithTrack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * MoveBlackMarkerTokenTest tests MoveBlackMarkerToken class.
 *
 * @author Riccardo Izzo
 */
public class MoveBlackMarkerTokenTest {
    SinglePlayerGame game;

    /**
     * Method initialization creates an instance of SinglePlayerGame, adds one player and generates the grid of development cards.
     */
    @Before
    public void initialization() {
        game = new SinglePlayerGame();
        game.addPlayer("Riccardo");
    }

    /**
     * Method playTokenTest simulates the activation of different tokens of type MoveBlackMarkerToken.
     */
    @Test
    public void playTokenTest() {
        SinglePlayerFaithTrack path = (SinglePlayerFaithTrack) game.getPlayerByName("Riccardo").getDashboard().getPath();
        assertEquals(0, path.getPlayerPos());
        game.playToken(new MoveBlackMarkerToken(2, false));
        assertEquals(0, path.getPlayerPos());
        assertEquals(2, path.getBlackFaithMarker());
        game.playToken(new MoveBlackMarkerToken(2, false));
        assertEquals(4, path.getBlackFaithMarker());
        game.playToken(new MoveBlackMarkerToken(1, true));
        assertEquals(5, path.getBlackFaithMarker());
    }
}