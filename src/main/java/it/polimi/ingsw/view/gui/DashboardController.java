package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.events.clientmessages.EndTurn;
import it.polimi.ingsw.events.clientmessages.SendBonusResources;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.view.ModelView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class DashboardController {
    @FXML Button marketButton;
    @FXML Button gridButton;
    @FXML ChoiceBox<String> playersChoiceBox;
    @FXML Pane dashboardPane;
    @FXML Button endTurnButton;
    private ModelView modelView;
    MarketController marketController;
    GridController gridController;

    private static GUI gui;
    private ImageView firstShelfRes1View;
    private ImageView secondShelfRes1View;
    private ImageView secondShelfRes2View;
    private ImageView thirdShelfRes1View;
    private ImageView thirdShelfRes2View;
    private ImageView thirdShelfRes3View;

    ImageView faithTrackImage;
    ImageView blackFaithTrackImage;

    ImageView developmentImageSlot1;
    ImageView developmentImageSlot2;
    ImageView developmentImageSlot3;

    Label[] amountLabel = new Label [4];

    public void setGUI(GUI gui) {
        DashboardController.gui = gui;
    }

    public void setup() {
        amountLabel = new Label[4];
        for(ModelView.DashboardView dashboard : modelView.getDashboards()) {
            playersChoiceBox.getItems().add(dashboard.getNickname());
        }
        playersChoiceBox.setValue(gui.getNickname());

        endTurnButton.setDisable(true);
        showDashboard();
    }

    public void choiceBoxChange(ActionEvent actionEvent) {
        String playerSelected = playersChoiceBox.getSelectionModel().getSelectedItem();
        if(playerSelected != null && playerSelected.equals(gui.getNickname())) {
            marketButton.setDisable(false);
            gridButton.setDisable(false);
            showDashboard();
        }
        else {
            marketButton.setDisable(true);
            gridButton.setDisable(true);
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
                    showDashboard();
                }
            });
            bonusPane.getChildren().add(selectButton);
            Scene bonusScene = new Scene(bonusPane, 400, 200);
            bonusStage.setTitle("Select bonus resources");
            bonusStage.setScene(bonusScene);
            bonusStage.show();
        }
    }
    public void showDashboard() {
        ModelView.DashboardView dashboardView = modelView.getMyDashboard();
        showFaithTrack(dashboardView.getFaithMarker(), dashboardView.getBlackMarker(), dashboardView.getPopesFavorTiles());
        showStrongbox(dashboardView.getStrongbox());
        showLeaders(dashboardView.getLeaderCards());
        showActiveDevelopments(dashboardView.getActiveDevelopments());
        showWarehouse(dashboardView.getWarehouse(), dashboardView.getExtraShelfResources());
    }
    public void showActiveDevelopments(ArrayList<Integer> activeDevelopments) {
        int slot = 0;
        if (developmentImageSlot1 != null)
            Platform.runLater(() -> dashboardPane.getChildren().remove(developmentImageSlot1));
        if (developmentImageSlot2 != null)
            Platform.runLater(() -> dashboardPane.getChildren().remove(developmentImageSlot2));
        if (developmentImageSlot3 != null)
            Platform.runLater(() -> dashboardPane.getChildren().remove(developmentImageSlot3));
        Image[] devImages = new Image[3];
        for (Integer id: activeDevelopments) {
//            System.out.printf("\nSLOT #%d\n", slot + 1);
            if (id != null) {
                devImages[slot] = new Image("/view/images/developments/developmentCard"+id+".png");
            } else {
                devImages[slot] = new Image("/view/images/developments/developmentCardEmpty.png");
            }
            slot++;
        }
        int xStart = 410, yStart = 320;
        double len = 140; double margin = 20;
        developmentImageSlot1 = new ImageView(devImages[0]);
        developmentImageSlot1.setLayoutX(xStart);
        developmentImageSlot1.setLayoutY(yStart);
        developmentImageSlot1.setFitWidth(len);
        developmentImageSlot1.setFitHeight(len*3/2);
        Platform.runLater(() -> dashboardPane.getChildren().add(developmentImageSlot1));

        developmentImageSlot2 = new ImageView(devImages[1]);
        developmentImageSlot2.setLayoutX(xStart+len+margin);
        developmentImageSlot2.setLayoutY(yStart);
        developmentImageSlot2.setFitWidth(len);
        developmentImageSlot2.setFitHeight(len*3/2);
        Platform.runLater(() -> dashboardPane.getChildren().add(developmentImageSlot2));

        developmentImageSlot3 = new ImageView(devImages[2]);
        developmentImageSlot3.setLayoutX(xStart+(len+margin)*2);
        developmentImageSlot3.setLayoutY(yStart);
        developmentImageSlot3.setFitWidth(len);
        developmentImageSlot3.setFitHeight(len*3/2);
        Platform.runLater(() -> dashboardPane.getChildren().add(developmentImageSlot3));
    }
    public static void showLeaders(Map<Integer,Boolean> leaders){
//        for (Integer id: leaders.keySet()){
//            if (leaders.get(id)) {
//                System.out.println(Colors.ANSI_GREEN_BOLD + "\nACTIVE" + Colors.ANSI_RESET);
//            } else {
//                System.out.println(Colors.ANSI_RED + "\nNOT ACTIVE" + Colors.ANSI_RESET);
//            }
//            showCards(Set.of(id));
//        }
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
            if(amountLabel != null && amountLabel[resCont] != null) {
                int finalResCont = resCont;
                Platform.runLater(() -> dashboardPane.getChildren().remove(amountLabel[finalResCont]));
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

            int finalResCont1 = resCont;
            Platform.runLater(() -> dashboardPane.getChildren().add(amountLabel[finalResCont1]));
            Platform.runLater(() -> dashboardPane.getChildren().add(strongboxResourceView));
            resCont++;
        }

    }

    private void showWarehouse(ArrayList<Resource> warehouse, ArrayList<Resource> extraShelfResources) {

        Platform.runLater(() -> dashboardPane.getChildren().remove(firstShelfRes1View));
        Platform.runLater(() -> dashboardPane.getChildren().remove(secondShelfRes1View));
        Platform.runLater(() -> dashboardPane.getChildren().remove(secondShelfRes2View));
        Platform.runLater(() -> dashboardPane.getChildren().remove(thirdShelfRes1View));
        Platform.runLater(() -> dashboardPane.getChildren().remove(thirdShelfRes2View));
        Platform.runLater(() -> dashboardPane.getChildren().remove(thirdShelfRes3View));


        Image firstShelfRes1 = new Image(getImage(warehouse.get(0)));
        Image secondShelfRes1 = new Image(getImage(warehouse.get(1)));
        Image secondShelfRes2 = new Image(getImage(warehouse.get(2)));
        Image thirdShelfRes1 = new Image(getImage(warehouse.get(3)));
        Image thirdShelfRes2 = new Image(getImage(warehouse.get(4)));
        Image thirdShelfRes3 = new Image(getImage(warehouse.get(5)));


        double len = 30;
        int xStart = 200, yStart = 335, yOffset = 55;
        firstShelfRes1View = new ImageView(firstShelfRes1);
        firstShelfRes1View.setLayoutX(xStart-len/2);
        firstShelfRes1View.setLayoutY(yStart);
        firstShelfRes1View.setFitWidth(len);
        firstShelfRes1View.setFitHeight(len);
        Platform.runLater(() -> dashboardPane.getChildren().add(firstShelfRes1View));

        secondShelfRes1View = new ImageView(secondShelfRes1);
        secondShelfRes1View.setLayoutX(xStart-len/4-len);
        secondShelfRes1View.setLayoutY(yStart+yOffset);
        secondShelfRes1View.setFitWidth(len);
        secondShelfRes1View.setFitHeight(len);
        Platform.runLater(() -> dashboardPane.getChildren().add(secondShelfRes1View));

        secondShelfRes2View = new ImageView(secondShelfRes2);
        secondShelfRes2View.setLayoutX(xStart+len/4);
        secondShelfRes2View.setLayoutY(yStart+yOffset);
        secondShelfRes2View.setFitWidth(len);
        secondShelfRes2View.setFitHeight(len);
        Platform.runLater(() -> dashboardPane.getChildren().add(secondShelfRes2View));

        thirdShelfRes1View = new ImageView(thirdShelfRes1);
        thirdShelfRes1View.setLayoutX(xStart-len - len/2 -len/4);
        thirdShelfRes1View.setLayoutY(yStart+yOffset*2);
        thirdShelfRes1View.setFitWidth(len);
        thirdShelfRes1View.setFitHeight(len);
        Platform.runLater(() -> dashboardPane.getChildren().add(thirdShelfRes1View));

        thirdShelfRes2View = new ImageView(thirdShelfRes2);
        thirdShelfRes2View.setLayoutX(xStart-len/2);
        thirdShelfRes2View.setLayoutY(yStart+yOffset*2);
        thirdShelfRes2View.setFitWidth(len);
        thirdShelfRes2View.setFitHeight(len);
        Platform.runLater(() -> dashboardPane.getChildren().add(thirdShelfRes2View));

        thirdShelfRes3View = new ImageView(thirdShelfRes3);
        thirdShelfRes3View.setLayoutX(xStart+len/2+len/4);
        thirdShelfRes3View.setLayoutY(yStart+yOffset*2);
        thirdShelfRes3View.setFitWidth(len);
        thirdShelfRes3View.setFitHeight(len);
        Platform.runLater(() -> dashboardPane.getChildren().add(thirdShelfRes3View));

//        ArrayList<Resource> temporaryShelf = new ArrayList<>();
        ResourceMap temporaryShelf = new ResourceMap();
        for(int i = 10; i<=14; i++) {
            Resource temp = warehouse.get(i-1);
            if ( temp != null) {
                temporaryShelf.modifyResource(temp, 1);
            }
        }
        if ( temporaryShelf.getAmount() > 0 ) {
            // 200 , 530
            Image image = new Image("/view/images/resources/temporaryShelf.png");
            ImageView temporaryShelfImage = new ImageView(image);
            temporaryShelfImage.setLayoutX(300);
            temporaryShelfImage.setLayoutY(500);
            temporaryShelfImage.setFitWidth(500);
            temporaryShelfImage.setFitHeight(142);
            Label coinLabel = new Label("x " + temporaryShelf.getResource(Resource.COIN));
            coinLabel.setLayoutX(360);
            coinLabel.setLayoutY(600);
            coinLabel.setPrefWidth(30);
            coinLabel.setPrefHeight(30);
            coinLabel.setTextFill(Color.web("white"));
            Label stoneLabel = new Label("x " + temporaryShelf.getResource(Resource.STONE));
            stoneLabel.setLayoutX(485);
            stoneLabel.setLayoutY(600);
            stoneLabel.setPrefWidth(30);
            stoneLabel.setPrefHeight(30);
            stoneLabel.setTextFill(Color.web("white"));
            Label servantLabel = new Label("x " + temporaryShelf.getResource(Resource.SERVANT));
            servantLabel.setLayoutX(610);
            servantLabel.setLayoutY(600);
            servantLabel.setPrefWidth(30);
            servantLabel.setPrefHeight(30);
            servantLabel.setTextFill(Color.web("white"));
            Label shieldLabel = new Label("x " + temporaryShelf.getResource(Resource.SHIELD));
            shieldLabel.setLayoutX(735);
            shieldLabel.setLayoutY(600);
            shieldLabel.setPrefWidth(30);
            shieldLabel.setPrefHeight(30);
            shieldLabel.setTextFill(Color.web("white"));



            Platform.runLater(() -> dashboardPane.getChildren().add(temporaryShelfImage));
            Platform.runLater(() -> dashboardPane.getChildren().add(coinLabel));
            Platform.runLater(() -> dashboardPane.getChildren().add(stoneLabel));
            Platform.runLater(() -> dashboardPane.getChildren().add(servantLabel));
            Platform.runLater(() -> dashboardPane.getChildren().add(shieldLabel));


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
        if (faithTrackImage != null) {
            Platform.runLater(() -> dashboardPane.getChildren().remove(dashboardPane.lookup("#faithTrackImage")));
        }
        Image faithImage = new Image("/view/images/faithTrack/cross.png");

        faithTrackImage = new ImageView(faithImage);
        faithTrackImage.setId("#faithTrackImage");
        int xOffset = 80;
        int yOffset = 190;

        int[] xStart = {40,80,120,120,120,160,200,240,280,320,320,320,360, 400,440,480, 520, 520, 520, 560, 600, 640, 680, 720, 760, 800, 840};
        int[] yStart = {0, 0, 0, -40, -80, -80 ,-80 ,-80, -80 ,-80, -40, 0, 0, 0, 0, 0, 0, -40, -80, -80, -80 ,-80 , -80 , -80 ,-80, -80};
        int len = 40;

        faithTrackImage = new ImageView(faithImage);
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
            blackFaithTrackImage.setId("blackFaithTrackImage");

            blackFaithTrackImage.setLayoutX(xOffset+xStart[faithMarker]);
            blackFaithTrackImage.setLayoutY(yOffset+yStart[faithMarker]);
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


            Platform.runLater(() ->  dashboardPane.getChildren().add(blackFaithTrackImage));
        }

        ImageView finalFaithTrackImage = faithTrackImage;
        Platform.runLater(() -> dashboardPane.getChildren().add(finalFaithTrackImage));

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

    public void endTurn(ActionEvent actionEvent) {
        gui.send(new EndTurn());
    }

    public void handleTemporaryShelf() {
        showDashboard();
    }
}
