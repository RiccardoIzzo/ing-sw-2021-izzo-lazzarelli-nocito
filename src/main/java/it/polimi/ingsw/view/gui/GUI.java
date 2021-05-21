package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.View;
import javafx.application.Application;

import it.polimi.ingsw.events.clientmessages.ClientMessage;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Master of Renaissance");
        Label label = new Label("lets play");
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 800, 600);
        scene.setCursor(Cursor.CROSSHAIR);


        stage.setScene(scene);

        stage.setOnCloseRequest((event) -> System.out.println("Closing Stage"));
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
    public void setNickname() {

    }

    @Override
    public void printText(String text) {

    }

    @Override
    public void send(ClientMessage message) {

    }

    @Override
    public void selectBonusResource(int amount) {

    }
}
