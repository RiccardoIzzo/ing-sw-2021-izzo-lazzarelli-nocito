package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.GetLobbies;
import it.polimi.ingsw.events.clientmessages.SetNickname;
import it.polimi.ingsw.events.servermessages.ValidNickname;
import it.polimi.ingsw.model.token.MoveBlackMarkerToken;
import it.polimi.ingsw.model.token.RemoveCardsToken;
import it.polimi.ingsw.model.token.SoloActionToken;
import it.polimi.ingsw.network.NetworkHandler;
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
    DashboardController dashboardController;
    static String nickname;
    private ModelView modelView;

    private boolean reconnected = false;

    private int bonusResourceAmount = 0;
    boolean showTempShelf = false;

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
            for (Map.Entry<String, Integer> lobby : lobbies.entrySet()) {
                lobbiesController.lobbiesListView.getItems().add("[" + lobby.getValue() + " players] - " + lobby.getKey());
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
        if (message instanceof ValidNickname) {
            //Platform.runLater(() -> mainStage.close());
            send(new GetLobbies());
        } else {
            Platform.runLater(() -> showAlert("This nickname is not valid! Try again.", Alert.AlertType.ERROR));
        }

    }

    @Override
    public void handleLobbies(Map<String, Integer> lobbies) {
        if (lobbiesController == null) {
            Platform.runLater(() -> startLobbies(lobbies));
        } else {
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
            dashboardController.setModelView(modelView);
            dashboardController.setup();
            mainStage.setScene(new Scene(root));
            mainStage.setResizable(false);
            mainStage.show();

            mainStage.centerOnScreen();

            dashboardController.handleBonusResource(bonusResourceAmount);

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
        bonusResourceAmount = amount;
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
        dashboardController.showDashboard(nickname);
    }

    @Override
    public void handleTemporaryShelf() {
        dashboardController.handleTemporaryShelf();

    }

    @Override
    public void handleCheckRequirement(boolean result, int id) {
        dashboardController.handleLeaderCardActivation(result, id);
    }

    public void updateDashboard() {
        dashboardController.showDashboard(nickname);
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
        if (reconnected) {
            if (modelView.getMyDashboard() != null && modelView.getMyDashboard().getLeaderCards().keySet().size() != 2) {
                handleLeaders();
            } else if (modelView.getMyDashboard() != null && modelView.getMyDashboard().getLeaderCards().keySet().size() == 2) {
                handleGameStart();
            }
            reconnected = false;
        }
    }

    @Override
    public String getInput(String check) {
        return null;
    }

    @Override
    public void printText(String text) {
        handleTextMessage(text);
    }

    @Override
    public void send(ClientMessage message) {
        network.sendToServer(message);
    }

    @Override
    public void showStats(Map<String, Integer> map) {
        dashboardController.showStats(map);
    }

    public void handleEndGame() {
        if(modelView.getDashboards().size() > 1)
            dashboardController.showEndGameText();
        else {
            dashboardController.singlePlayerEnd(100);
        }
    }

    @Override
    public String getNickname() {
        return nickname;
    }


    /* Alerts */
    public void displayAlert(String header, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setHeaderText(header);
        a.show();
    }

    public void showAlert(String header, Alert.AlertType type) {
        Platform.runLater(() -> displayAlert(header, type));
    }

    public void handleTextMessage(String text) {
        System.out.println(text);
        if (text.indexOf(nickname) == 0 && text.contains("back online")) {
            reconnected = true;
            System.out.println("riconnesso");
        }

    }

    public void enableLobbies() {
        lobbiesController.enable();
    }

    /**
     * Method getValidAction returns a list of valid user action.
     * @return the list of actions.
     */

//    @Override
    public ArrayList<Action> getValidActions() {
        ArrayList<Action> actions = new ArrayList<>();
        for(Action action : Action.values()){
            if(action.enabled) actions.add(action);
        }
        return actions;
    }
    /**
     * Method startTurn at the beginning of the player turn re-enable all actions.
     */
    @Override
    public void startTurn(){
        for(Action action : Action.values()){
            action.enabled = true;
        }
        if(dashboardController != null) {
            dashboardController.startTurn();
        }
    }
    @Override
    public void basicActionPlayed(){
        Action.TAKE_RESOURCE.enabled = false;
        Action.BUY_CARD.enabled = false;
        Action.ACTIVATE_PRODUCTION.enabled = false;
    }

    @Override
    public void handleTurn() {
        if(dashboardController != null) {
            dashboardController.handleWaitingText();
            if (!modelView.getCurrPlayer().equals(nickname) && modelView.getDashboards().size() > 1) {
                dashboardController.endTurnButton.setDisable(true);
                dashboardController.showLeaders.setDisable(true);
                dashboardController.marketButton.setDisable(true);
                dashboardController.gridButton.setDisable(true);
            }
        }
    }

    public void handleToken(SoloActionToken token) {
        int index = 0;
        if(token instanceof RemoveCardsToken) {
            index = switch (((RemoveCardsToken) token).getColor()) {
                case BLUE -> 1;
                case GREEN -> 2;
                case PURPLE -> 3;
                case YELLOW -> 4;
            };
        }
        else if(token instanceof MoveBlackMarkerToken) {
            index = switch (((MoveBlackMarkerToken) token).getSteps()) {
                case +2 -> 5;
                case +1 -> 7;
                default -> 0;
            };
        }
        if (index > 0 && dashboardController != null) {
            dashboardController.showToken(index);
        }
    }

    public void handleDefeat() {
        dashboardController.singlePlayerEnd(0);

    }
}
