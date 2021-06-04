package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class SelectLeaderController {
    public Button card1Button;
    public ImageView card1ImageView;
    public Button card2Button;
    public ImageView card2ImageView;
    public Button card3Button;
    public ImageView card3ImageView;
    public Button card4Button;
    public ImageView card4ImageView;
    public Button selectButton;
    Set<Integer> leaderIds;

    public void start() throws IOException {
        GUI.mainStage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scenes/sceneSelectLeaders.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void setLeadersIds(Set<Integer> ids) {
        leaderIds = ids;
    }

    public void selectButtonClicked(ActionEvent actionEvent) {
    }

    public void cardButtonClicked(ActionEvent actionEvent) {
    }
}
