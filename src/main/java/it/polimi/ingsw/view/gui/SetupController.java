package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public void start() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scenes/scene1.fxml"));
        Parent root = loader.load();

        GUI.mainStage.setTitle("Master of Renaissance");
        GUI.mainStage.setScene(new Scene(root));
        GUI.mainStage.show();
    }
    public void setNicknameButtonClicked(ActionEvent actionEvent) {

        Map<String, Integer> lobbies = new HashMap<>();
        lobbies.put("lobby1", 2);
        lobbies.put("lobby3", 2);
        lobbies.put("lobby2r", 2);

//        openLobbyList(((Node) actionEvent.getSource()).getScene(), lobbies);
        GUI.startLobbies(((Node) actionEvent.getSource()).getScene(), lobbies);

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

}
