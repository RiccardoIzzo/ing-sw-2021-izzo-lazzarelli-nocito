package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.events.clientmessages.CheckRequirement;
import it.polimi.ingsw.events.clientmessages.DiscardLeaderCard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class LeadersController {

    @FXML Button firstLeaderCardActivateButton;
    @FXML Button firstLeaderCardDiscardButton;
    @FXML Button secondLeaderCardActivateButton;
    @FXML Button secondLeaderCardDiscardButton;
    @FXML Pane leaderPane;
    @FXML ImageView firstLeaderCard;
    @FXML ImageView secondLeaderCard;

    Label firstActiveLabel;
    Label secondActiveLabel;

    Map<Integer, Boolean> leaderIds;
    private GUI gui;

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void setup(Map<Integer, Boolean> ids) {
        leaderIds = ids;
        System.out.println("leadersIds");
        System.out.println(leaderIds);
        if(ids.size() >= 1) {
            firstLeaderCard.setImage(new Image("/view/images/leaderCards/leaderCard" + leaderIds.keySet().toArray()[0] + ".png"));

        }
        else {
            firstLeaderCard.setImage(new Image("/view/images/cardBack.png"));
        }
        if(ids.size() == 2) {
            secondLeaderCard.setImage(new Image("/view/images/leaderCards/leaderCard" + leaderIds.keySet().toArray()[1] + ".png"));
        }
        else {
            secondLeaderCard.setImage(new Image("/view/images/cardBack.png"));
        }

        if(ids.size() >= 1 && (boolean) leaderIds.values().toArray()[0] ) {
            firstLeaderCardActivateButton.setDisable(true);
            firstLeaderCardDiscardButton.setDisable(true);

            firstActiveLabel = new Label("ACTIVE");
            firstActiveLabel.setLayoutX(firstLeaderCard.getLayoutX());
            firstActiveLabel.setLayoutY(firstLeaderCardActivateButton.getLayoutY()+firstLeaderCardActivateButton.getHeight()+10);
            firstActiveLabel.setTextFill(Color.web("green"));
            firstActiveLabel.setPrefWidth(90);
            firstActiveLabel.setPrefHeight(30);

            Platform.runLater(() -> leaderPane.getChildren().add(firstActiveLabel));
        }
        if(ids.size() == 2 && (boolean) leaderIds.values().toArray()[1] ) {
            secondLeaderCardActivateButton.setDisable(true);
            secondLeaderCardDiscardButton.setDisable(true);

            secondActiveLabel = new Label("ACTIVE");
            secondActiveLabel.setLayoutX(secondLeaderCard.getLayoutX());
            secondActiveLabel.setLayoutY(secondLeaderCardActivateButton.getLayoutY()+secondLeaderCardActivateButton.getHeight()+10);
            secondActiveLabel.setTextFill(Color.web("green"));
            secondActiveLabel.setPrefWidth(90);
            secondActiveLabel.setPrefHeight(30);

            Platform.runLater(() -> leaderPane.getChildren().add(secondActiveLabel));
        }
    }
    public void start() {

    }

    public void activateButtonClick(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(firstLeaderCardActivateButton)) {
            gui.send(new CheckRequirement((Integer) leaderIds.keySet().toArray()[0]));
        }
        else {
            gui.send(new CheckRequirement((Integer) leaderIds.keySet().toArray()[1]));
        }

        Stage stage = (Stage) leaderPane.getScene().getWindow();
        stage.close();
    }

    public void discardButtonClick(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(firstLeaderCardDiscardButton)) {
            gui.send(new DiscardLeaderCard((Integer) leaderIds.keySet().toArray()[0]));
        }
        else {
            gui.send(new DiscardLeaderCard((Integer) leaderIds.keySet().toArray()[1]));
        }
        gui.updateDashboard();
        Stage stage = (Stage) leaderPane.getScene().getWindow();
        stage.close();
    }
}
