package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class DashboardController {
    @FXML ChoiceBox<String> choiceBox;
    @FXML Pane dashboardPane;

    private static GUI gui;

    public void setGUI(GUI gui) {
        DashboardController.gui = gui;
    }

    public void showMarket() {
        showPopup("/view/scenes/sceneMarket.fxml");
    }

    public void showGrid() {
        showPopup("/view/scenes/sceneGrid.fxml");
    }

    private void showPopup(String scenePath) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(scenePath));
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(gui.mainStage);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
