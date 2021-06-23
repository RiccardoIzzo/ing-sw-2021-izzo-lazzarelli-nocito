package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.CheckRequirement;
import it.polimi.ingsw.events.clientmessages.DiscardLeaderCard;
import it.polimi.ingsw.model.Resource;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeadersController {

    @FXML Button firstLeaderCardActivateButton;
    @FXML Button firstLeaderCardDiscardButton;
    @FXML Button secondLeaderCardActivateButton;
    @FXML Button secondLeaderCardDiscardButton;
    @FXML Pane leaderPane;
    @FXML ImageView firstLeaderCard;
    @FXML ImageView secondLeaderCard;

    Button[] activateButton = new Button[2];
    Button[] discardButton = new Button[2];
    ImageView[] leaderCardImageView = new ImageView[2];

    Label[] activeLabels;

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
        activateButton[0] = firstLeaderCardActivateButton;
        activateButton[1] = secondLeaderCardActivateButton;
        discardButton[0] = firstLeaderCardDiscardButton;
        discardButton[1] = secondLeaderCardDiscardButton;
        leaderCardImageView[0] = firstLeaderCard;
        leaderCardImageView[1] = secondLeaderCard;

        gui.dashboardController.leaderExtraProductionRes = new HashMap<>();

        ArrayList<Action> validActions = gui.getValidActions();
        for(int i =0; i<leaderCardImageView.length; i++) {
            activateButton[i].setDisable(!(validActions.contains(Action.ACTIVATE_LEADER)));
            discardButton[i].setDisable(!(validActions.contains(Action.DISCARD_LEADER)));
            if(ids.size() >= 1+i && (playerNickname.equals(gui.getNickname()) || (boolean) leaderIds.values().toArray()[i])) {
                leaderCardImageView[i].setImage(new Image("/view/images/leaderCards/leaderCard" + leaderIds.keySet().toArray()[i] + ".png"));

            }
            else {
                leaderCardImageView[i].setImage(new Image("/view/images/cardBack.png"));
                activateButton[i].setDisable(true);
                discardButton[i].setDisable(true);
            }
        }


        activeLabels = new Label[2];

        for(int i=0; i<leaderCardImageView.length; i++) {

            if(ids.size() >= 1 && leaderIds.values().size() >= i && (boolean) leaderIds.values().toArray()[i] ) {
                activateButton[i].setDisable(true);
                discardButton[i].setDisable(true);
                activeLabels[i] = new Label("ACTIVE");
                activeLabels[i].setLayoutX(leaderCardImageView[i].getLayoutX() + 5);
                activeLabels[i].setLayoutY(firstLeaderCardActivateButton.getLayoutY() + firstLeaderCardActivateButton.getHeight() + 10);
                activeLabels[i].setTextFill(Color.web("green"));
                activeLabels[i].setPrefWidth(90);
                activeLabels[i].setPrefHeight(30);


                int finalI = i;
                int id = (Integer) leaderIds.keySet().toArray()[i];
                if ((modelView.getCurrPlayer().equals(gui.getNickname()) || modelView.getCurrPlayer().length()<1) && id >=209 && id <=212 && modelView.getMyDashboard().getAvailableProduction().contains(id)) {

                    ComboBox<ImageView> basicProductionRes;
                    basicProductionRes = new ComboBox<>();
                    basicProductionRes.setLayoutX(leaderCardImageView[i].getLayoutX()+leaderCardImageView[i].getFitWidth()/2-8);
                    basicProductionRes.setLayoutY(leaderCardImageView[i].getLayoutY()+leaderCardImageView[i].getFitHeight()-55);
                    int len = 27;
                    basicProductionRes.setMaxWidth(len);
                    basicProductionRes.setMaxHeight(len);
                    Platform.runLater(() -> leaderPane.getChildren().add(basicProductionRes));
                    String[] resources = {"coinLeader", "servantLeader" , "shieldLeader" , "stoneLeader"};
                    Resource[] res = {Resource.COIN, Resource.SERVANT, Resource.SHIELD, Resource.STONE};
                    basicProductionRes.setStyle("-fx-opacity: 1; -fx-background-color: transparent; ");
                    for (String resource : resources) {
                        Image resourceImage = new Image("/view/images/resources/" + resource + ".png");
                        ImageView resourceImageView = new ImageView(resourceImage);
                        resourceImageView.setFitWidth(len);
                        resourceImageView.setFitHeight(len);
                        basicProductionRes.getItems().add(resourceImageView);
                    }
                    basicProductionRes.setCellFactory(listView -> new ListCell<>() {

                        private final ImageView imageView = new ImageView();

                        @Override
                        public void updateItem(ImageView item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                String imageURL = item.getImage().getUrl();
                                Image image = new Image(imageURL, true); // true means load in background
                                imageView.setImage(image);
                                imageView.setFitWidth(len);
                                imageView.setFitHeight(len);
                                setGraphic(imageView);
                            }
                        }
                    });


                    Button extraDevButton = new Button("Enable");
                    extraDevButton.setLayoutX(discardButton[i].getLayoutX());
                    extraDevButton.setLayoutY(discardButton[i].getLayoutY() + discardButton[i].getHeight() + 10);
                    extraDevButton.setPrefWidth(discardButton[i].getWidth());
                    extraDevButton.setPrefHeight(discardButton[i].getHeight());
                    extraDevButton.setOnAction(event -> {
                        int index = basicProductionRes.getSelectionModel().getSelectedIndex();
                        if(index >= 0) {
                            if(basicProductionRes.isDisabled()) {
                                basicProductionRes.setDisable(false);
                                basicProductionRes.setStyle("-fx-opacity: 1;");
                                extraDevButton.setText("Enable");
                                gui.dashboardController.activateProduction(5 + finalI);
                                gui.dashboardController.leaderExtraProductionRes.remove(finalI, res[index]);
                            }
                            else {
                                    basicProductionRes.setDisable(true);
                                extraDevButton.setText("Disable");
                                    gui.dashboardController.activateProduction(5 + finalI);
                                    gui.dashboardController.leaderExtraProductionRes.put(finalI, res[index]);
                            }
                        }
                        else {
                            gui.showAlert("Select the output resource!", Alert.AlertType.ERROR);
                        }


                    });
                    Platform.runLater(() -> leaderPane.getChildren().add(extraDevButton));

                }

                Platform.runLater(() -> leaderPane.getChildren().add(activeLabels[finalI]));
            }
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
