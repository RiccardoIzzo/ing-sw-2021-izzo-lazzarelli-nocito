package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.servermessages.*;
import it.polimi.ingsw.network.Server;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MultiplayerGame;
import it.polimi.ingsw.model.SinglePlayerGame;


public class Controller {
    Game game;
    GameHandler gameHandler;
    TurnHandler turnHandler;

    private Server server;

    public Controller() {
        gameHandler = new GameHandler();
        turnHandler = new TurnHandler();
    }
    public void setPlayersNumber(int numberOfPlayers) {
        game = numberOfPlayers > 1 ? new MultiplayerGame() : new SinglePlayerGame();
    }

}
