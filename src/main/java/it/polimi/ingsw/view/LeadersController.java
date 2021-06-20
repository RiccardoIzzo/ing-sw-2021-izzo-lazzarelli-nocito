package it.polimi.ingsw.view;

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
    String playerNickname;
    private GUI gui;

    public void setModelView(ModelView modelView) {
        this.modelView = modelView;
    }

    private ModelView modelView;

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void setup(Map<Integer, Boolean> ids, String nickname) {
        leaderIds = ids;
        playerNickname = nickname;

        if (gui.getValidActions().contains(Action.ACTIVATE_LEADER)) {
            firstLeaderCardActivateButton.setDisable(false);
            secondLeaderCardActivateButton.setDisable(false);
        }
        else{
            firstLeaderCardActivateButton.setDisable(true);
            secondLeaderCardActivateButton.setDisable(true);
        }
        if (gui.getValidActions().contains(Action.DISCARD_LEADER)) {
            firstLeaderCardDiscardButton.setDisable(false);
            secondLeaderCardDiscardButton.setDisable(false);
        }
        else{
            firstLeaderCardDiscardButton.setDisable(true);
            secondLeaderCardDiscardButton.setDisable(true);
        }

        if(ids.size() >= 1 && (playerNickname.equals(gui.getNickname()) || (boolean) leaderIds.values().toArray()[0])) {
            firstLeaderCard.setImage(new Image("/view/images/leaderCards/leaderCard" + leaderIds.keySet().toArray()[0] + ".png"));

        }
        else {
            firstLeaderCard.setImage(new Image("/view/images/cardBack.png"));
            firstLeaderCardActivateButton.setDisable(true);
            firstLeaderCardDiscardButton.setDisable(true);
        }
        if(ids.size() == 2 && (playerNickname.equals(gui.getNickname()) || (boolean) leaderIds.values().toArray()[1])) {
            secondLeaderCard.setImage(new Image("/view/images/leaderCards/leaderCard" + leaderIds.keySet().toArray()[1] + ".png"));
        }
        else {
            secondLeaderCard.setImage(new Image("/view/images/cardBack.png"));
            secondLeaderCardDiscardButton.setDisable(true);
            secondLeaderCardActivateButton.setDisable(true);
        }

        if(ids.size() >= 1 && (boolean) leaderIds.values().toArray()[0] ) {
            firstLeaderCardActivateButton.setDisable(true);
            firstLeaderCardDiscardButton.setDisable(true);

            firstActiveLabel = new Label("ACTIVE");
            firstActiveLabel.setLayoutX(firstLeaderCard.getLayoutX()+5);
            firstActiveLabel.setLayoutY(firstLeaderCardActivateButton.getLayoutY()+firstLeaderCardActivateButton.getHeight()+10);
            firstActiveLabel.setTextFill(Color.web("green"));
            firstActiveLabel.setPrefWidth(90);
            firstActiveLabel.setPrefHeight(30);

            if (modelView.getCurrPlayer().equals(gui.getNickname()) && modelView.getMyDashboard().getAvailableProduction().contains((Integer)leaderIds.keySet().toArray()[0])) {
                Button developmentButton = new Button("Enable");
                developmentButton.setLayoutX(firstLeaderCardDiscardButton.getLayoutX());
                developmentButton.setLayoutY(firstLeaderCardDiscardButton.getLayoutY()+firstLeaderCardDiscardButton.getHeight()+10);
                developmentButton.setPrefWidth(firstLeaderCardDiscardButton.getWidth());
                developmentButton.setPrefHeight(firstLeaderCardDiscardButton.getHeight());
                developmentButton.setOnAction(event -> {
                    developmentButton.setText("EnableDisable".replace(developmentButton.getText(), ""));
                    gui.dashboardController.activateProduction(5);
                });
                developmentButton.setId("developmentButton5");
                Platform.runLater(() -> {
                    leaderPane.getChildren().remove(leaderPane.lookup("#developmentButton5"));
                    leaderPane.getChildren().add(developmentButton);

                });
            }

            Platform.runLater(() -> leaderPane.getChildren().add(firstActiveLabel));



        }
        if(ids.size() == 2 && (boolean) leaderIds.values().toArray()[1] ) {
            secondLeaderCardActivateButton.setDisable(true);
            secondLeaderCardDiscardButton.setDisable(true);

            secondActiveLabel = new Label("ACTIVE");
            secondActiveLabel.setLayoutX(secondLeaderCard.getLayoutX()+5);
            secondActiveLabel.setLayoutY(secondLeaderCardActivateButton.getLayoutY()+secondLeaderCardActivateButton.getHeight()+10);
            secondActiveLabel.setTextFill(Color.web("green"));
            secondActiveLabel.setPrefWidth(90);
            secondActiveLabel.setPrefHeight(30);

            if (modelView.getCurrPlayer().equals(gui.getNickname()) && modelView.getMyDashboard().getAvailableProduction().contains((Integer)leaderIds.keySet().toArray()[1])) {
                Button developmentButton = new Button("Enable");
                developmentButton.setLayoutX(secondLeaderCardDiscardButton.getLayoutX());
                developmentButton.setLayoutY(secondLeaderCardDiscardButton.getLayoutY()+secondLeaderCardDiscardButton.getHeight()+10);
                developmentButton.setPrefWidth(secondLeaderCardDiscardButton.getWidth());
                developmentButton.setPrefHeight(secondLeaderCardDiscardButton.getHeight());
                developmentButton.setOnAction(event -> {
                    developmentButton.setText("EnableDisable".replace(developmentButton.getText(), ""));
                    gui.dashboardController.activateProduction(6);
                });
                developmentButton.setId("developmentButton6");
                Platform.runLater(() -> {
                    leaderPane.getChildren().remove(leaderPane.lookup("#developmentButton6"));
                    leaderPane.getChildren().add(developmentButton);

                });
            }

            Platform.runLater(() -> leaderPane.getChildren().add(secondActiveLabel));
        }
    }
    public void start() {
        if(!modelView.getCurrPlayer().equals(gui.getNickname()) && modelView.getDashboards().size() > 1) {
            disableButtons();
        }
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

    public void disableButtons() {
        firstLeaderCardActivateButton.setDisable(true);
        secondLeaderCardActivateButton.setDisable(true);
        firstLeaderCardDiscardButton.setDisable(true);
        secondLeaderCardDiscardButton.setDisable(true);

    }
}
