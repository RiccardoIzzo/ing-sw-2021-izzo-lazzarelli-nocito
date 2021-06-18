package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.model.JsonCardsCreator;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.ProductionLeaderCard;
import it.polimi.ingsw.view.ModelView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private static GUI gui;
    ImageView firstShelfRes1View;
    ImageView secondShelfRes1View;
    ImageView secondShelfRes2View;
    ImageView thirdShelfRes1View;
    ImageView thirdShelfRes2View;
    ImageView thirdShelfRes3View;

    ImageView temporaryShelfImage;
    ImageView tempShelfImageView1;
    ImageView tempShelfImageView2;
    ImageView tempShelfImageView3;
    ImageView tempShelfImageView4;
    ImageView faithTrackImage;
    ImageView blackFaithTrackImage;

    ImageView[] popeFavorTilesImages = new ImageView[3];

    ImageView developmentImageSlot1;
    ImageView developmentImageSlot2;
    ImageView developmentImageSlot3;
    Button[] developmentButton = new Button [3];

    Resource[] basicProductionResources = new Resource[3];
    ArrayList<Integer> enabledProductions = new ArrayList<>();
    @FXML ComboBox<ImageView> basicProductionRes1;
    @FXML ComboBox<ImageView> basicProductionRes2;
    @FXML ComboBox<ImageView> basicProductionRes3;
    Label[] amountLabel = new Label [4];

    Integer swapIndex = -1;
    public void setGUI(GUI gui) {
        DashboardController.gui = gui;
    }

    public void setup() {
        amountLabel = new Label[4];
        for(ModelView.DashboardView dashboard : modelView.getDashboards()) {
            playersChoiceBox.getItems().add(dashboard.getNickname());
        }
        playersChoiceBox.setValue(gui.getNickname());
        basicProductionResources = new Resource[3];
        activateProductionsButton.setDisable(true);
        enabledProductions = new ArrayList<>();
        String[] resources = {"coin2", "servant2" , "shield2" , "stone2"};
        ObservableList<String> options = FXCollections.observableArrayList();

        basicProductionRes1.setStyle("-fx-opacity: 1;");
        basicProductionRes2.setStyle("-fx-opacity: 1;");
        basicProductionRes3.setStyle("-fx-opacity: 1;");
        for (String resource : resources) {
            Image resourceImage = new Image("/view/images/resources/" + resource + ".png");
            ImageView resourceImageView1 = new ImageView(resourceImage);
            resourceImageView1.setFitWidth(22);
            resourceImageView1.setFitHeight(22);
            ImageView resourceImageView2 = new ImageView(resourceImage);
            resourceImageView2.setFitWidth(22);
            resourceImageView2.setFitHeight(22);
            ImageView resourceImageView3 = new ImageView(resourceImage);
            resourceImageView3.setFitWidth(22);
            resourceImageView3.setFitHeight(22);

            basicProductionRes1.getItems().add(resourceImageView1);
            basicProductionRes2.getItems().add(resourceImageView2);
            basicProductionRes3.getItems().add(resourceImageView3);
        }

        basicProductionRes1.setCellFactory(listView -> new ListCell<>() {

            private ImageView imageView = new ImageView();

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

        basicProductionRes2.setCellFactory(listView -> new ListCell<>() {

            private ImageView imageView = new ImageView();

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

        basicProductionRes3.setCellFactory(listView -> new ListCell<>() {

            private ImageView imageView = new ImageView();

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
        endTurnButton.setDisable(true);
        showDashboard();
    }

    public void showLeaders(ActionEvent actionEvent) {

        leadersController = showPopup("/view/scenes/sceneLeaders.fxml").getController();
        leadersController.setup(modelView.getMyDashboard().getLeaderCards());
        leadersController.setGUI(gui);
        leadersController.start();
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


//    showLeaders(dashboardView.getLeaderCards());
//    public static void showLeaders(Map<Integer,Boolean> leaders){
////        for (Integer id: leaders.keySet()){
////            if (leaders.get(id)) {
////                System.out.println(Colors.ANSI_GREEN_BOLD + "\nACTIVE" + Colors.ANSI_RESET);
////            } else {
////                System.out.println(Colors.ANSI_RED + "\nNOT ACTIVE" + Colors.ANSI_RESET);
////            }
////            showCards(Set.of(id));
////        }
//    }

    public class StatusListCell extends ListCell<String> {
        protected void updateItem(String item, boolean empty){
            super.updateItem(item, empty);
            setGraphic(null);
            setText(null);
            if(item!=null){
                ImageView imageView = new ImageView(new Image(item));
                imageView.setFitWidth(22);
                imageView.setFitHeight(22);
                setGraphic(imageView);
            }
        }

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
        showActiveDevelopments(dashboardView.getActiveDevelopments());
        showWarehouse(dashboardView.getWarehouse(), dashboardView.getExtraShelfResources());
    }
    public void activateProduction(int index) {
        if(enabledProductions.contains(index-1)) {
            enabledProductions.remove(Integer.valueOf(index - 1));
            developmentButton[index-1].setText("Enable");
            switch (index) {
                case 1 -> developmentImageSlot1.setStyle("-fx-opacity: 1");
                case 2 -> developmentImageSlot2.setStyle("-fx-opacity: 1");
                case 3 -> developmentImageSlot3.setStyle("-fx-opacity: 1");
            }
            if(index ==4) {
                basicProductionRes1.setDisable(false);
                basicProductionRes2.setDisable(false);
                basicProductionRes3.setDisable(false);
            }
        }
        else {
            if(index != 4 || (basicProductionResources[0]!= null && basicProductionResources[1]!= null && basicProductionResources[2]!= null)) {
                enabledProductions.add(index-1);
                developmentButton[index-1].setText("Disable");
                if(index ==4) {
                    basicProductionRes1.setDisable(true);
                    basicProductionRes2.setDisable(true);
                    basicProductionRes3.setDisable(true);
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
    public void activateAllProductions(ActionEvent actionEvent) {
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
            }
        }
        if(totalResources.removeResources(requiredResources)) {
            gui.send(new ActivateProduction(productions));
            basicActionPlayed();
            showDashboard();
        }
        else {
            gui.showAlert("There are not enough resources to activate all enabled production", Alert.AlertType.ERROR);
        }

    }
    public void basicActionPlayed() {
    }
    public void showActiveDevelopments(ArrayList<Integer> activeDevelopments) {
        int slot = 0;
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
        developmentButton[3].setId("developmentButton" + (4));
        Platform.runLater(() -> dashboardPane.getChildren().add(developmentButton[3]));


        for (Integer id: activeDevelopments) {
            developmentButton[slot] = new Button("Enable");
            developmentButton[slot].setLayoutX(410+len/6+(len+margin)*slot);
            developmentButton[slot].setLayoutY(340+len*3/2);
            developmentButton[slot].setPrefWidth(len*2/3);
            developmentButton[slot].setPrefHeight(len/6);
            int finalSlot = slot;
            developmentButton[slot].setOnAction(event -> activateProduction(finalSlot+1));
            developmentButton[slot].setId("developmentButton" + (slot+1));
            Platform.runLater(() -> dashboardPane.getChildren().add(developmentButton[finalSlot]));

            if (id != null) {
                devImages[slot] = new Image("/view/images/developments/developmentCard"+id+".png");
                developmentButton[slot].setDisable(false);

            } else {
                devImages[slot] = new Image("/view/images/developments/developmentCardEmpty.png");
                developmentButton[slot].setDisable(true);
            }



            slot++;
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
            dashboardPane.getChildren().remove(dashboardPane.lookup("#firstShelfRes1View"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#secondShelfRes1View"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#secondShelfRes2View"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#thirdShelfRes1View"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#thirdShelfRes2View"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#thirdShelfRes3View"));
        });


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
        firstShelfRes1View.setPickOnBounds(true);
        firstShelfRes1View.setOnMouseClicked((MouseEvent e) -> handleShelfClick(0));
        firstShelfRes1View.setId("firstShelfRes1View");
        Platform.runLater(() -> dashboardPane.getChildren().add(firstShelfRes1View));

        secondShelfRes1View = new ImageView(secondShelfRes1);
        secondShelfRes1View.setLayoutX(xStart-len/4-len);
        secondShelfRes1View.setLayoutY(yStart+yOffset);
        secondShelfRes1View.setFitWidth(len);
        secondShelfRes1View.setFitHeight(len);
        secondShelfRes1View.setPickOnBounds(true);
        secondShelfRes1View.setOnMouseClicked((MouseEvent e) -> handleShelfClick(1));
        secondShelfRes1View.setId("secondShelfRes1View");
        Platform.runLater(() -> dashboardPane.getChildren().add(secondShelfRes1View));

        secondShelfRes2View = new ImageView(secondShelfRes2);
        secondShelfRes2View.setLayoutX(xStart+len/4);
        secondShelfRes2View.setLayoutY(yStart+yOffset);
        secondShelfRes2View.setFitWidth(len);
        secondShelfRes2View.setFitHeight(len);
        secondShelfRes2View.setPickOnBounds(true);
        secondShelfRes2View.setOnMouseClicked((MouseEvent e) -> handleShelfClick(2));
        secondShelfRes2View.setId("secondShelfRes2View");
        Platform.runLater(() -> dashboardPane.getChildren().add(secondShelfRes2View));

        thirdShelfRes1View = new ImageView(thirdShelfRes1);
        thirdShelfRes1View.setLayoutX(xStart-len - len/2 -len/4);
        thirdShelfRes1View.setLayoutY(yStart+yOffset*2);
        thirdShelfRes1View.setFitWidth(len);
        thirdShelfRes1View.setFitHeight(len);
        thirdShelfRes1View.setPickOnBounds(true);
        thirdShelfRes1View.setOnMouseClicked((MouseEvent e) -> handleShelfClick(3));
        thirdShelfRes1View.setId("thirdShelfRes1View");
        Platform.runLater(() -> dashboardPane.getChildren().add(thirdShelfRes1View));

        thirdShelfRes2View = new ImageView(thirdShelfRes2);
        thirdShelfRes2View.setLayoutX(xStart-len/2);
        thirdShelfRes2View.setLayoutY(yStart+yOffset*2);
        thirdShelfRes2View.setFitWidth(len);
        thirdShelfRes2View.setFitHeight(len);
        thirdShelfRes2View.setPickOnBounds(true);
        thirdShelfRes2View.setOnMouseClicked((MouseEvent e) -> handleShelfClick(4));
        thirdShelfRes2View.setId("thirdShelfRes2View");
        Platform.runLater(() -> dashboardPane.getChildren().add(thirdShelfRes2View));

        thirdShelfRes3View = new ImageView(thirdShelfRes3);
        thirdShelfRes3View.setLayoutX(xStart+len/2+len/4);
        thirdShelfRes3View.setLayoutY(yStart+yOffset*2);
        thirdShelfRes3View.setFitWidth(len);
        thirdShelfRes3View.setFitHeight(len);
        thirdShelfRes3View.setPickOnBounds(true);
        thirdShelfRes3View.setOnMouseClicked((MouseEvent e) -> handleShelfClick(5));
        thirdShelfRes3View.setId("thirdShelfRes3View");
        Platform.runLater(() -> dashboardPane.getChildren().add(thirdShelfRes3View));

//        ArrayList<Resource> temporaryShelf = new ArrayList<>();
        ResourceMap temporaryShelf = new ResourceMap();
        for(int i = 10; i<=14; i++) {
            Resource temp = warehouse.get(i-1);
            if ( temp != null) {
                temporaryShelf.modifyResource(temp, 1);
            }
        }

        Platform.runLater(() -> {
            dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView1"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView2"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView3"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView4"));
            dashboardPane.getChildren().remove(dashboardPane.lookup("#temporaryShelfImage"));
        });

        if ( gui.showTempShelf ) {
            marketButton.setDisable(true);
            gridButton.setDisable(true);
            // 200 , 530
            Image image = new Image("/view/images/resources/temporaryShelfEmpty.png");
            temporaryShelfImage = new ImageView(image);
            temporaryShelfImage.setLayoutX(300);
            temporaryShelfImage.setLayoutY(500);
            temporaryShelfImage.setFitWidth(500);
            temporaryShelfImage.setFitHeight(142);
            temporaryShelfImage.setId("temporaryShelfImage");

            Platform.runLater(() -> dashboardPane.getChildren().add(temporaryShelfImage));


            Image tempShelfImage1 = new Image(getImage(warehouse.get(10)));
            tempShelfImageView1 = new ImageView(tempShelfImage1);
            tempShelfImageView1.setLayoutX(337.5);
            tempShelfImageView1.setLayoutY(530);
            tempShelfImageView1.setFitWidth(85);
            tempShelfImageView1.setFitHeight(85);
            tempShelfImageView1.setPickOnBounds(true);
            tempShelfImageView1.setOnMouseClicked((MouseEvent e) -> handleShelfClick(10));
            tempShelfImageView1.setId("tempShelfImageView1");
            Platform.runLater(() -> dashboardPane.getChildren().add(tempShelfImageView1));

            Image tempShelfImage2 = new Image(getImage(warehouse.get(11)));
            tempShelfImageView2 = new ImageView(tempShelfImage2);
            tempShelfImageView2.setLayoutX(457.5);
            tempShelfImageView2.setLayoutY(530);
            tempShelfImageView2.setFitWidth(85);
            tempShelfImageView2.setFitHeight(85);
            tempShelfImageView2.setPickOnBounds(true);
            tempShelfImageView2.setOnMouseClicked((MouseEvent e) -> handleShelfClick(11));
            tempShelfImageView2.setId("tempShelfImageView2");
            Platform.runLater(() -> dashboardPane.getChildren().add(tempShelfImageView2));

            Image tempShelfImage3 = new Image(getImage(warehouse.get(12)));
            tempShelfImageView3 = new ImageView(tempShelfImage3);
            tempShelfImageView3.setLayoutX(577.5);
            tempShelfImageView3.setLayoutY(530);
            tempShelfImageView3.setFitWidth(85);
            tempShelfImageView3.setFitHeight(85);
            tempShelfImageView3.setPickOnBounds(true);
            tempShelfImageView3.setOnMouseClicked((MouseEvent e) -> handleShelfClick(12));
            tempShelfImageView3.setId("tempShelfImageView3");
            Platform.runLater(() -> dashboardPane.getChildren().add(tempShelfImageView3));


            Image tempShelfImage4 = new Image(getImage(warehouse.get(13)));
            tempShelfImageView4 = new ImageView(tempShelfImage4);
            tempShelfImageView4.setLayoutX(697.5);
            tempShelfImageView4.setLayoutY(530);
            tempShelfImageView4.setFitWidth(85);
            tempShelfImageView4.setFitHeight(85);
            tempShelfImageView4.setPickOnBounds(true);
            tempShelfImageView4.setOnMouseClicked((MouseEvent e) -> handleShelfClick(13));
            tempShelfImageView4.setId("tempShelfImageView4");
            Platform.runLater(() -> dashboardPane.getChildren().add(tempShelfImageView4));



        }
        else {
            marketButton.setDisable(false);
            gridButton.setDisable(false);
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
        if (faithTrackImage != null) {
            Platform.runLater(() -> dashboardPane.getChildren().remove(dashboardPane.lookup("#faithTrackImage")));
        }
        Image faithImage = new Image("/view/images/faithTrack/cross.png");

         for(int i = 0; i<popesFavorTiles.length; i++) {
             int finalI = i+1;
             Platform.runLater(() -> dashboardPane.getChildren().remove(dashboardPane.lookup("#popeFavorTilesImages" + finalI)));
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
        Platform.runLater(() -> dashboardPane.getChildren().add(popeFavorTilesImages[0]));
        Platform.runLater(() -> dashboardPane.getChildren().add(popeFavorTilesImages[1]));
        Platform.runLater(() -> dashboardPane.getChildren().add(popeFavorTilesImages[2]));

        faithTrackImage = new ImageView(faithImage);
        faithTrackImage.setId("faithTrackImage");
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
            blackFaithTrackImage.setId("blackFaithTrackImage");

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


            Platform.runLater(() ->  dashboardPane.getChildren().add(blackFaithTrackImage));
        }

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
        basicProductionResources = new Resource[3];
        marketButton.setDisable(true);
        gridButton.setDisable(true);
        showDashboard();
    }
    public void endTurn(ActionEvent actionEvent) {
        if (gui.showTempShelf) {
            gui.showTempShelf = false;

            Platform.runLater(() -> {
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView1"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView2"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView3"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#tempShelfImageView4"));
                dashboardPane.getChildren().remove(dashboardPane.lookup("#temporaryShelfImage"));
            });

        }
        gui.send(new SetWarehouse(modelView.getMyDashboard().getWarehouse()));
        gui.send(new EndTurn());
        marketButton.setDisable(false);
        gridButton.setDisable(false);
        enabledProductions.clear();
        developmentImageSlot1.setStyle("-fx-opacity: 1");
        developmentImageSlot2.setStyle("-fx-opacity: 1");
        developmentImageSlot3.setStyle("-fx-opacity: 1");
    }

    public void handleTemporaryShelf() {
        showDashboard();
    }

    public void basicProductionResChange(ActionEvent actionEvent) {
        @SuppressWarnings("unchecked")
        ComboBox<ImageView> comboBox = (ComboBox<ImageView>) actionEvent.getSource();
            String[] resources = {"coin2", "servant2", "shield2", "stone2"};
            int selectedIndex = 0;
            switch (comboBox.getId()) {
                case "basicProductionRes1" -> {
                    System.out.println("Risorsa 1: " + comboBox.getSelectionModel().getSelectedIndex());
                    selectedIndex = 0;
                }
                case "basicProductionRes2" -> {
                    System.out.println("Risorsa 2: " + comboBox.getSelectionModel().getSelectedIndex());
                    selectedIndex = 1;
                }
                case "basicProductionRes3" -> {
                    System.out.println("Risorsa 3: " + comboBox.getSelectionModel().getSelectedIndex());
                    selectedIndex = 2;
                }
            }
            switch (comboBox.getSelectionModel().getSelectedIndex()) {
                case 0 -> basicProductionResources[selectedIndex] = Resource.COIN;
                case 1 -> basicProductionResources[selectedIndex] = Resource.SERVANT;
                case 2 -> basicProductionResources[selectedIndex] = Resource.SHIELD;
                case 3 -> basicProductionResources[selectedIndex] = Resource.STONE;
            }

    }
}
