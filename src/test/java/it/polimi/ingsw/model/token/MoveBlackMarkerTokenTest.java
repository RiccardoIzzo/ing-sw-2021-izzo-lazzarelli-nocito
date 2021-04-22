package it.polimi.ingsw.model.token;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SinglePlayerGame;
import it.polimi.ingsw.model.player.SinglePlayerFaithTrack;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MoveBlackMarkerTokenTest tests MoveBlackMarkerToken class.
 *
 * @author Riccardo Izzo
 */
public class MoveBlackMarkerTokenTest {
    Game singlePlayerGame;

    /**
     * Method initialization creates an instance of SinglePlayerGame, adds one player and generates the grid of development cards.
     */
    @Before
    public void initialization() {
        singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.addPlayer("Riccardo");
    }

    /**
     * Method playTokenTest simulates the activation of different tokens of type MoveBlackMarkerToken.
     */
    @Test
    public void playTokenTest() {
        SinglePlayerFaithTrack path = (SinglePlayerFaithTrack) singlePlayerGame.getPlayerByName("Riccardo").getDashboard().getPath();
        assertEquals(0, path.getPlayerPos());
        new MoveBlackMarkerToken(2, false).playToken(singlePlayerGame);
        assertEquals(0, path.getPlayerPos());
        assertEquals(2, path.getBlackFaithMarker());
        new MoveBlackMarkerToken(2, false).playToken(singlePlayerGame);
        assertEquals(4, path.getBlackFaithMarker());
        new MoveBlackMarkerToken(1, true).playToken(singlePlayerGame);
        assertEquals(5, path.getBlackFaithMarker());
    }
}