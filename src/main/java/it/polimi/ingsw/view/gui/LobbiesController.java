package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.events.clientmessages.CreateLobby;
import it.polimi.ingsw.events.clientmessages.GetLobbies;
import it.polimi.ingsw.events.clientmessages.JoinLobby;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class LobbiesController {
    public ListView<String> lobbiesListView;
    public VBox lobbiesNumberVBox;
    // Lobbies Scene
    @FXML TextField lobbyTextField;
    @FXML TextField numPlayersTextField;

    Map<String, Integer> lobbies;
    public void setLobbies(Map<String,Integer> lobbies) {
        this.lobbies = lobbies;
    }

    public void start() throws IOException {
        GUI.mainStage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scenes/scene2.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        LobbiesController controller = loader.getController();

            for(Map.Entry<String,Integer> lobby : lobbies.entrySet()) {
                controller.lobbiesListView.getItems().add("["+lobby.getValue()+" players] - " + lobby.getKey());
            }
        controller.lobbiesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }
    public void joinButtonClicked() {
        String selectedItem = lobbiesListView.getSelectionModel().getSelectedItem();
        String lobbyID = selectedItem.substring(selectedItem.indexOf(" - ")+3);
        System.out.println("LobbyID: " + lobbyID);

    }

    public void createButtonClicked() {
        System.out.println(lobbyTextField.getText() + " - " + numPlayersTextField.getText());
    }
}
