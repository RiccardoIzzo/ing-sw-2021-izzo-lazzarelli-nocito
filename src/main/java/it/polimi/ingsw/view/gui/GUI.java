package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.events.clientmessages.GetLobbies;
import it.polimi.ingsw.events.clientmessages.SetNickname;
import it.polimi.ingsw.events.servermessages.ValidNickname;
import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.ModelView;
import it.polimi.ingsw.view.View;
import javafx.application.Application;

import it.polimi.ingsw.events.clientmessages.ClientMessage;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * GUI class manages the game with a Graphical User Interface.
 */
public class GUI extends Application implements View {
    Stage mainStage;
    private static NetworkHandler network;
    SetupController setupController;
    static LobbiesController lobbiesController;
    static SelectLeaderController selectLeaderController;
    private ActionHandler actionHandler;
    private String nickname;
    private ModelView modelView;
//    private NetworkHandler network;



    @Override
    public void start(Stage stage) throws Exception {
        //actionHandler = new ActionHandler(this);
        mainStage = stage;
        setupController = new SetupController();
        setupController.setGUI(this);
        setupController.start();

//        handleLeaders();
    }
    public void connect(String ip, int port) {
        network = new NetworkHandler(ip, port, this);
        network.setConnection();
    }
    public void startLobbies(Map<String, Integer> lobbies) {
        lobbiesController = new LobbiesController();
        lobbiesController.setGUI(this);
        lobbiesController.setLobbies(lobbies);
        try {
            lobbiesController.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handleNickname(ServerMessage message) {
        if(message instanceof ValidNickname) {
            send(new GetLobbies());
        }
        else {
            showAlert("This nickname is not valid! Try again.", Alert.AlertType.ERROR );
        }

    }

    @Override
    public void handleLobbies(Map<String, Integer> lobbies) {
        System.out.println("Handle lobbies!");
        Platform.runLater(() -> startLobbies(lobbies));
    }

    @Override
    public void handleLeaders() {
        Set<Integer> ids = modelView.getMyDashboard().getLeaderCards().keySet();
        selectLeaderController = new SelectLeaderController();
        selectLeaderController.setLeadersIds(ids);
        try {
            selectLeaderController.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        send(new SetNickname(nickname));
    }

    @Override
    public void setModelView(ModelView modelView) {
        this.modelView = modelView;
        network.getServerConnection().setModelView(modelView);
    }

    @Override
    public String getInput(String check) {
        return null;
    }

    @Override
    public void printText(String text) {
        System.out.println(text);
    }

    @Override
    public void send(ClientMessage message) {
        network.sendToServer(message);
    }

    @Override
    public void handleSoloActionToken() {

    }

    @Override
    public void showStats(Map<String, Integer> map) {

    }

    @Override
    public String getNickname() {
        return nickname;
    }


    /* Alerts */

    public void showAlert(String header, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setHeaderText(header);
        a.show();
    }

}
