package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.ModelView;
import it.polimi.ingsw.view.View;
import javafx.application.Application;

import it.polimi.ingsw.events.clientmessages.ClientMessage;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Map;

/**
 * GUI class manages the game with a Graphical User Interface.
 */
public class GUI extends Application implements View {
    @FXML Label serverLabel;
    @FXML TextField serverTextField;
    @FXML Label ipLabel;
    @FXML TextField ipTextField;
    @FXML Label nicknameLabel;
    @FXML TextField nicknameTextField;
    @FXML Button setNicknameButton;
    @FXML Button connectButton;
    @FXML Group nicknameGroup;


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/scene1.fxml"));
        Parent root = loader.load();

        stage.setTitle("Master of Renaissance");
        stage.setScene(new Scene(root));
        stage.show();

//        Label label = new Label("lets play");
//        VBox vBox = new VBox();
//        Scene scene = new Scene(vBox, 800, 600);
//        scene.setCursor(Cursor.CROSSHAIR);
//
//
//        stage.setScene(scene);
//
//        stage.setOnCloseRequest((event) -> System.out.println("Closing Stage"));
//        stage.show();



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
    public void handleTurn() {

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
    }

    public void connectButtonClicked(ActionEvent actionEvent) {
        serverLabel.setOpacity(0);
        serverTextField.setOpacity(0);
        ipLabel.setOpacity(0);
        ipTextField.setOpacity(0);
        connectButton.setOpacity(0);

        nicknameGroup.setOpacity(1);
        setNicknameButton.setOpacity(1);

    }

}
