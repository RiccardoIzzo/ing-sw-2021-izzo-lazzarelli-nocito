package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.events.clientmessages.GetLobbies;
import it.polimi.ingsw.events.clientmessages.SetNickname;
import it.polimi.ingsw.events.servermessages.ValidNickname;
import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.view.ModelView;
import it.polimi.ingsw.view.View;
import javafx.application.Application;

import it.polimi.ingsw.events.clientmessages.ClientMessage;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * GUI class manages the game with a Graphical User Interface.
 */
public class GUI extends Application implements View {
    Stage mainStage;
    private NetworkHandler network;
    SetupController setupController;
    LobbiesController lobbiesController;
    SelectLeaderController selectLeaderController;
    MarketController marketController;
    GridController gridController;
    DashboardController dashboardController;
    static String nickname;
    private ModelView modelView;




    @Override
    public void start(Stage stage) throws Exception {
        //actionHandler = new ActionHandler(this);
        mainStage = stage;
        setupController = new SetupController();
        setupController.setGUI(this);
        setupController.start();

    }
    public void setNickname(String name) {
        nickname = name;
    }
    public void connect(String ip, int port) {
        network = new NetworkHandler(ip, port, this);
        network.setConnection();
    }
    public void startLobbies(Map<String, Integer> lobbies) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scenes/sceneLobbies.fxml"));

        try {
            Parent root = loader.load();
//            Stage stage = new Stage();
            mainStage.setScene(new Scene(root));
            mainStage.show();

            lobbiesController = loader.getController();
            lobbiesController.setGUI(this);
            lobbiesController.setLobbies(lobbies);
            for(Map.Entry<String,Integer> lobby : lobbies.entrySet()) {
                lobbiesController.lobbiesListView.getItems().add("["+lobby.getValue()+" players] - " + lobby.getKey());
            }
            lobbiesController.lobbiesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

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
            Platform.runLater(() -> mainStage.close());
            send(new GetLobbies());
        }
        else {
            Platform.runLater(() ->  showAlert("This nickname is not valid! Try again.", Alert.AlertType.ERROR ));
        }

    }

    @Override
    public void handleLobbies(Map<String, Integer> lobbies) {
        if (lobbiesController == null ) {
            Platform.runLater(() -> startLobbies(lobbies));
        }
        else {
            Platform.runLater(() -> lobbiesController.refreshLobbies(lobbies));
        }
    }

    public void handleGameStart() {
        Platform.runLater(this::startGame);
    }

    public void startGame() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scenes/sceneDashboard.fxml"));
        Parent root;
        try {
            root = loader.load();
//            Stage stage = new Stage();
            dashboardController = loader.getController();
            dashboardController.setGUI(this);
            mainStage.setScene(new Scene(root));
            mainStage.show();

            mainStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startLeaders() {
        Set<Integer> ids = modelView.getMyDashboard().getLeaderCards().keySet();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scenes/sceneSelectLeaders.fxml"));
        Parent root;
        try {
            root = loader.load();
//            Stage stage = new Stage();
            mainStage.setScene(new Scene(root));
            mainStage.show();
            selectLeaderController = loader.getController();
            selectLeaderController.setGUI(this);
            selectLeaderController.setLeadersIds(new ArrayList<>(ids));
            selectLeaderController.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLobbyJoined() {
        lobbiesController.addWaitingView("Lobby created! Waiting for other players ...");
    }
    @Override
    public void handleLeaders() {
            Platform.runLater(this::startLeaders);

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
