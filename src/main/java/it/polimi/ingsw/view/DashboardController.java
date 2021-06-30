package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.model.JsonCardsCreator;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.ProductionLeaderCard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * DashboardController class manages the dashboard scene.
 *
 * @author Andrea Nocito
 */
public class DashboardController {
    /**
     * DashboardControllers handles Market, Grid, GameOver and Leaders controllers.
     */
    MarketController marketController;
    GridController gridController;
    LeadersController leadersController;
    GameOverController gameOverController;

    private static GUI gui;
    private ModelView modelView;

    @FXML Button showLeaders;
    @FXML Button activateProductionsButton;
    @FXML Button marketButton;
    @FXML Button gridButton;
    @FXML ChoiceBox<String> playersChoiceBox;
    @FXML Pane dashboardPane;
    @FXML Button endTurnButton;

    ImageView firstShelfRes1View;
    ImageView secondShelfRes1View;
    ImageView secondShelfRes2View;
    ImageView thirdShelfRes1View;
    ImageView thirdShelfRes2View;
    ImageView thirdShelfRes3View;
    ImageView[] ShelfView = new ImageView[6];

    ImageView firstExtraShelfResView1;
    ImageView firstExtraShelfResView2;
    ImageView secondExtraShelfResView1;
    ImageView secondExtraShelfResView2;
    ImageView[] ExtraShelfView = new ImageView[4];

    Integer swapIndex = -1;
    boolean showTempShelf = false;
    ImageView temporaryShelfImage;
    Button tempShelfButton;
    ImageView tempShelfImageView1;
    ImageView tempShelfImageView2;
    ImageView tempShelfImageView3;
    ImageView tempShelfImageView4;
    ImageView[] tempShelfImageViews = new ImageView[4];

    ImageView faithTrackImage;
    ImageView blackFaithTrackImage;
    ImageView[] popeFavorTilesImages = new ImageView[3];

    ImageView developmentImageSlot1;
    ImageView developmentImageSlot2;
    ImageView developmentImageSlot3;
    ImageView[] developmentImage = new ImageView[3];
    Button[] developmentButton = new Button [4];

    Resource[] basicProductionResources = new Resource[3];
    ArrayList<Integer> enabledProductions = new ArrayList<>();
    @FXML ComboBox<ImageView> basicProductionRes1;
    @FXML ComboBox<ImageView> basicProductionRes2;
    @FXML ComboBox<ImageView> basicProductionRes3;
    ArrayList<ComboBox<ImageView>> basicProductionResImages = new ArrayList<>();
    Map<Integer, Resource> leaderExtraProductionRes;

    ImageView[] strongboxResourceView = new ImageView[4];
    Label[] amountLabel = new Label [4];

    ImageView tokenImageView;

    /**
     * Method setGUI sets up the GUI for the DashboardController.
     * @param gui GUI reference.
     */
    public void setGUI(GUI gui) {
        DashboardController.gui = gui;
    }

    /**
     * Method setup manages dashboard elements before showing the scene.
     * It adds users nicknames to playersChoiceBox, sets up basic production
     * and calls setupArrays and handleWaitingText
     */
    public void setup() {
        amountLabel = new Label[4];
        leaderExtraProductionRes = new HashMap<>();

        for(ModelView.DashboardView dashboard : modelView.getDashboards()) {
            playersChoiceBox.getItems().add(dashboard.getNickname());
        }
        setupArrays();
        playersChoiceBox.setValue(gui.getNickname());

        endTurnButton.setDisable(true);
        showLeaders.setDisable(false);

        updatePlayerDashboard();
        if(modelView.getDashboards().size() == 1)
            showToken(0);

        setupBasicProduction();
        handleWaitingText();
    }

