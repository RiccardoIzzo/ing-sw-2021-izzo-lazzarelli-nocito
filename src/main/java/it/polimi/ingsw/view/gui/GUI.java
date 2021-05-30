package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.ModelView;
import it.polimi.ingsw.view.View;
import javafx.application.Application;

import it.polimi.ingsw.events.clientmessages.ClientMessage;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

/**
 * GUI class manages the game with a Graphical User Interface.
 */
public class GUI extends Application implements View {
    static Stage mainStage;
    SetupController setupController;
    static LobbiesController lobbiesController;
    private ActionHandler actionHandler;
    private String nickname;
    private ModelView modelView;
    private NetworkHandler network;



    @Override
    public void start(Stage stage) throws Exception {
        //actionHandler = new ActionHandler(this);
        mainStage = stage;
        setupController = new SetupController();
        setupController.start();

    }
    public static void startLobbies(Scene scene, Map<String, Integer> lobbies) {
        lobbiesController = new LobbiesController();
        lobbiesController.setLobbies(lobbies);
        try {
            lobbiesController.start(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handleNickname(ServerMessage message) {

    }

    @Override
    public void handleLobbies(Map<String, Integer> lobbies) {
    }

    @Override
    public void handleLeaders() {

    }

    @Override
    public void handleBonusResource(int amount) {

    }

    @Override
    public void handleTakeResource() {
    }

    @Override
    public void handleBuyCard() {
    }

    @Override
    public void handleActivateProduction() {
    }

    @Override
    public void handleActivateLeader() {
    }

    @Override
    public void handleDiscardLeader() {
    }

    @Override
    public void handleTemporaryShelf() {

    }

    @Override
    public void handleCheckRequirement(boolean result, int id) {

    }

    @Override
    public void startTurn() {

    }

    @Override
    public void basicActionPlayed() {

    }

    @Override
    public void handleTurn() {

    }

    @Override
    public void handleEndTurn() {
    }

    @Override
    public void setNickname() {

    }

    @Override
    public void setModelView(ModelView modelView) {

    }

    @Override
    public String getInput(String check) {
        return null;
    }

    @Override
    public void printText(String text) {

    }

    @Override
    public void send(ClientMessage message) {

    }

    @Override
    public String getNickname() {
        return null;
    }

    /* ACTION EVENTS */


}
