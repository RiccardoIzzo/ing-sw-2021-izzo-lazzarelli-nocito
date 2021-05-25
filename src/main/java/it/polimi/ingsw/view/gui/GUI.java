package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.ModelView;
import it.polimi.ingsw.view.View;
import javafx.application.Application;

import it.polimi.ingsw.events.clientmessages.ClientMessage;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * GUI class manages the game with a Graphical User Interface.
 */
public class GUI extends Application implements View {
    // Connect Scene
    @FXML Label serverLabel;
    @FXML TextField serverTextField;
    @FXML Label ipLabel;
    @FXML TextField ipTextField;
    @FXML Label nicknameLabel;
    @FXML TextField nicknameTextField;
    @FXML Button setNicknameButton;
    @FXML Button connectButton;
    @FXML Group nicknameGroup;

    // Lobbies Scene
    @FXML VBox lobbiesVBox;
    @FXML TextField lobbyTextField;
    @FXML TextField numPlayersTextField;

    // Grid Scene
    public ImageView card1ImageView;
    public Button card1Button;
    public ImageView cardSelectedImageView;
    public Button buyCardButton;

    // Market Scene
    public Button firstColumnButton;
    public Button secondColumnButton;
    public Button thirdColumnButton;
    public Button fourthColumnButton;
    public Button firstRowButton;
    public Button secondRowButton;
    public Button thirdRowButton;

    //Game over Scene
    public ListView<String> pointsListView;
    public ListView<Integer> playersListView;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/scene3a.fxml"));
        Parent root = loader.load();

        stage.setTitle("Master of Renaissance");
        stage.setScene(new Scene(root));
        stage.show();
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
    public void setNicknameButtonClicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/scene2.fxml"));
        Parent root;
        try {
            Scene scene = ((Node) actionEvent.getSource()).getScene();
            root = loader.load();
            scene.setRoot(root);

            Map<String, Integer> lobbies = new HashMap<>();
            lobbies.put("lobby1", 2);
            lobbies.put("lobby3", 2);
            lobbies.put("lobby2r", 2);
            for(Map.Entry<String,Integer> lobby : lobbies.entrySet()) {
                Label lobbyLabel = new Label();
                lobbyLabel.setText(lobby.getKey());
                lobbiesVBox.setAlignment(Pos.CENTER);
                lobbiesVBox.getChildren().add(lobbyLabel);
            }
//            handleLobbies(lobbies);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void connectButtonClicked() {
        serverLabel.setOpacity(0);
        serverTextField.setOpacity(0);
        ipLabel.setOpacity(0);
        ipTextField.setOpacity(0);
        connectButton.setOpacity(0);

        nicknameGroup.setOpacity(1);
        setNicknameButton.setOpacity(1);

    }


    public void joinButtonClicked() {
    }

    public void createButtonClicked() {
    }

    public void columnButtonClicked(ActionEvent actionEvent) {
        Button arrowButton = (Button) actionEvent.getSource();
        System.out.println(arrowButton.getId());

    }

    public void rowButtonClicked(ActionEvent actionEvent) {
        Button arrowButton = (Button) actionEvent.getSource();
        System.out.println(arrowButton.getId());
    }

    public void cardButtonClicked() {
    }
}