     /**
     * Method setupBasicProduction manages the combobox for the selection of the basic production resources,
     * activateProductionsButton and the array of enabled productions.
     */
    public void setupBasicProduction() {

        basicProductionResources = new Resource[3];
        activateProductionsButton.setDisable(true);
        enabledProductions = new ArrayList<>();
        String[] resources = {"coinLeader", "servantLeader", "shieldLeader", "stoneLeader"};
        for(ComboBox<ImageView> basicProduction : basicProductionResImages) {
            basicProduction.setStyle("-fx-opacity: 1; -fx-background-color: transparent; ");
        }
        for (String resource : resources) {
            Image resourceImage = new Image("/view/images/resources/" + resource + ".png");
            for(ComboBox<ImageView> basicProduction : basicProductionResImages) {
                ImageView resourceImageView = new ImageView(resourceImage);
                resourceImageView.setFitWidth(25);
                resourceImageView.setFitHeight(25);
                basicProduction.getItems().add(resourceImageView);
            }
        }
        for(ComboBox<ImageView> basicProduction : basicProductionResImages) {
            basicProduction.setCellFactory(listView -> new ListCell<>() {

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
                        imageView.setFitWidth(25);
                        imageView.setFitHeight(25);
                        setGraphic(imageView);
                    }
                }
            });
        }
    }

    /**
     * Method setupArrays adds the scene elements to the dedicated arrays
     */
    public void setupArrays() {
        tempShelfImageViews[0] = tempShelfImageView1;
        tempShelfImageViews[1] = tempShelfImageView2;
        tempShelfImageViews[2] = tempShelfImageView3;
        tempShelfImageViews[3] = tempShelfImageView4;

        basicProductionResImages.add(basicProductionRes1);
        basicProductionResImages.add(basicProductionRes2);
        basicProductionResImages.add(basicProductionRes3);

        ShelfView[0] = firstShelfRes1View;
        ShelfView[1] = secondShelfRes1View;
        ShelfView[2] = secondShelfRes2View;
        ShelfView[3] = thirdShelfRes1View;
        ShelfView[4] = thirdShelfRes2View;
        ShelfView[5] = thirdShelfRes3View;

        ExtraShelfView[0] = firstExtraShelfResView1;
        ExtraShelfView[1] = firstExtraShelfResView2;
        ExtraShelfView[2] = secondExtraShelfResView1;
        ExtraShelfView[3] = secondExtraShelfResView2;

        developmentImage[0] = developmentImageSlot1;
        developmentImage[1] = developmentImageSlot2;
        developmentImage[2] = developmentImageSlot3;
    }

    /**
     * Method showLeaders sets up leadersController
     */
    public void showLeaders() {
        String playerSelected = playersChoiceBox.getSelectionModel().getSelectedItem();
        ModelView.DashboardView playerDashboardView = modelView.getDashboards().stream().filter(dashboardView -> dashboardView.getNickname().equals(playerSelected)).findAny().orElse(null);
        if (playerDashboardView != null) {

            leadersController = showPopup("/view/scenes/sceneLeaders.fxml", true).getController();
            leadersController.setGUI(gui);
            leadersController.setModelView(modelView);
            leadersController.setup(playerDashboardView.getLeaderCards(), playerSelected);
            leadersController.start();
        }
    }

    /**
     * Method showLeaders sets up leadersController
     */
    public void handleLeaderCardActivation(boolean result, int id) {
        if(!result) {
            gui.showAlert("It was not possible to activate leader card " + id, Alert.AlertType.ERROR);
        }
        else {
            gui.send(new ActivateLeaderCard(id));
            gui.showAlert("Leader card activated", Alert.AlertType.CONFIRMATION);
        }
    }

    /**
     * Method choiceBoxChange checks which nickname has been selected in playersChoiceBox
     * and shows the selected player's dashboard
     */
    public void choiceBoxChange() {
        String playerSelected = playersChoiceBox.getSelectionModel().getSelectedItem();
        if(playerSelected != null) {
            if(developmentButton[0] != null)
                developmentButton[0].setOpacity(0);
            if(developmentButton[1] != null)
                developmentButton[1].setOpacity(0);
            if(developmentButton[1] != null)
                developmentButton[1].setOpacity(0);
            showDashboard(playerSelected);
        }
    }

     /**
     * Method handleBonusResource gets called if the player is not the first one to start its turn.
     * It lets the user pick the bonus resources.
     * @param amount the number of resources that the player can select
     */
    public void handleBonusResource(int amount) {
        if(amount > 0) {
            dashboardPane.setDisable(true);
            Pane bonusPane = new Pane();
            bonusPane.setPrefWidth(400);
            bonusPane.setPrefHeight(300);
            Label bonusTitle = new Label("Select " + amount + " Resource" + (amount > 1 ? "s" : ""));
            bonusTitle.setLayoutX(125);
            bonusTitle.setStyle("-fx-text-alignment: center ; -fx-font-size: 20px");
            bonusTitle.setLayoutY(2);
            bonusTitle.setPrefWidth(150);
            bonusTitle.setPrefHeight(50);
            bonusPane.getChildren().add(bonusTitle);

            String[] resources = {"coin2", "servant2" , "shield2" , "stone2"};
            TextField[] resourceTextField = new TextField[4];
            for(int i = 0; i< resources.length; i++) {
                Image resourceImage = new Image("/view/images/resources/"+resources[i]+".png");
                ImageView resourceImageView = new ImageView(resourceImage);

                resourceImageView.setLayoutX(100*i+ (double) 20);
                resourceImageView.setLayoutY(50);
                resourceImageView.setFitWidth(60);
                resourceImageView.setFitHeight(60);
                resourceImageView.setStyle("-fx-border-width: 5");
                bonusPane.getChildren().add(resourceImageView);

                resourceTextField[i] = new TextField();
                resourceTextField[i].setLayoutX((double) 100*i+ (double) 35);
                resourceTextField[i].setLayoutY(115);
                resourceTextField[i].setPrefWidth(30);
                resourceTextField[i].setPrefHeight(15);
                resourceTextField[i].setText("0");
                int finalI = i;
                resourceTextField[i].textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        resourceTextField[finalI].setText(newValue.replaceAll("[^\\d]", ""));
                    }
                    if (resourceTextField[finalI].getText().length() > 1) {
                        resourceTextField[finalI].setText(String.valueOf(resourceTextField[finalI].getText().charAt(0)));
                    }
                });
                bonusPane.getChildren().add(resourceTextField[i]);
            }
            Stage bonusStage = new Stage();

            Button selectButton = new Button("Select");
            selectButton.setLayoutX(150);
            selectButton.setStyle("-fx-text-alignment: center ; -fx-font-size: 20px");
            selectButton.setLayoutY(150);
            selectButton.setPrefWidth(100);
            selectButton.setPrefHeight(30);
            selectButton.setOnAction(event -> {
                int total = 0;
                ResourceMap bonusResources = new ResourceMap();
                for(int i=0; i<4; i++) {
                    if (resourceTextField[i].getText().length() > 0) {
                        total += Integer.parseInt(resourceTextField[i].getText());
                        switch (i){
                            case 0 -> bonusResources.modifyResource(Resource.COIN, Integer.parseInt(resourceTextField[i].getText()));
                            case 1 -> bonusResources.modifyResource(Resource.SERVANT, Integer.parseInt(resourceTextField[i].getText()));
                            case 2 -> bonusResources.modifyResource(Resource.SHIELD, Integer.parseInt(resourceTextField[i].getText()));
                            case 3 -> bonusResources.modifyResource(Resource.STONE, Integer.parseInt(resourceTextField[i].getText()));
                        }
                    }
                }
                if(total == amount) {
                    gui.send(new SendBonusResources(bonusResources));
                    bonusStage.close();
                    dashboardPane.setDisable(false);
                    gui.handleTurn();
                    showDashboard(gui.getNickname());
                }
            });
            bonusPane.getChildren().add(selectButton);
            Scene bonusScene = new Scene(bonusPane, 400, 200);
            bonusStage.setTitle("Select bonus resources");
            bonusStage.setScene(bonusScene);
            bonusStage.show();
        }
    }

    /**
     * Method handleWaitingText shows the nickname of the user that playing its turn
     */
    public void handleWaitingText() {
        if(modelView.getDashboards().size() > 1) {
            Platform.runLater(() -> dashboardPane.getChildren().remove(dashboardPane.lookup("#waitingText")));
            Label waitingText;
            if (modelView.getCurrPlayer().equals(gui.getNickname())) {
                waitingText = new Label("It's your turn!");
            } else {
                waitingText = new Label("Playing: " + modelView.getCurrPlayer() + ". Wait for your turn...");
            }
            waitingText.setId("waitingText" + (4));
            waitingText.setLayoutX(88);
            waitingText.setLayoutY(5);
            waitingText.setTextFill(Color.web("white"));
            waitingText.setPrefWidth(300);
            waitingText.setPrefHeight(23);
            waitingText.setId("waitingText");
            Platform.runLater(() -> dashboardPane.getChildren().add(waitingText));
        }
    }

    /**
     * Method activateProduction sets a disabled production to "enabled" and
     * adds it to the list of productions to activate, or sets an "enabled"
     * production to "disabled" and removes it from the list of productions
     * @param index the index of the selected production
     */
    public void activateProduction(int index) {
        if(playersChoiceBox.getSelectionModel().getSelectedItem().equals(gui.getNickname())) {
            if (enabledProductions.contains(index - 1)) {
                enabledProductions.remove(Integer.valueOf(index - 1));
                if (index <= 4)
                    developmentButton[index - 1].setText("Enable");
                if (index == 4) {
                    basicProductionResImages.get(0).setDisable(false);
                    basicProductionResImages.get(1).setDisable(false);
                    basicProductionResImages.get(2).setDisable(false);
                } else if (index < 4)
                    developmentImage[index - 1].setStyle("-fx-opacity: 1");
            } else {
                if (index != 4 || (basicProductionResources[0] != null && basicProductionResources[1] != null && basicProductionResources[2] != null)) {
                    enabledProductions.add(index - 1);
                    if (index <= 4)
                        developmentButton[index - 1].setText("Disable");
                    if (index == 4) {
                        basicProductionResImages.get(0).setDisable(true);
                        basicProductionResImages.get(1).setDisable(true);
                        basicProductionResImages.get(2).setDisable(true);
                    }
                }
                if (index < 4)
                    developmentImage[index - 1].setStyle("-fx-opacity: 0.7");

            }
            activateProductionsButton.setDisable(enabledProductions.size() == 0);
        }
    }

    /**
     * Method activateAllProductions checks if the enabled productions meet their requirements,
     * if they don't it shows an alert, otherwise it send a message to server to activate the productions
     */
    public void activateAllProductions() {
        ResourceMap totalResources = modelView.getMyDashboard().getTotalResources();
        ArrayList<Integer> productions = new ArrayList<>();
        ResourceMap requiredResources = new ResourceMap();
        for(Integer index : enabledProductions) {
            if(index == 3) {
                requiredResources.modifyResource(basicProductionResources[0], 1);
                requiredResources.modifyResource(basicProductionResources[1], 1);
            }
            else {
                int production = index < 3 ? modelView.getMyDashboard().getActiveDevelopments().get(index) : (int) modelView.getMyDashboard().getLeaderCards().keySet().toArray()[index-4];
                productions.add(production);
                Card card = JsonCardsCreator.generateCard(production);
                if(card instanceof ProductionLeaderCard) requiredResources.addResources(((ProductionLeaderCard) card).getProduction().getInputResource());
                else if(card instanceof DevelopmentCard) requiredResources.addResources(((DevelopmentCard) card).getProduction().getInputResource());
            }
        }
        if(totalResources.removeResources(requiredResources)) {
            gui.send(new ActivateProduction(productions));
            if (enabledProductions.contains(3)) {
                ResourceMap inputBasicProduction = new ResourceMap();
                inputBasicProduction.modifyResource(basicProductionResources[0], 1);
                inputBasicProduction.modifyResource(basicProductionResources[1], 1);
                ResourceMap outputBasicProduction = new ResourceMap();
                outputBasicProduction.modifyResource(basicProductionResources[2], 1);
                gui.send(new BasicProduction(inputBasicProduction, outputBasicProduction));
            }
            if(enabledProductions.contains(4)) {
                ResourceMap output = new ResourceMap();
                output.modifyResource(leaderExtraProductionRes.get(0), 1);
                gui.send(new BasicProduction(new ResourceMap(), output));
            }
            if(enabledProductions.contains(5)) {
                ResourceMap output = new ResourceMap();
                output.modifyResource(leaderExtraProductionRes.get(1), 1);
                gui.send(new BasicProduction(new ResourceMap(), output));
            }
            gui.basicActionPlayed();
            showDashboard(gui.getNickname());
        }
        else {
            gui.showAlert("There are not enough resources to activate all enabled production", Alert.AlertType.ERROR);
        }

    }

    /**
     * Method resetProductions resets all the productions to "disabled"
     */
    public void resetProductions() {
        for (Button button : developmentButton) {
            if(button != null) {
                Platform.runLater(() -> {
                    button.setText("Enable");
                    button.setDisable(false);
                });
            }
        }
        enabledProductions.clear();
        for(ImageView devImage : developmentImage) {
            if (devImage != null)
                devImage.setStyle("-fx-opacity: 1");
        }
        for(ComboBox<ImageView> basicProduction : basicProductionResImages) {
            basicProduction.setDisable(false);
        }
        activateProductionsButton.setDisable(true);
    }

    /**
     * Method resetProductions resets all the productions and sets the buttons to disabled,
     * so that they cannot be activated
     */
    public void disableProductions() {
        for (Button button : developmentButton) {
            if(button != null) {
                button.setDisable(true);
            }
        }
        enabledProductions.clear();
        basicProductionRes1.setDisable(true);
        basicProductionRes2.setDisable(true);
        basicProductionRes3.setDisable(true);
        activateProductionsButton.setDisable(true);
    }

    /**
     * Method showDashboard shows the current faithTrack of the player
     */
    public void updatePlayerFaithTrack() {
        ModelView.DashboardView dashboardView = modelView.getMyDashboard();
        showFaithTrack(dashboardView.getFaithMarker(), dashboardView.getBlackMarker(), dashboardView.getPopesFavorTiles());

    }

    /**
     * Method showDashboard shows the current strongbox of the player
     */
    public void updatePlayerStrongbox() {
        ModelView.DashboardView dashboardView = modelView.getMyDashboard();
        showStrongbox(dashboardView.getStrongbox());
    }

    /**
     * Method showDashboard shows the current development cards of the player
     */
    public void updatePlayerActiveDevelopments() {
        ModelView.DashboardView dashboardView = modelView.getMyDashboard();
        showActiveDevelopments(dashboardView.getActiveDevelopments());
    }

    /**
     * Method showDashboard shows the current warehouse of the player
     */
    public void updatePlayerWarehouse() {
        ModelView.DashboardView dashboardView = modelView.getMyDashboard();
        showWarehouse(dashboardView.getWarehouse(), dashboardView.getExtraShelfResources());
    }

    /**
     * Method showDashboard shows the current dashboard of the selected player
     * @param nickname name of the selected player
     */
    public void showDashboard(String nickname) {
        if(nickname.equals(gui.getNickname())) {
            gridButton.setDisable(false);
            ModelView.DashboardView dashboardView = modelView.getMyDashboard();
            if(gui.getNickname().equals(modelView.getCurrPlayer()) || modelView.getCurrPlayer().length() < 1) {
                ArrayList<Action> actions = gui.getValidActions();
                if (actions.size() == 0) {
                    marketButton.setDisable(true);
                    endTurnButton.setDisable(true);
                    for(Button button : developmentButton) {
                        if(button != null) {
                            button.setDisable(true);
                        }
                    }
                } else {
                    marketButton.setDisable(!actions.contains(Action.TAKE_RESOURCE));
                    endTurnButton.setDisable(!actions.contains(Action.END_TURN));

                    if (actions.contains(Action.ACTIVATE_PRODUCTION)) {
                        resetProductions();
                    } else {
                        disableProductions();
                    }

                }
            }

            showFaithTrack(dashboardView.getFaithMarker(), dashboardView.getBlackMarker(), dashboardView.getPopesFavorTiles());
            showStrongbox(dashboardView.getStrongbox());
            showActiveDevelopments(dashboardView.getActiveDevelopments());
            showWarehouse(dashboardView.getWarehouse(), dashboardView.getExtraShelfResources());
        }
        else {
            ModelView.DashboardView playerDashboardView = modelView.getDashboards().stream().filter(dashboardView -> dashboardView.getNickname().equals(nickname)).findAny().orElse(null);
            if (playerDashboardView != null) {
                showFaithTrack(playerDashboardView.getFaithMarker(), playerDashboardView.getBlackMarker(), playerDashboardView.getPopesFavorTiles());
                showStrongbox(playerDashboardView.getStrongbox());
                showActiveDevelopments(playerDashboardView.getActiveDevelopments());
                showWarehouse(playerDashboardView.getWarehouse(), playerDashboardView.getExtraShelfResources());
                marketButton.setDisable(true);
            }
        }
    }

    /**
     * Method showActiveDevelopments shows the active developments cards or an empty card if a slot doesn't have any
     */
    public void showActiveDevelopments(ArrayList<Integer> activeDevelopments) {
        if(developmentButton == null) {
            developmentButton = new Button[4];
        }
        Image[] devImages = new Image[3];
        int xStart = 410, yStart = 320;
        double len = 140; double margin = 20;
        if(developmentButton[3] == null) {
            developmentButton[3] = new Button("Enable");
            Platform.runLater(() -> dashboardPane.getChildren().add(developmentButton[3]));
            developmentButton[3].setLayoutX(300);
            developmentButton[3].setLayoutY(570);
            developmentButton[3].setPrefWidth(len*2/3);
            developmentButton[3].setPrefHeight(len/6);
            developmentButton[3].setOnAction(event -> activateProduction(4));
            developmentButton[3].setId("developmentButton4");
        }
        developmentButton[3].setDisable(!modelView.getCurrPlayer().equals(gui.getNickname()) && !(modelView.getCurrPlayer().length() < 1));

        for (int slot=0; slot<3; slot++) {
            int finalSlot = slot;
            if (activeDevelopments.get(slot) != null) {
                if(developmentButton[slot] == null ) {
                    developmentButton[slot] = new Button("Enable");
                    Platform.runLater(() -> dashboardPane.getChildren().add(developmentButton[finalSlot]));
                }
                developmentButton[slot].setLayoutX(410 + len / 6 + (len + margin) * slot);
                developmentButton[slot].setLayoutY(340 + len * 3 / 2);
                developmentButton[slot].setPrefWidth(len * 2 / 3);
                developmentButton[slot].setPrefHeight(len / 6);
                developmentButton[slot].setOnAction(event -> activateProduction(finalSlot + 1));
                developmentButton[slot].setId("developmentButton" + (slot + 1));
            }

            if (activeDevelopments.get(slot) != null) {
                devImages[slot] = new Image("/view/images/developments/developmentCard"+activeDevelopments.get(slot)+".png");
                developmentButton[slot].setOpacity(1);
            } else {
                devImages[slot] = new Image("/view/images/developments/developmentCardEmpty.png");
                if (developmentButton[slot] != null)
                    developmentButton[slot].setOpacity(0);
            }


        }

        for(int i=0; i<developmentImage.length; i++) {
            if(developmentImage[i] == null) {
                developmentImage[i] = new ImageView();
                int finalI = i;
                Platform.runLater(() -> dashboardPane.getChildren().add(developmentImage[finalI]));
            }
            developmentImage[i].setImage(devImages[i]);
            developmentImage[i].setLayoutX(xStart+(len+margin)*i);
            developmentImage[i].setLayoutY(yStart);
            developmentImage[i].setFitWidth(len);
            developmentImage[i].setFitHeight(len*3/2);
            developmentImage[i].setId("developmentImageSlot" + (i+1));

            developmentImage[i].setStyle("fx-opacity: " + (enabledProductions != null && enabledProductions.contains(i) ? 0.7 :1));
            developmentImage[i].setFitHeight(len*3/2);
        }

        if(!gui.getValidActions().contains(Action.ACTIVATE_PRODUCTION)) {
           for(Button button : developmentButton)
               if(button != null) {
                   button.setDisable(true);
               }
        }
    }

    /**
     * Method showStrongbox shows the strongbox resources and their amount.
     * @param strongbox ResourceMap containing the amount of resources in the strongbox
     */
    private void showStrongbox(ResourceMap strongbox) {
        int xStart = 120, yStart = 515;
        double len = 40; double padding = 20;
        int resCont = 0;
        if(amountLabel == null) {
            amountLabel = new Label[4];
        }
        for (Map.Entry<Resource,Integer> value  : strongbox.getResources().entrySet() ) {
            String amount = "x" + value.getValue();
            Image resourceImage = new Image(getImage(value.getKey()));
            int finalResCont = resCont;
            if(strongboxResourceView[resCont] == null) {
                strongboxResourceView[resCont] = new ImageView();
                Platform.runLater(() -> {
                    dashboardPane.getChildren().add(amountLabel[finalResCont]);
                    dashboardPane.getChildren().add(strongboxResourceView[finalResCont]);
                });
                strongboxResourceView[resCont].setImage(resourceImage);
                strongboxResourceView[resCont].setLayoutX(xStart+(len+padding*3/2)*(resCont%2));
                int half = resCont/2;
                strongboxResourceView[resCont].setLayoutY(yStart+(len+padding)*half);
                strongboxResourceView[resCont].setFitWidth(len);
                strongboxResourceView[resCont].setFitHeight(len);
                strongboxResourceView[resCont].setId("strongBoxImage"+resCont);
            }
            if( amountLabel[resCont] == null) {
                amountLabel[resCont] = new Label();
                amountLabel[resCont].setTextFill(Color.web("white"));
                amountLabel[resCont].setLayoutX(strongboxResourceView[resCont].getLayoutX()+len/2);
                amountLabel[resCont].setLayoutY(strongboxResourceView[resCont].getLayoutY()+len);
                amountLabel[resCont].setMinWidth(strongboxResourceView[resCont].getFitWidth());
                amountLabel[resCont].setMinHeight(20);
                amountLabel[resCont].setId("strongboxLabel"+resCont);
            }

            Platform.runLater(() -> amountLabel[finalResCont].setText(amount));
            resCont++;
        }

    }

    /**
     * Method showWarehouse shows the warehouse of the selected player and, if enabled,
     * the temporary shelf with the resources obtained from the market
     * @param warehouse warehouse of the selected players
     * @param extraShelfResources arraylist of the resources stored in the extra shelf enabled by some leader cards
     */
    private void showWarehouse(ArrayList<Resource> warehouse, ArrayList<Resource> extraShelfResources) {

        double len = 30;
        int xStart = 200, yStart = 335, yOffset = 55;
        if(ExtraShelfView[0] != null)
            ExtraShelfView[0].setStyle("-fx-opacity: 0");
        if(ExtraShelfView[1] != null)
            ExtraShelfView[1].setStyle("-fx-opacity: 0");
        for(int i = 0; i < extraShelfResources.size()*2; i++) {
            int xBool = (i+1)%2;
            int yBool = (i/2);
            Image extraShelfImage = new Image(getImage(warehouse.get(6+i) == null ? extraShelfResources.get(yBool) : warehouse.get(6+i)));
            if(ExtraShelfView[i] == null) {
                ExtraShelfView[i] = new ImageView();
                ExtraShelfView[i].setLayoutX(360 - (len + 20) * xBool);
                ExtraShelfView[i].setLayoutY(yStart + (len + 20) * yBool);
                ExtraShelfView[i].setFitWidth(len);
                ExtraShelfView[i].setFitHeight(len);
                ExtraShelfView[i].setPickOnBounds(true);
                int finalI = i;
                ExtraShelfView[i].setOnMouseClicked((MouseEvent e) -> handleShelfClick(6 + finalI));
                ExtraShelfView[i].setId("extraShelf" + i);
                Platform.runLater(() -> dashboardPane.getChildren().add(ExtraShelfView[finalI]));
            }
            ExtraShelfView[i].setStyle("-fx-opacity: " + (warehouse.get(6 + i) == null ? "0.5" : "1"));
            ExtraShelfView[i].setImage(extraShelfImage);
        }

        double[] shelfLayoutX = {xStart-len/2, xStart-len/4-len, xStart+len/4, xStart-len - len/2 -len/4, xStart-len/2, xStart+len/2+len/4 };
        for(int i = 0; i < ShelfView.length; i++) {
            int row =(i+1)/2-i/5;
            Image shelfImage =  new Image(getImage(warehouse.get(i)));
            int finalI = i;
            if(ShelfView[i] == null) {
                ShelfView[i] = new ImageView();
                ShelfView[i].setLayoutX(shelfLayoutX[i]);
                ShelfView[i].setLayoutY((double) yStart + yOffset * row);
                ShelfView[i].setFitWidth(len);
                ShelfView[i].setFitHeight(len);
                ShelfView[i].setPickOnBounds(true);
                ShelfView[i].setOnMouseClicked((MouseEvent e) -> handleShelfClick(finalI));
                ShelfView[i].setId("shelf" + (i));
                Platform.runLater(() -> dashboardPane.getChildren().add(ShelfView[finalI]));
            }
            ShelfView[i].setImage(shelfImage);
        }
        ResourceMap temporaryShelf = new ResourceMap();
        for(int i = 10; i<=14; i++) {
            Resource temp = warehouse.get(i-1);
            if ( temp != null) {
                temporaryShelf.modifyResource(temp, 1);
            }
        }

        Platform.runLater(() -> {
            for(ImageView item : tempShelfImageViews) {
                if(item != null)
                    dashboardPane.getChildren().remove(dashboardPane.lookup("#" + item.getId()));
            }
        });

        if ( showTempShelf ) {
            marketButton.setDisable(true);
            gridButton.setDisable(true);

            if(temporaryShelfImage == null) {
                Image image = new Image("/view/images/resources/temporaryShelfEmpty.png");
                temporaryShelfImage = new ImageView(image);
                temporaryShelfImage.setLayoutX(300);
                temporaryShelfImage.setLayoutY(500);
                temporaryShelfImage.setFitWidth(500);
                temporaryShelfImage.setFitHeight(142);
                temporaryShelfImage.setId("temporaryShelfImage");

                Platform.runLater(() -> dashboardPane.getChildren().add(temporaryShelfImage));
            }
            if(tempShelfButton == null) {
                tempShelfButton = new Button("Done");
                tempShelfButton.setLayoutX(810);
                tempShelfButton.setLayoutY(612);
                tempShelfButton.setPrefWidth(60);
                tempShelfButton.setPrefHeight(30);
                tempShelfButton.setId("temporaryShelfButton");
                tempShelfButton.setOnMouseClicked((MouseEvent e) -> {
                    showTempShelf = false;
                    gui.send(new SetWarehouse(modelView.getMyDashboard().getWarehouse()));
                    gui.basicActionPlayed();
                    showDashboard(gui.getNickname());
                });
                Platform.runLater(() -> dashboardPane.getChildren().add(tempShelfButton));
            }
            tempShelfButton.setDisable(endTurnButton.isDisabled());

                for(int i =0; i<tempShelfImageViews.length; i++) {
                    Image tempShelfImage = new Image(getImage(warehouse.get(10+i)));
                    if(tempShelfImageViews[i] == null) {
                        tempShelfImageViews[i] = new ImageView();
                    }
                    tempShelfImageViews[i] = new ImageView(tempShelfImage);
                    tempShelfImageViews[i].setLayoutX(337.5+120*i);
                    tempShelfImageViews[i].setLayoutY(530);
                    tempShelfImageViews[i].setFitWidth(85);
                    tempShelfImageViews[i].setFitHeight(85);
                    tempShelfImageViews[i].setPickOnBounds(true);
                    int finalI = i;
                    tempShelfImageViews[i].setOnMouseClicked((MouseEvent e) -> handleShelfClick(10+ finalI));
                    tempShelfImageViews[i].setId("tempShelfImageView"+(1+i));
                    Platform.runLater(() -> dashboardPane.getChildren().add(tempShelfImageViews[finalI]));
                }
        }
        else {
            if(temporaryShelfImage != null) {
                Platform.runLater(() -> {
                    dashboardPane.getChildren().remove(dashboardPane.lookup("#temporaryShelfImage"));
                    dashboardPane.getChildren().remove(dashboardPane.lookup("#temporaryShelfButton"));
                });
                tempShelfButton = null;
                temporaryShelfImage = null;
            }
        }
    }

    /**
     * Method resizeShelf changes the size of the clicked resource's image in the shelves of the temporary shelf
     * @param index index of the selected shelf
     */
    private void resizeShelf(int index) {
        ImageView selectedShelf;
        int margin = 3;
        if(index < 6)
            selectedShelf = ShelfView[index];
        else if(index >= 10 ) {
            selectedShelf = tempShelfImageViews[index - 10];
            margin *= 2;
        }
        else
            selectedShelf = ExtraShelfView[index-6];

        margin *= swapIndex == -1 ? 1 : -1;

        selectedShelf.setLayoutX(selectedShelf.getLayoutX()+margin);
        selectedShelf.setLayoutY(selectedShelf.getLayoutY()+margin);
        selectedShelf.setFitWidth(selectedShelf.getFitWidth()-margin*2);
        selectedShelf.setFitHeight(selectedShelf.getFitHeight()-margin*2);
    }

    /**
     * Method handleShelfClick lets the user move the resources on the shelves, calling resizeShelf to show
     * which resource has been selected or by handling the swap of the two selected resources
     * @param index index of the selected shelf
     */
    private void handleShelfClick(int index) {
        if(showTempShelf) {
            if (swapIndex < 0) {
                resizeShelf(index);
                swapIndex = index;
            } else {
                resizeShelf(swapIndex);
                List<Integer> validSlots = IntStream.rangeClosed(0, 13)
                        .boxed().collect(Collectors.toList());

                if (modelView.getMyDashboard().getExtraShelfResources().size() == 0) {
                    validSlots.removeAll(Arrays.asList(6, 7, 8, 9));
                } else if (modelView.getMyDashboard().getExtraShelfResources().size() == 1) {
                    validSlots.removeAll(Arrays.asList(8, 9));
                }

                if (validSlots.contains(swapIndex) && validSlots.contains(index)) {
                    modelView.getMyDashboard().swapResources(swapIndex, index);
                    swapIndex = -1;
                    ModelView.DashboardView dashboardView = modelView.getMyDashboard();
                    showWarehouse(dashboardView.getWarehouse(), dashboardView.getExtraShelfResources());
                } else {
                    gui.showAlert("Slot numbers are not valid.\nAvailable slots:\n" + validSlots, Alert.AlertType.ERROR);
                }
                endTurnButton.setDisable(!modelView.getMyDashboard().checkWarehouse());
                tempShelfButton.setDisable(endTurnButton.isDisabled());
            }
        }
    }

    /**
     * Method getImage
     * @param resource the resource of which an image is needed
     * @return the url of the image associated with the selected resource
     */
    String getImage(Resource resource) {
        if (resource == null) {
            return "/view/images/resources/notFound.png";
        }
        else {
            String res = switch (resource) {
                case COIN -> "coin2";
                case SERVANT -> "servant2";
                case SHIELD -> "shield2";
                case STONE -> "stone2";
            };
            return "/view/images/resources/" + res + ".png";
        }
    }

    /**
     * Method showFaithTrack shows the markers position and the tiles of the selected players
     * @param faithMarker position of the faith marker
     * @param blackMarker position of the black faith marker
     * @param popesFavorTiles arrays of booleans to show which pope favor tiles have been activated
     */
    private void showFaithTrack(Integer faithMarker, Integer blackMarker, Boolean[] popesFavorTiles) {

        Image faithImage = new Image("/view/images/faithTrack/cross.png");

         for(int i = 0; i<popesFavorTiles.length; i++) {
             Image tileImage = new Image("/view/images/faithTrack/pope_favor"+(i+1) + ((popesFavorTiles[i] != null) && popesFavorTiles[i] ? "_front": "_back" )+".png");
             if(popeFavorTilesImages[i] == null) {
                 popeFavorTilesImages[i] = new ImageView();
                 popeFavorTilesImages[i].setLayoutX(290 + 222.5 * i);
                 popeFavorTilesImages[i].setLayoutY(160);
                 popeFavorTilesImages[i].setFitWidth(60);
                 popeFavorTilesImages[i].setFitHeight(60);
                 popeFavorTilesImages[i].setId("popeFavorTilesImages" + (i + 1));
                 dashboardPane.getChildren().add(popeFavorTilesImages[i]);
             }
             popeFavorTilesImages[i].setImage(tileImage);
         }
        popeFavorTilesImages[1].setLayoutX(491);
        popeFavorTilesImages[1].setLayoutY(120);
        popeFavorTilesImages[1].setFitWidth(60);
        popeFavorTilesImages[1].setFitHeight(60);

        if(faithTrackImage == null ) {
            faithTrackImage = new ImageView();
            faithTrackImage.setId("faithTrackImage");
            Platform.runLater(() -> dashboardPane.getChildren().add(faithTrackImage));
            faithTrackImage.setImage(faithImage);
        }
        int xOffset = 80;
        int yOffset = 190;

        int[] xStart = {40,80,120,120,120,160,200,240,280,320,320,320,360, 400,440,480, 520, 520, 520, 560, 600, 640, 680, 720, 760};
        int[] yStart = {0, 0, 0, -40, -80, -80 ,-80 ,-80, -80 ,-80, -40, 0, 0, 0, 0, 0, 0, -40, -80, -80, -80 ,-80 , -80 , -80 ,-80};
        int len = 40;

        faithTrackImage.setLayoutX(xOffset+ (double) xStart[faithMarker]);
        faithTrackImage.setLayoutY(yOffset+ (double) yStart[faithMarker]);
        faithTrackImage.setFitWidth(len);
        faithTrackImage.setFitHeight(len);

        if ( modelView.getDashboards().size() == 1 ) {
            Image blackFaithImage = new Image("/view/images/faithTrack/blackCross.png");


            if(blackFaithTrackImage == null ) {
                blackFaithTrackImage = new ImageView();
                blackFaithTrackImage.setId("blackFaithTrackImage");
                Platform.runLater(() -> dashboardPane.getChildren().add(blackFaithTrackImage));
                blackFaithTrackImage.setImage(blackFaithImage);
            }


            if(faithMarker.equals(blackMarker)) {
                double halfLen = (double)(len)/2;
                blackFaithTrackImage.setLayoutX(xOffset+xStart[blackMarker]+halfLen);
                blackFaithTrackImage.setLayoutY(yOffset+yStart[blackMarker]+halfLen);
                blackFaithTrackImage.setFitWidth(halfLen);
                blackFaithTrackImage.setFitHeight(halfLen);
                faithTrackImage.setFitWidth(halfLen);
                faithTrackImage.setFitHeight(halfLen);
            }
            else {
                blackFaithTrackImage.setLayoutX(xOffset+ (double) xStart[blackMarker]);
                blackFaithTrackImage.setLayoutY(yOffset+ (double) yStart[blackMarker]);
                blackFaithTrackImage.setFitWidth(len);
                blackFaithTrackImage.setFitHeight(len);
                faithTrackImage.setFitWidth(len);
                faithTrackImage.setFitHeight(len);
            }
        }
    }

    /**
     * Method showMarket sets up marketController and shows the current market.
     */
    public void showMarket() {
        marketController = showPopup("/view/scenes/sceneMarket.fxml", true).getController();
        marketController.setGUI(gui);
        marketController.setMarketTray(modelView.getMarketTray());
        marketController.setSlideMarble(modelView.getSlideMarble());
        marketController.start();
        marketController.checkActiveWhiteMarbleLeaders(modelView.getMyDashboard().getLeaderCards());
    }

    /**
     * Method showGrid sets up gridController and shows the available cards that can be bought
     */
    public void showGrid() {
        gridController = showPopup("/view/scenes/sceneGrid.fxml", true).getController();
        gridController.setGUI(gui);
        gridController.setup(modelView);
    }

    /**
     * Method showPopup opens a scene as a popup
     * @param scenePath the path of the scene to open
     * @param stageButtonsVisible boolean that indicates if the stage should or not show maximize, minimize and close buttons
     * @return the FXMLLoader of the scene
     */
    private FXMLLoader showPopup(String scenePath, boolean stageButtonsVisible) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(scenePath));
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(gui.mainStage);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            if(!stageButtonsVisible) {
                stage.setY(250);
                stage.setX(350);
                stage.initStyle(StageStyle.UNDECORATED);
            }

            stage.show();

        } catch (IOException e) {
            Logger.getLogger("DashboardController error");
        }
        return loader;
    }

    /**
     * Method setModelView sets the model view.
     * @param modelView ModelView instance.
     */
    public void setModelView(ModelView modelView) {
        this.modelView = modelView;
    }

    /**
     * Method startTurn resets basicProduction and updates the players dashboard
     */
    public void startTurn() {
        for(int i=0; i<basicProductionResImages.size(); i++) {
            int finalI = i;
            Platform.runLater(() -> basicProductionResImages.get(finalI).valueProperty().set(null));
            basicProductionResources[i] = null;
        }
        for(Action action : Action.values()){
            action.enabled = true;
        }
        endTurnButton.setDisable(false);
        updatePlayerDashboard();
    }

    /**
     * Method endTurn disables all the elements that cannot be interacted with when it's another player's turn
     * and sends a message of endTurn
     */
    public void endTurn() {
        if (showTempShelf) {
            showTempShelf = false;
            gui.send(new SetWarehouse(modelView.getMyDashboard().getWarehouse()));

            Platform.runLater(() -> {
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView1"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView2"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView3"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView4"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#temporaryShelfImage"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#temporaryShelfButton"));
            });

        }
        gui.send(new EndTurn());
        endTurnButton.setDisable(true);
        marketButton.setDisable(true);
        if(modelView.getDashboards().size() > 1 ) {
            for (Button button : developmentButton) {
                if (button != null) {
                    button.setDisable(true);
                }
            }
        }
        for(Action action : Action.values()){
            action.enabled = false;
        }

        handleWaitingText();
    }

    /**
     * Method updatePlayerDashboard shows the updated dashboard of the player
     */
    public void updatePlayerDashboard() {
        Platform.runLater(() -> {
            playersChoiceBox.getSelectionModel().select(gui.getNickname());
            playersChoiceBox.setValue(gui.getNickname());
        });
        showDashboard(gui.getNickname());
    }

    /**
     * Method updateDashboards shows the updated dashboard of the player
     * currently selected
     */
    public void updateDashboards() {
        showDashboard(playersChoiceBox.getSelectionModel().getSelectedItem());
    }

    /**
     * Method handleTemporaryShelf is called when resources are received from the market and
     * calls showsDashboard method to update the scene and show the temporary shelf
     */
    public void handleTemporaryShelf() {
        showDashboard(gui.getNickname());
    }

    /**
     * Method basicProductionResChange
     */
    public void basicProductionResChange(ActionEvent actionEvent) {
        @SuppressWarnings("unchecked") ComboBox<ImageView> comboBox = (ComboBox<ImageView>) actionEvent.getSource();
        int selectedIndex = -1;
        switch (comboBox.getId()) {
            case "basicProductionRes1" -> selectedIndex = 0;
            case "basicProductionRes2" -> selectedIndex = 1;
            case "basicProductionRes3" -> selectedIndex = 2;
        }
        switch (comboBox.getSelectionModel().getSelectedIndex()) {
            case 0 -> basicProductionResources[selectedIndex] = Resource.COIN;
            case 1 -> basicProductionResources[selectedIndex] = Resource.SERVANT;
            case 2 -> basicProductionResources[selectedIndex] = Resource.SHIELD;
            case 3 -> basicProductionResources[selectedIndex] = Resource.STONE;
        }
    }

    /**
     * Method showEndGameText lets the user know that the game is about to finish
     */
    public void showEndGameText() {
        Label endGameLabel = new Label("The game is about to finish.Waiting for the remaining players to play theirs last turn...");
        endGameLabel.setLayoutX(88);
        endGameLabel.setLayoutY(dashboardPane.getHeight()-40);
        endGameLabel.setTextFill(Color.web("red"));
        endGameLabel.setPrefHeight(40);
        endGameLabel.setPrefWidth(dashboardPane.getWidth()-176);
        endGameLabel.setId("endGameLabel");
        Platform.runLater(() -> {
            dashboardPane.getChildren().remove(dashboardPane.lookup("#endGameLabel"));
            dashboardPane.getChildren().add(endGameLabel);
        });
    }

    /**
     * Method showStats disables dashboardPane and sets up gameOverController to show the multiplayer game result
     * @param map map that associates the players nicknames with the number of points they made
     */
    public void showStats(Map<String, Integer> map) {
        dashboardPane.setDisable(true);

        Platform.runLater(() -> {
        gameOverController = showPopup("/view/scenes/sceneGameOver.fxml", true).getController();
        gameOverController.setGUI(gui);
        gameOverController.setMap(map);
        gameOverController.start();
        });
    }

    /**
     * Method singlePlayerEnd disables dashboardPane and sets up gameOverController to show the single player game result
     * @param points the number of points made by the user
     */
    public void singlePlayerEnd(int points) {
        dashboardPane.setDisable(true);
        Platform.runLater(() -> {
            gameOverController = showPopup("/view/scenes/sceneGameOver.fxml", false).getController();
            if(points > 0) {
                Map<String, Integer> map = new HashMap<>();
                map.put(gui.getNickname(), points);
                gameOverController.setMap(map);
            }
            gameOverController.setGUI(gui);
            gameOverController.start();
        });
    }

    /**
     * Method showToken display the last drawn token in the single player game
     * @param index the type of token to show
     */
    public void showToken(int index) {
        if(tokenImageView == null) {
            tokenImageView = new ImageView();
            tokenImageView.setLayoutX(98);
            tokenImageView.setLayoutY(90);
            tokenImageView.setFitWidth(50);
            tokenImageView.setFitHeight(50);
            Platform.runLater(() -> dashboardPane.getChildren().add(tokenImageView));
        }
        Image tokenImage = new Image("/view/images/tokens/cerchio"+index+".png");
        tokenImageView.setImage(tokenImage);
        showDashboard(gui.getNickname());
    }
}
