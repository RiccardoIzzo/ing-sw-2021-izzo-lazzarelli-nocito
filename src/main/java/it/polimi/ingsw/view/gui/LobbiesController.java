package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Map;

public class LobbiesController {
    // Lobbies Scene
    @FXML VBox lobbiesVBox;
    @FXML TextField lobbyTextField;
    @FXML TextField numPlayersTextField;

    Map<String, Integer> lobbies;
    Scene scene;
    LobbiesController(Map<String,Integer> lobbies) {
        this.lobbies = lobbies;
    }
    public void start(Scene scene) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scenes/scene2.fxml"));
        Parent root = loader.load();

        GUI.mainStage.close();
        GUI.mainStage.setScene(new Scene(root));
        GUI.mainStage.show();

//        lobbiesVBox.getChildren().removeAll();
            for(Map.Entry<String,Integer> lobby : lobbies.entrySet()) {
                Label lobbyLabel = new Label();
                lobbyLabel.setText(lobby.getKey());
//                lobbiesVBox.setAlignment(Pos.CENTER);
//                lobbiesVBox.getChildren().add(lobbyLabel);
            }
//            handleLobbies(lobbies);

    }
    public void joinButtonClicked() {
    }

    public void createButtonClicked() {
    }
}
