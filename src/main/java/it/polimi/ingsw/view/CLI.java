package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;

import javax.swing.*;
import java.io.PrintStream;
import java.util.Scanner;

public class CLI {
    private ActionListener actionListener;
    private ActionHandler actionHandler;
    private Game playerGame;
    private String playerID; //nickname

    private final PrintStream output;
    private final Scanner input;

    CLI() {
        actionHandler = new ActionHandler();
        actionListener = new ActionListener();

        input = new Scanner(System.in);
        output = new PrintStream(System.out);
    }
    public Game getPlayerGame() {
        return playerGame;
    }

    void startGame() {
        // Connection etc. , set playerID

        showDashboard();
        chooseAction();
    }
    void showDashboard(){
        // print dashboard
    }
    void chooseAction() {
        // ask whichAction

        // showLeaderCards
        // showMarker
        // showGrid
        // arrangeShelves
        // showAvailableProductions

        // execute actions and then show updated dashboard
    }

    void showLeaderCards() {
        // show leader card and see if the player can/wants to play a leader card
    }
    //
    void playLeaderCard() {

    }


    void showMarket() {
        // show market and ask if player wants to take resources
    }
    //
    void takeResources() {

    }

    void arrangeShelves() {
        // edit shelves configuration
    }


    void showGrid() {
        // show grid see if the player can/wants to buy a card
    }
    //
    void buyCard() {

    }
    //
    void showAvailableProductions() {
        // show available productions and see if the player can/wants to start one
    }
    void startProduction() {

    }

    //
    void endTurn() {

    }
    void showResults() {

    }
    void updateFaithTrack() {

    }

}
