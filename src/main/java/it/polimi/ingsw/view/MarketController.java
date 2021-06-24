package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.TakeResources;
import it.polimi.ingsw.model.JsonCardsCreator;
import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.card.WhiteMarbleLeaderCard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;


/**
 * MarketController class manages the Market scene.
 * @author Andrea Nocito
 */
public class MarketController {
    // Market Scene
    public Button firstColumnButton;
    public Button secondColumnButton;
    public Button thirdColumnButton;
    public Button fourthColumnButton;
    public Button firstRowButton;
    public Button secondRowButton;
    public Button thirdRowButton;
    public Pane marketPane;
    public ChoiceBox<String> whiteMarbleChoiceBox;
    private GUI gui;
    private ArrayList<MarbleColor> marketTray;
    ArrayList<Integer> activeWhiteMarbleLeaders;
    int leaderID = 0;

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * Method checkActiveWhiteMarbleLeaders checks if there are any white marble leaders active and sets up
     * whiteMarbleChoiceBox before adding it to the scene (if not empty).
     * @param leaderCards a map representing the leader cards.
     */
    public void checkActiveWhiteMarbleLeaders(Map<Integer, Boolean> leaderCards) {
        whiteMarbleChoiceBox = new ChoiceBox<>();

        whiteMarbleChoiceBox.setLayoutX(marketPane.getWidth()/2-75);
        whiteMarbleChoiceBox.setLayoutY(0);
        whiteMarbleChoiceBox.setPrefWidth(150);
        whiteMarbleChoiceBox.setMinHeight(30);
        whiteMarbleChoiceBox.setOnAction(event -> {
            int index = whiteMarbleChoiceBox.getSelectionModel().getSelectedIndex();
            if(index >= 0) {
                leaderID = activeWhiteMarbleLeaders.get(index);
            }
        });

        activeWhiteMarbleLeaders = (ArrayList<Integer>) leaderCards
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .filter(leader -> JsonCardsCreator.generateLeaderCard(leader) instanceof WhiteMarbleLeaderCard)
                .collect(Collectors.toList());

        if (activeWhiteMarbleLeaders.size() >= 1){
            WhiteMarbleLeaderCard card1 = (WhiteMarbleLeaderCard) Objects.requireNonNull(JsonCardsCreator.generateCard(activeWhiteMarbleLeaders.get(0)));
            whiteMarbleChoiceBox.getItems().add("White marble = " + ((MarbleColor) card1.getExchange().toArray()[0]).name());
            whiteMarbleChoiceBox.setValue("White marble = " + card1.getExchange().toString());


            if (activeWhiteMarbleLeaders.size() == 2) {
                System.out.print("&& == 2");
                WhiteMarbleLeaderCard card2 = (WhiteMarbleLeaderCard) Objects.requireNonNull(JsonCardsCreator.generateCard(activeWhiteMarbleLeaders.get(1)));
                whiteMarbleChoiceBox.getItems().add("White marble = " + ((MarbleColor) card2.getExchange().toArray()[0]).name());
            }
            Platform.runLater(() -> marketPane.getChildren().add(whiteMarbleChoiceBox));

            leaderID = activeWhiteMarbleLeaders.get(0);
            whiteMarbleChoiceBox.getSelectionModel().selectFirst();

        }


    }

    /**
     * Method start sets up marketPane
     */
    public void start() {
        Pane marblePane = new Pane();
        double len = 50;
        double paneWidth = len*4;
        double paneHeight = len*3;
        double xStart = 25, yStart = 30;
        double yPadding = 5;


        marblePane.setMinHeight(paneHeight);
        marblePane.setMinWidth(paneWidth);
        marblePane.setLayoutX(xStart);
        marblePane.setLayoutY(yStart);

        for (int i = 2; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                MarbleColor color = marketTray.get(i*4+j);

                Image image = new Image("/view/images/marbleColors/"+color.name()+".png");
                ImageView marbleImage = new ImageView(image);


                marbleImage.setLayoutX(xStart+len*j);
                marbleImage.setLayoutY(yStart+(2-i)*(len+yPadding));
                marbleImage.setFitWidth(len);
                marbleImage.setFitHeight(len);
                marblePane.getChildren().add(marbleImage);

            }
        }
        Platform.runLater(() -> marketPane.getChildren().add(marblePane));
        marblePane.toFront();
    }

    /**
     * Method columnButtonClicked is called when an arrow representing a column of the market tray is clicked
     * It sends TakeResource message
     */
    public void columnButtonClicked(ActionEvent actionEvent) {
        Button arrowButton = (Button) actionEvent.getSource();

        if (whiteMarbleChoiceBox.isShowing()) {
            whiteMarbleChoiceBox.getSelectionModel().getSelectedIndex();
        }
        int index = switch (arrowButton.getId()) {
            case "firstColumnButton" -> 1;
            case "secondColumnButton" -> 2;
            case "thirdColumnButton" -> 3;
            case "fourthColumnButton" -> 4;
            default -> 0;
        };

        if (leaderID != 0) {
            gui.send(new TakeResources(index, 2, leaderID));
        }
        else {
            gui.send(new TakeResources(index, 2));

        }
        gui.dashboardController.showTempShelf = true;
        gui.basicActionPlayed();
        Stage stage = (Stage) arrowButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Method rowButtonClicked is called when an arrow representing a row of the market tray is clicked
     * It sends TakeResource message
     */
    public void rowButtonClicked(ActionEvent actionEvent) {
        Button arrowButton = (Button) actionEvent.getSource();
        if (whiteMarbleChoiceBox.isShowing()) {
            whiteMarbleChoiceBox.getSelectionModel().getSelectedIndex();
        }
        int index = switch (arrowButton.getId()) {
            case "firstRowButton" -> 1;
            case "secondRowButton" -> 2;
            case "thirdRowButton" -> 3;
            default -> 0;
        };
        if (index > 0) {
            if (leaderID != 0) {
                gui.send(new TakeResources(index, 1, leaderID));
            }
            else {
                gui.send(new TakeResources(index, 1));
            }
        }
        gui.dashboardController.showTempShelf = true;
        gui.basicActionPlayed();
        Stage stage = (Stage) arrowButton.getScene().getWindow();
        stage.close();
    }

    public void setMarketTray(ArrayList<MarbleColor> marketTray) {
        this.marketTray = marketTray;
    }
}
