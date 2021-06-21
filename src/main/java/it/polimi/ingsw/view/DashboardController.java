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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DashboardController {
    @FXML Button showLeaders;
    @FXML Button activateProductionsButton;
    @FXML Button marketButton;
    @FXML Button gridButton;
    @FXML ChoiceBox<String> playersChoiceBox;
    @FXML Pane dashboardPane;
    @FXML Button endTurnButton;
    private ModelView modelView;
    MarketController marketController;
    GridController gridController;
    LeadersController leadersController;
    GameOverController gameOverController;
    private static GUI gui;
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
    Button[] developmentButton = new Button [4];

    Resource[] basicProductionResources = new Resource[3];
    ArrayList<Integer> enabledProductions = new ArrayList<>();
    @FXML ComboBox<ImageView> basicProductionRes1;
    @FXML ComboBox<ImageView> basicProductionRes2;
    @FXML ComboBox<ImageView> basicProductionRes3;
    ArrayList<ComboBox<ImageView>> basicProductionResImages = new ArrayList<>();

    Label[] amountLabel = new Label [4];

    public void setGUI(GUI gui) {
        DashboardController.gui = gui;
    }

    public void setup() {
        amountLabel = new Label[4];
        for(ModelView.DashboardView dashboard : modelView.getDashboards()) {
            playersChoiceBox.getItems().add(dashboard.getNickname());
        }
        setupArrays();
        playersChoiceBox.setValue(gui.getNickname());

        endTurnButton.setDisable(false);
        if(modelView.getDashboards().size() == 1)
            showToken(0);

        setupBasicProduction();
    }
    public void setupBasicProduction() {

        basicProductionResources = new Resource[3];
        activateProductionsButton.setDisable(true);
        enabledProductions = new ArrayList<>();
        String[] resources = {"coin2", "servant2" , "shield2" , "stone2"};
        for(ComboBox<ImageView> basicProduction : basicProductionResImages) {
            basicProduction.setStyle("-fx-opacity: 1;");
        }
        for (String resource : resources) {
            Image resourceImage = new Image("/view/images/resources/" + resource + ".png");
            for(ComboBox<ImageView> basicProduction : basicProductionResImages) {
                ImageView resourceImageView = new ImageView(resourceImage);
                resourceImageView.setFitWidth(22);
                resourceImageView.setFitHeight(22);
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
                        imageView.setFitWidth(22);
                        imageView.setFitHeight(22);
                        setGraphic(imageView);
                    }
                }
            });
        }
    }
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
    }
    public void showLeaders() {
        String playerSelected = playersChoiceBox.getSelectionModel().getSelectedItem();
        ModelView.DashboardView playerDashboardView = modelView.getDashboards().stream().filter(dashboardView -> dashboardView.getNickname().equals(playerSelected)).findAny().orElse(null);
        if (playerDashboardView != null) {

            leadersController = showPopup("/view/scenes/sceneLeaders.fxml").getController();
            leadersController.setGUI(gui);
            leadersController.setModelView(modelView);
            leadersController.setup(playerDashboardView.getLeaderCards(), playerSelected);
            leadersController.start();
        }
    }

    public void handleLeaderCardActivation(boolean result, int id) {
        if(!result) {
            gui.showAlert("It was not possible to activate leader card " + id, Alert.AlertType.ERROR);
        }
        else {
            gui.send(new ActivateLeaderCard(id));
            gui.showAlert("Leader card activated", Alert.AlertType.CONFIRMATION);
        }
    }



    public void choiceBoxChange() {
        String playerSelected = playersChoiceBox.getSelectionModel().getSelectedItem();
        if(playerSelected != null) {
            if (playerSelected.equals(gui.getNickname())) {
                marketButton.setDisable(false);
                gridButton.setDisable(false);
            }
            else {
                marketButton.setDisable(true);
                gridButton.setDisable(true);
            }
            showDashboard(playerSelected);
        }
    }
    public void handleBonusResource(int amount) {
        if(amount > 0) {
            dashboardPane.setDisable(true);
            Pane bonusPane = new Pane();
            bonusPane.setPrefWidth(400);
            bonusPane.setPrefHeight(300);
            //add nodes
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

                resourceImageView.setLayoutX(100*i+20);
                resourceImageView.setLayoutY(50);
                resourceImageView.setFitWidth(60);
                resourceImageView.setFitHeight(60);
                resourceImageView.setStyle("-fx-border-width: 5");
                bonusPane.getChildren().add(resourceImageView);

                resourceTextField[i] = new TextField();
                resourceTextField[i].setLayoutX(100*i+35);
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
                }
            });
            bonusPane.getChildren().add(selectButton);
            Scene bonusScene = new Scene(bonusPane, 400, 200);
            bonusStage.setTitle("Select bonus resources");
            bonusStage.setScene(bonusScene);
            bonusStage.show();
        }
    }
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
            waitingText.setPrefWidth(300);
            waitingText.setPrefHeight(23);
            waitingText.setId("waitingText");
            Platform.runLater(() -> dashboardPane.getChildren().add(waitingText));
        }
    }
    public void showDashboard(String nickname) {
        if(nickname.equals(gui.getNickname())) {
            ModelView.DashboardView dashboardView = modelView.getMyDashboard();
            if(gui.getNickname().equals(modelView.getCurrPlayer()) || modelView.getCurrPlayer().length() < 1) {
                ArrayList<Action> actions = gui.getValidActions();
                if (actions.size() == 0) {
                    showLeaders.setDisable(true);
                    marketButton.setDisable(true);
                    gridButton.setDisable(true);
                    endTurnButton.setDisable(true);
                    for(Button button : developmentButton) {
                        button.setDisable(true);
                    }
                } else {
                    marketButton.setDisable(!actions.contains(Action.TAKE_RESOURCE));

                    gridButton.setDisable(!actions.contains(Action.BUY_CARD));

                    if (actions.contains(Action.ACTIVATE_PRODUCTION)) {
                        resetProductions();
                    } else {
                        disableProductions();
                    }

                    showLeaders.setDisable(!actions.contains(Action.ACTIVATE_LEADER) && !actions.contains(Action.DISCARD_LEADER));
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
                gridButton.setDisable(true);
            }
        }
    }
    public void activateProduction(int index) {
        if(enabledProductions.contains(index-1)) {
            enabledProductions.remove(Integer.valueOf(index - 1));
            if(index <= 4)
                developmentButton[index-1].setText("Enable");
            if(index == 4) {
                basicProductionResImages.get(0).setDisable(false);
                basicProductionResImages.get(1).setDisable(false);
                basicProductionResImages.get(2).setDisable(false);
            }
            switch (index) {
                case 1 -> developmentImageSlot1.setStyle("-fx-opacity: 1");
                case 2 -> developmentImageSlot2.setStyle("-fx-opacity: 1");
                case 3 -> developmentImageSlot3.setStyle("-fx-opacity: 1");
            }
        }
        else {
            System.out.println("basicProductionResources");
            System.out.println(index);
            System.out.println(basicProductionResources[0]);
            System.out.println(basicProductionResources[1]);
            System.out.println(basicProductionResources[2]);
            if(index != 4 || (basicProductionResources[0]!= null && basicProductionResources[1]!= null && basicProductionResources[2]!= null)) {
                enabledProductions.add(index-1);
                if(index <= 4)
                    developmentButton[index-1].setText("Disable");
                if(index == 4) {
                    basicProductionResImages.get(0).setDisable(true);
                    basicProductionResImages.get(1).setDisable(true);
                    basicProductionResImages.get(2).setDisable(true);
                }
            }
            switch (index) {
                case 1 -> developmentImageSlot1.setStyle("-fx-opacity: 0.9");
                case 2 -> developmentImageSlot2.setStyle("-fx-opacity: 0.9");
                case 3 -> developmentImageSlot3.setStyle("-fx-opacity: 0.9");
            }

        }
        activateProductionsButton.setDisable(enabledProductions.size() == 0);
    }
    public void activateAllProductions() {
        ResourceMap totalResources = modelView.getMyDashboard().getTotalResources();
        ArrayList<Integer> productions = new ArrayList<>();
        ResourceMap requiredResources = new ResourceMap();
        for(Integer index : enabledProductions) {
            switch(index) {
                case 0:
                case 1:
                case 2:
                    int production = modelView.getMyDashboard().getActiveDevelopments().get(index);
                    productions.add(production);
                    Card card = JsonCardsCreator.generateCard(production);
                    if(card instanceof ProductionLeaderCard) requiredResources.addResources(((ProductionLeaderCard) card).getProduction().getInputResource());
                    else if(card instanceof DevelopmentCard) requiredResources.addResources(((DevelopmentCard) card).getProduction().getInputResource());
                    break;
                case 3:
                    requiredResources.modifyResource(basicProductionResources[0], 1);
                    requiredResources.modifyResource(basicProductionResources[1], 1);

                    break;
                default:
                    break;
                case 4:
                case 5:
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
            gui.basicActionPlayed();
            showDashboard(gui.getNickname());
        }
        else {
            gui.showAlert("There are not enough resources to activate all enabled production", Alert.AlertType.ERROR);
        }

    }
    public void resetProductions() {
        for (Button button : developmentButton) {
            if(button != null) {
                button.setText("Enable");
                button.setDisable(false);
            }
        }
        enabledProductions.clear();
        if (developmentImageSlot1 != null)
           developmentImageSlot1.setStyle("-fx-opacity: 1");
        if (developmentImageSlot2 != null)
            developmentImageSlot2.setStyle("-fx-opacity: 1");
        if (developmentImageSlot3 != null)
            developmentImageSlot3.setStyle("-fx-opacity: 1");
        for(ComboBox<ImageView> basicProduction : basicProductionResImages) {
            basicProduction.setDisable(false);
        }
        activateProductionsButton.setDisable(true);
    }
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
    public void showActiveDevelopments(ArrayList<Integer> activeDevelopments) {
        Platform.runLater(() -> {
            dashboardPane.getChildren().remove(dashboardPane.lookup("#developmentButton1"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#developmentButton2"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#developmentButton3"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#developmentButton4"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#developmentImageSlot1"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#developmentImageSlot2"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#developmentImageSlot3"));
        });
        developmentButton = new Button[4];
        Image[] devImages = new Image[3];
        int xStart = 410, yStart = 320;
        double len = 140; double margin = 20;

        developmentButton[3] = new Button("Enable");
        developmentButton[3].setLayoutX(300);
        developmentButton[3].setLayoutY(570);
        developmentButton[3].setPrefWidth(len*2/3);
        developmentButton[3].setPrefHeight(len/6);
        developmentButton[3].setOnAction(event -> activateProduction(4));
        developmentButton[3].setId("developmentButton4");
        developmentButton[3].setDisable(!modelView.getCurrPlayer().equals(gui.getNickname()) && !(modelView.getCurrPlayer().length() < 1));
        Platform.runLater(() -> dashboardPane.getChildren().add(developmentButton[3]));

        for (int slot=0; slot<3; slot++) {
            developmentButton[slot] = new Button("Enable");
            developmentButton[slot].setLayoutX(410+len/6+(len+margin)*slot);
            developmentButton[slot].setLayoutY(340+len*3/2);
            developmentButton[slot].setPrefWidth(len*2/3);
            developmentButton[slot].setPrefHeight(len/6);
            int finalSlot = slot;
            developmentButton[slot].setOnAction(event -> activateProduction(finalSlot+1));
            developmentButton[slot].setId("developmentButton" + (slot+1));
            Platform.runLater(() -> dashboardPane.getChildren().add(developmentButton[finalSlot]));

            if (activeDevelopments.get(slot) != null) {
                devImages[slot] = new Image("/view/images/developments/developmentCard"+activeDevelopments.get(slot)+".png");
            } else {
                devImages[slot] = new Image("/view/images/developments/developmentCardEmpty.png");
                developmentButton[slot].setDisable(true);
            }


        }
        developmentImageSlot1 = new ImageView(devImages[0]);
        developmentImageSlot1.setLayoutX(xStart);
        developmentImageSlot1.setLayoutY(yStart);
        developmentImageSlot1.setFitWidth(len);
        developmentImageSlot1.setFitHeight(len*3/2);
        developmentImageSlot1.setId("developmentImageSlot1");
        Platform.runLater(() -> dashboardPane.getChildren().add(developmentImageSlot1));

        developmentImageSlot2 = new ImageView(devImages[1]);
        developmentImageSlot2.setLayoutX(xStart+len+margin);
        developmentImageSlot2.setLayoutY(yStart);
        developmentImageSlot2.setFitWidth(len);
        developmentImageSlot2.setFitHeight(len*3/2);
        developmentImageSlot2.setId("developmentImageSlot2");
        Platform.runLater(() -> dashboardPane.getChildren().add(developmentImageSlot2));

        developmentImageSlot3 = new ImageView(devImages[2]);
        developmentImageSlot3.setLayoutX(xStart+(len+margin)*2);
        developmentImageSlot3.setLayoutY(yStart);
        developmentImageSlot3.setFitWidth(len);
        developmentImageSlot3.setFitHeight(len*3/2);
        developmentImageSlot3.setId("developmentImageSlot3");
        Platform.runLater(() -> dashboardPane.getChildren().add(developmentImageSlot3));

        if(!gui.getValidActions().contains(Action.ACTIVATE_PRODUCTION)) {
           for(Button button : developmentButton)
               button.setDisable(true);
        }
    }

    private void showStrongbox(ResourceMap strongbox) {
        int xStart = 120, yStart = 515;
        double len = 40; double padding = 20;
        int resCont = 0;
        for (Map.Entry<Resource,Integer> value  : strongbox.getResources().entrySet() ) {

            String amount = "x" + value.getValue();
            Image resourceImage = new Image(getImage(value.getKey()));
            ImageView strongboxResourceView = new ImageView(resourceImage);
            strongboxResourceView.setLayoutX(xStart+(len+padding*3/2)*(resCont%2));
            int half = resCont/2;
            strongboxResourceView.setLayoutY(yStart+(len+padding)*half);
            strongboxResourceView.setFitWidth(len);
            strongboxResourceView.setFitHeight(len);
            strongboxResourceView.setId("strongBoxImage"+resCont);
            if(amountLabel != null && amountLabel[resCont] != null) {
                int finalResCont = resCont;
                Platform.runLater(() -> {
                    dashboardPane.getChildren().remove(dashboardPane.lookup("#strongBoxImage"+finalResCont));
                    dashboardPane.getChildren().remove(dashboardPane.lookup("#strongboxLabel"+finalResCont));

                });
            }
            if(amountLabel == null) {
                amountLabel = new Label[4];
            }
            amountLabel[resCont] = new Label(amount);
            amountLabel[resCont].setTextFill(Color.web("white"));
            amountLabel[resCont].setLayoutX(strongboxResourceView.getLayoutX()+len/2);
            amountLabel[resCont].setLayoutY(strongboxResourceView.getLayoutY()+len);
            amountLabel[resCont].setMinWidth(strongboxResourceView.getFitWidth());
            amountLabel[resCont].setMinHeight(20);
            amountLabel[resCont].setId("strongboxLabel"+resCont);
            int finalResCont1 = resCont;
            Platform.runLater(() -> {
                dashboardPane.getChildren().add(amountLabel[finalResCont1]);
                dashboardPane.getChildren().add(strongboxResourceView);
            });
            resCont++;
        }

    }

    private void showWarehouse(ArrayList<Resource> warehouse, ArrayList<Resource> extraShelfResources) {

        Platform.runLater(() -> {
            for(int i=0; i<ShelfView.length; i++) {
                dashboardPane.getChildren().remove(dashboardPane.lookup("#shelf" + (i)));
            }
            for(int i=0; i<ExtraShelfView.length; i++) {
                dashboardPane.getChildren().remove(dashboardPane.lookup("#extraShelf" + i));
            }

        });


        double len = 30;
        int xStart = 200, yStart = 335, yOffset = 55;

        for(int i = 0; i < extraShelfResources.size()*2; i++) {
            int xBool = (i+1)%2;
            int yBool = (i/2);
            ExtraShelfView[i] = new ImageView(new Image(getImage(warehouse.get(6+i) == null ? extraShelfResources.get(0) : warehouse.get(6+i))));
            ExtraShelfView[i] .setStyle("-fx-opacity: " + (warehouse.get(6+i) == null ? "0.5" : "1"));
            ExtraShelfView[i] .setLayoutX(360-(len+20)*xBool);
            ExtraShelfView[i] .setLayoutY(yStart+(len+20)*yBool);
            ExtraShelfView[i] .setFitWidth(len);
            ExtraShelfView[i] .setFitHeight(len);
            ExtraShelfView[i] .setPickOnBounds(true);
            int finalI = i;
            ExtraShelfView[i] .setOnMouseClicked((MouseEvent e) -> handleShelfClick(6+ finalI));
            ExtraShelfView[i] .setId("extraShelf" + i);
            Platform.runLater(() -> dashboardPane.getChildren().add(ExtraShelfView[finalI]));
        }

        double[] shelfLayoutX = {xStart-len/2, xStart-len/4-len, xStart+len/4, xStart-len - len/2 -len/4, xStart-len/2, xStart+len/2+len/4 };
        for(int i = 0; i < ShelfView.length; i++) {
            int row =(i+1)/2-i/5;
            ShelfView[i] = new ImageView( new Image(getImage(warehouse.get(i))));
            ShelfView[i].setLayoutX(shelfLayoutX[i]);
            ShelfView[i].setLayoutY(yStart+yOffset*row);
            ShelfView[i].setFitWidth(len);
            ShelfView[i].setFitHeight(len);
            ShelfView[i].setPickOnBounds(true);
            int finalI = i;
            ShelfView[i].setOnMouseClicked((MouseEvent e) -> handleShelfClick(finalI));
            ShelfView[i].setId("shelf" + (i));
            Platform.runLater(() -> dashboardPane.getChildren().add(ShelfView[finalI]));
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
            dashboardPane.getChildren().remove(dashboardPane.lookup("#temporaryShelfImage"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#temporaryShelfButton"));
        });

        if ( gui.showTempShelf ) {
            marketButton.setDisable(true);
            gridButton.setDisable(true);
                Image image = new Image("/view/images/resources/temporaryShelfEmpty.png");
                temporaryShelfImage = new ImageView(image);
                temporaryShelfImage.setLayoutX(300);
                temporaryShelfImage.setLayoutY(500);
                temporaryShelfImage.setFitWidth(500);
                temporaryShelfImage.setFitHeight(142);
                temporaryShelfImage.setId("temporaryShelfImage");

                Platform.runLater(() -> dashboardPane.getChildren().add(temporaryShelfImage));

                tempShelfButton = new Button("Done");
                tempShelfButton.setLayoutX(810);
                tempShelfButton.setLayoutY(612);
                tempShelfButton.setPrefWidth(60);
                tempShelfButton.setPrefHeight(30);
                tempShelfButton.setDisable(endTurnButton.isDisabled());
                tempShelfButton.setId("temporaryShelfButton");
                tempShelfButton.setOnMouseClicked((MouseEvent e) -> {
                    gui.showTempShelf = false;
                    gui.send(new SetWarehouse(modelView.getMyDashboard().getWarehouse()));
                    gui.basicActionPlayed();
                    showDashboard(gui.getNickname());
                });

                Platform.runLater(() -> dashboardPane.getChildren().add(tempShelfButton));

                for(int i =0; i<tempShelfImageViews.length; i++) {
                    Image tempShelfImage = new Image(getImage(warehouse.get(10+i)));

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
    }

    private void handleShelfClick(int index) {
        if(gui.showTempShelf) {
            if (swapIndex < 0) {
                swapIndex = index;
            } else {
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
    private void showFaithTrack(Integer faithMarker, Integer blackMarker, Boolean[] popesFavorTiles) {

        Image faithImage = new Image("/view/images/faithTrack/cross.png");

         for(int i = 0; i<popesFavorTiles.length; i++) {
             popeFavorTilesImages[i] = new ImageView(new Image("/view/images/faithTrack/pope_favor"+(i+1) + (popesFavorTiles[i] ? "_front": "_back" )+".png"));
             popeFavorTilesImages[i].setLayoutX(290+222.5*i);
             popeFavorTilesImages[i].setLayoutY(160);
             popeFavorTilesImages[i].setFitWidth(60);
             popeFavorTilesImages[i].setFitHeight(60);
             popeFavorTilesImages[i].setId("popeFavorTilesImages" + (i+1));
         }
        popeFavorTilesImages[1].setLayoutX(491);
        popeFavorTilesImages[1].setLayoutY(120);
        popeFavorTilesImages[1].setFitWidth(60);
        popeFavorTilesImages[1].setFitHeight(60);


        Platform.runLater(() -> {
            Platform.runLater(() -> dashboardPane.getChildren().remove(dashboardPane.lookup("#" + popeFavorTilesImages[0].getId())));
            dashboardPane.getChildren().add(popeFavorTilesImages[0]);
        });
        Platform.runLater(() -> {
            Platform.runLater(() -> dashboardPane.getChildren().remove(dashboardPane.lookup("#" + popeFavorTilesImages[1].getId())));
            dashboardPane.getChildren().add(popeFavorTilesImages[1]);
        });
        Platform.runLater(() -> {
            Platform.runLater(() -> dashboardPane.getChildren().remove(dashboardPane.lookup("#" + popeFavorTilesImages[2].getId())));
            dashboardPane.getChildren().add(popeFavorTilesImages[2]);
        });

        faithTrackImage = new ImageView(faithImage);
        int xOffset = 80;
        int yOffset = 190;

        int[] xStart = {40,80,120,120,120,160,200,240,280,320,320,320,360, 400,440,480, 520, 520, 520, 560, 600, 640, 680, 720, 760, 800, 840};
        int[] yStart = {0, 0, 0, -40, -80, -80 ,-80 ,-80, -80 ,-80, -40, 0, 0, 0, 0, 0, 0, -40, -80, -80, -80 ,-80 , -80 , -80 ,-80, -80};
        int len = 40;

        faithTrackImage.setLayoutX(xOffset+xStart[faithMarker]);
        faithTrackImage.setLayoutY(yOffset+yStart[faithMarker]);
        faithTrackImage.setFitWidth(len);
        faithTrackImage.setFitHeight(len);

        if ( modelView.getDashboards().size() == 1 ) {
            Image blackFaithImage = new Image("/view/images/faithTrack/blackCross.png");

            if (blackFaithTrackImage != null) {
                Platform.runLater(() -> dashboardPane.getChildren().remove(dashboardPane.lookup("#blackFaithTrackImage")));
            }
            blackFaithTrackImage = new ImageView(blackFaithImage);

            blackFaithTrackImage.setLayoutX(xOffset+xStart[blackMarker]);
            blackFaithTrackImage.setLayoutY(yOffset+yStart[blackMarker]);
            blackFaithTrackImage.setFitWidth(len);
            blackFaithTrackImage.setFitHeight(len);

            if(faithMarker.equals(blackMarker)) {
                double halfLen = (double)(len)/2;
                faithTrackImage.setFitWidth(halfLen);
                faithTrackImage.setFitHeight(halfLen);
                blackFaithTrackImage.setLayoutX(blackFaithTrackImage.getLayoutX()+halfLen);
                blackFaithTrackImage.setLayoutY(blackFaithTrackImage.getLayoutY()+halfLen);
                blackFaithTrackImage.setFitWidth(halfLen);
                blackFaithTrackImage.setFitHeight(halfLen);
            }

            blackFaithTrackImage.setId("blackFaithTrackImage");
            Platform.runLater(() -> dashboardPane.getChildren().add(blackFaithTrackImage));
        }

        Platform.runLater(() -> dashboardPane.getChildren().remove(dashboardPane.lookup("#faithTrackImage")));
        faithTrackImage.setId("faithTrackImage");
        Platform.runLater(() -> dashboardPane.getChildren().add(faithTrackImage));

    }

    public void showMarket() {
        marketController = showPopup("/view/scenes/sceneMarket.fxml").getController();
        marketController.setGUI(gui);
        marketController.setMarketTray(modelView.getMarketTray());
        marketController.start();
        marketController.checkActiveWhiteMarbleLeaders(modelView.getMyDashboard().getLeaderCards());
    }

    public void showGrid() {
        gridController = showPopup("/view/scenes/sceneGrid.fxml").getController();
        gridController.setGUI(gui);
        gridController.setup(modelView);
    }

    private FXMLLoader showPopup(String scenePath) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(scenePath));
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(gui.mainStage);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }

    public void setModelView(ModelView modelView) {
        this.modelView = modelView; 
    }
    public void startTurn() {
        for(ComboBox<ImageView> basicProduction : basicProductionResImages) {
            basicProduction.valueProperty().set(null);
        }
        endTurnButton.setDisable(!gui.getValidActions().contains(Action.END_TURN));
        showDashboard(gui.getNickname());
    }
    public void endTurn() {
        if (gui.showTempShelf) {
            gui.showTempShelf = false;
            gui.send(new SetWarehouse(modelView.getMyDashboard().getWarehouse()));

            Platform.runLater(() -> {
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView1"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView2"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView3"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView4"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#temporaryShelfImage"));
            });

        }
        gui.send(new EndTurn());
        endTurnButton.setDisable(true);
        marketButton.setDisable(true);
        gridButton.setDisable(true);
        for(Button button : developmentButton) {
            button.setDisable(true);
        }
        showDashboard(gui.getNickname());
        handleWaitingText();
    }

    public void handleTemporaryShelf() {
        showDashboard(gui.getNickname());
    }

    public void basicProductionResChange(ActionEvent actionEvent) {
        @SuppressWarnings("unchecked")
        ComboBox<ImageView> comboBox = (ComboBox<ImageView>) actionEvent.getSource();
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

    public void showEndGameText() {
        Label endGameLabel = new Label("The game is about to finish.Waiting for the remaining players to play theirs last turn...");
        endGameLabel.setLayoutX(88);
        endGameLabel.setLayoutY(dashboardPane.getHeight()-30);
        endGameLabel.setTextFill(Color.web("red"));
        endGameLabel.setPrefHeight(30);
        endGameLabel.setPrefWidth(dashboardPane.getWidth()-176);
        endGameLabel.setId("endGameLabel");
        Platform.runLater(() -> {
            dashboardPane.getChildren().remove(dashboardPane.lookup("#endGameLabel"));
            dashboardPane.getChildren().add(endGameLabel);
        });
    }

    public void showStats(Map<String, Integer> map) {
        dashboardPane.setDisable(true);

        Platform.runLater(() -> {
        gameOverController = showPopup("/view/scenes/sceneGameOver.fxml").getController();
        gameOverController.setMap(map);
        gameOverController.start();
        });
    }
    public void singlePlayerEnd(int points) {
        dashboardPane.setDisable(true);
        Platform.runLater(() -> {
            gameOverController = showPopup("/view/scenes/sceneGameOver.fxml").getController();
            if(points > 0) {
                gameOverController.setPoints(points);
            }
            gameOverController.start();
        });
    }

    public void showToken(int index) {
        Image tokenImage = new Image("/view/images/tokens/cerchio"+index+".png");
        ImageView tokenImageView = new ImageView(tokenImage);
        tokenImageView.setLayoutX(98);
        tokenImageView.setLayoutY(90);
        tokenImageView.setFitWidth(50);
        tokenImageView.setFitHeight(50);
        Platform.runLater(() -> {
            dashboardPane.getChildren().remove(dashboardPane.lookup("#endGameLabel"));
            dashboardPane.getChildren().add(tokenImageView);
        });
    }
}
