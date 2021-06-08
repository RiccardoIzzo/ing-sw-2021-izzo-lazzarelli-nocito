package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.events.clientmessages.GetLobbies;
import it.polimi.ingsw.events.clientmessages.SetNickname;
import it.polimi.ingsw.network.NetworkHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetupController {
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

        gui.mainStage.close();
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

        nicknameGroup.setOpacity(1);
        setNicknameButton.setOpacity(1);

    }

}
