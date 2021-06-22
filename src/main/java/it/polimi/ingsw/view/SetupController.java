package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.SetNickname;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SetupController {
    public Button connectButton;
    @FXML Pane connectPane;
    // Connect Scene
    @FXML Label serverLabel;
    @FXML TextField serverTextField;
    @FXML Label ipLabel;
    @FXML TextField ipTextField;
    @FXML Label nicknameLabel;
    @FXML TextField nicknameTextField;
    Button setNicknameButton;


    private static GUI gui;

    public void setGUI(GUI gui) {
        SetupController.gui = gui;
    }

    public void start() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scenes/sceneConnect.fxml"));
        Parent root = loader.load();

        gui.mainStage.setTitle("Master of Renaissance");
        gui.mainStage.setScene(new Scene(root));
        gui.mainStage.show();

    }
    public void setNicknameButtonClicked(ActionEvent actionEvent) {
        String nickname = nicknameTextField.getText();
        if ( nickname.length() < 1 ) {
            gui.showAlert("Nickname not valid!", Alert.AlertType.ERROR);
            return;
        }
        gui.setNickname(nickname);
        gui.send(new SetNickname(nickname));

//        gui.startLobbies(lobbies);

//        gui.send(new GetLobbies());

    }

    public void connectButtonClicked() {
        if(serverTextField.getText().length() < 1 || ipTextField.getText().length() < 1) {
            gui.showAlert("Error or IP not valid!", Alert.AlertType.ERROR);
            return;
        }

        gui.connect(serverTextField.getText(), Integer.parseInt(ipTextField.getText()));
        serverLabel.setOpacity(0);
        serverTextField.setOpacity(0);
        ipLabel.setOpacity(0);
        ipTextField.setOpacity(0);
        connectButton.setOpacity(0);
        connectButton.setManaged(false);



        nicknameLabel = new Label("Nickname");
        nicknameLabel.setAlignment(Pos.CENTER);
        nicknameLabel.setLayoutX(200);
        nicknameLabel.setLayoutY(175);
        nicknameLabel.setPrefWidth(200);
        nicknameLabel.setPrefHeight(30);

        nicknameTextField = new TextField();
        nicknameTextField.setAlignment(Pos.CENTER);
        nicknameTextField.setLayoutX(200);
        nicknameTextField.setLayoutY(205);
        nicknameTextField.setPrefWidth(200);
        nicknameTextField.setPrefHeight(20);
        nicknameTextField.setPromptText("Insert nickname");


        setNicknameButton = new Button();
        setNicknameButton.setText("Set nickname");
        setNicknameButton.setLayoutX(240);
        setNicknameButton.setLayoutY(325.0);
        setNicknameButton.setPrefWidth(120);
        setNicknameButton.setPrefHeight(30);
        setNicknameButton.setOnAction(this::setNicknameButtonClicked);

        Platform.runLater(() -> {


            connectPane.getChildren().add(setNicknameButton);
            connectPane.getChildren().add(nicknameLabel);
            connectPane.getChildren().add(nicknameTextField);
        });


    }

}
