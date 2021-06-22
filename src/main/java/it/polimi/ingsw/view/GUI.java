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
 * @author Andrea Nocito
 */
public class GUI extends Application implements View {

    /**
     * GUI also handles SelectLeader Connect, Lobbies and Dashboard controllers.
     */
    SetupController setupController;
    LobbiesController lobbiesController;
    SelectLeaderController selectLeaderController;
    DashboardController dashboardController;

    Stage mainStage;
    private NetworkHandler network;
    private ModelView modelView;
    static String nickname;
    private boolean reconnected = false;
    private int bonusResourceAmount = 0;

    /**
     * Method start sets up the stage and sets up setupController
     * @param stage mainStage of the game
     */
    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        setupController = new SetupController();
        setupController.setGUI(this);
        setupController.start();

    }

    /**
     * Method setNickname is called by setupController to set the player's nickname
     * @param name player's nickname
     */
    public void setNickname(String name) {
        nickname = name;
    }

    /**
     * Method connect is called by setupController to start the connection with the specified server.
     * @param ip, port
     */
    public void connect(String ip, int port) {
        network = new NetworkHandler(ip, port, this);
        network.setConnection();
    }

    /**
     * Method startLobbies is called by setupLobbies and sets up lobbiesController
     * @param lobbies map that associates the lobby id to the maximum number of players for that lobby.
     */
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

    /**
     * GUI main method.
     * @param args main args.
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Method handleNickname manages the two possible answers to a SetNickname message, ValidNickname and InvalidNickname.
     * @param message ServerMessage to handle.
     */
    @Override
    public void handleNickname(ServerMessage message) {
        if (message instanceof ValidNickname) {
            //Platform.runLater(() -> mainStage.close());
            send(new GetLobbies());
        } else {
            Platform.runLater(() -> showAlert("This nickname is not valid! Try again.", Alert.AlertType.ERROR));
        }

    }

    /**
     * Method handleLobbies manages the lobby system.
     * It checks if lobbiesController has already been initialized and either calls startLobbies
     * or lobbiesController's method refreshLobbies to just refresh the list of available lobbies.
     * @param lobbies map that associates the lobby id to the maximum number of players for that lobby.
     */
    @Override
    public void handleLobbies(Map<String, Integer> lobbies) {
        if (lobbiesController == null) {
            Platform.runLater(() -> startLobbies(lobbies));
        } else {
            Platform.runLater(() -> lobbiesController.refreshLobbies(lobbies));
        }
    }

    /**
     * Method startGame is called by method setModelView and sets up dashboardController
    */
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


    /**
     * Method startLeaders is called by method handleLeaders and sets up selectLeaderController
     */
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


    /**
     * Method handleLobbyJoined is called by ActionHandler and makes lobbiesController display a waiting message
     * that confirms to the users that the lobby has been created.
     */
    public void handleLobbyJoined() {
        lobbiesController.addWaitingView("Lobby created! Waiting for other players ...");
    }

    /**
     * Method handleLobbyJoined is called by ActionHandler and calls startLeaders to set up selectLeadersController.
     */
    @Override
    public void handleLeaders() {
        Platform.runLater(this::startLeaders);

    }

    /**
     * Method handleLobbyJoined is called by ActionHandler and calls startLeaders to set up selectLeadersController.
     * @param amount an integer containing the numbers of bonus resources available for the player.
     */
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

    /**
     * Method handleDiscardLeader is called by ActionHandler and makes dashboardController refresh dashboard
     * to show temporary shelf and let the player organize its resources
     */
    @Override
    public void handleTemporaryShelf() {
        dashboardController.handleTemporaryShelf();

    }

    /**
     *  Method handleDiscardLeader is called by ActionHandler and manages CheckRequirement message.
     * @param result outcome of the requirement check.
     * @param id card id.
     */
    @Override
    public void handleCheckRequirement(boolean result, int id) {
        dashboardController.handleLeaderCardActivation(result, id);
    }
    /**
     *  Method updateDashboard is called by scene controllers to show the results of played actions
     */
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


    /**
     * Method setModelView sets the modelView for gui, controllers and network.
     * @param modelView the modelView.
     */
    @Override
    public void setModelView(ModelView modelView) {
        this.modelView = modelView;
        network.getServerConnection().setModelView(modelView);
        if (reconnected) {
            if (modelView.getMyDashboard() != null && modelView.getMyDashboard().getLeaderCards().keySet().size() != 2) {
                handleLeaders();
            } else if (modelView.getMyDashboard() != null && modelView.getMyDashboard().getLeaderCards().keySet().size() == 2) {
                Platform.runLater(this::startGame);
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

    /**
     * Method send dispatch a message to the server.
     * @param message the ClientMessage to send.
     */
    @Override
    public void send(ClientMessage message) {
        network.sendToServer(message);
    }

    /**
     * Method showStats calls dashboardController method to set up gameOverController
     * and show the statics at the end of the game.
     * @param gameStats a map containing the statistics.
     */
    @Override
    public void showStats(Map<String, Integer> gameStats) {
        dashboardController.showStats(gameStats);
    }

    /**
     *  Method handleEndGame is called by ActionHandler and sets up the final turn of the game.
     */
    public void handleEndGame() {
        if(modelView.getDashboards().size() > 1)
            dashboardController.showEndGameText();
        else {
            dashboardController.singlePlayerEnd(100);
        }
    }

    /**
     * Method getNickname returns the player's nickname.
     * @return player's nickname.
     */
    @Override
    public String getNickname() {
        return nickname;
    }


    /**
     * Method displayAlert is called by showAlert to display the desired alert
     */
    public void displayAlert(String header, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setHeaderText(header);
        a.show();
    }

    /**
     * Method showAlert is called by gui and controllers to show an alert to the player
     * @param header the text of the alert
     * @param type the type of the alert (error, info etc.)
     */
    public void showAlert(String header, Alert.AlertType type) {
        Platform.runLater(() -> displayAlert(header, type));
    }

    /**
     * Method handleTextMessage prints the given text on the terminal.
     * @param text text to print.
     */
    public void handleTextMessage(String text) {
        System.out.println(text);
        if (text.indexOf(nickname) == 0 && text.contains("back online")) {
            reconnected = true;
            System.out.println("riconnesso");
        }

    }


    /**
     *  Method enableLobbies is called by ActionHandler and resets lobbiesController
     *  after an unsuccessful try to join or create a lobby
     */
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


    /**
     * Method basicActionPlayed disables the three mutually exclusive basic actions.
     */
    @Override
    public void basicActionPlayed(){
        Action.TAKE_RESOURCE.enabled = false;
        Action.BUY_CARD.enabled = false;
        Action.ACTIVATE_PRODUCTION.enabled = false;
    }

    /**
     * Method handleTurn manages a player's turn and the available user actions.
     * It calls method handleWaitingText of dashboardController to show the user the current turn player, and
     * if it's an opponent's turn it disables all the actions available only during a player's own turn.
     */
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

    /**
     *  Method handleToken is called by ActionHandler and calls the method showToken of dashboardController
     *  to show the player which token has been drawn.
     */
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

    /**
     *  Method handleDefeat is called by ActionHandler when the single player game has ended
     *  with a defeat, and calls singlePlayerEnd method of dashboardController to set up gameOverController.
     */
    public void handleDefeat() {
        dashboardController.singlePlayerEnd(0);

    }
}
