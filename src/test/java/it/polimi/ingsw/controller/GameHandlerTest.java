package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import it.polimi.ingsw.network.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class GameHandlerTest {
    private GameHandler gameHandlerTest;
    private Server serverTest;
    @Before
    public void setUp() {
        serverTest = new Server(1024);
        gameHandlerTest = new GameHandler(serverTest, "test");
        gameHandlerTest.setGameMode(2);
    }
    @After
    public void tearDown() {
        gameHandlerTest = null;
    }

    /**
     * Method testSetGameMode sets the number of players to 1 and checks that game is set as an instance of SinglePlayerGame,
     * then sets the numbers of players to 2 and checks  that game is set as an instance of MultiplayerGame
     * */
    @Test
    public void testSetGameMode() {
        Game game = gameHandlerTest.getGame();
        assertFalse(game instanceof SinglePlayerGame);
        assertTrue(game instanceof MultiplayerGame);

        gameHandlerTest.setGameMode(1);
        game = gameHandlerTest.getGame();
        assertTrue(game instanceof SinglePlayerGame);


    }

}