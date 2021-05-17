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
        gameHandlerTest = new GameHandler(serverTest);
        gameHandlerTest.setGameMode(2);
    }
    @After
    public void tearDown() {
        gameHandlerTest = null;
    }


}