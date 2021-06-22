package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.SelectLeaderCards;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * SelectLeaderController class manages the SelectLeaders scene.
 * @author Andrea Nocito
 */
public class SelectLeaderController {
    @FXML private Button card1Button;
    @FXML private ImageView card1ImageView;
    @FXML private Button card2Button;
    @FXML private ImageView card2ImageView;
    @FXML private Button card3Button;
    @FXML private ImageView card3ImageView;
    @FXML private Button card4Button;
    @FXML private ImageView card4ImageView;
    @FXML private Button selectButton;

    ArrayList<Integer> leaderIds;
    static ArrayList<Integer> selectedLeaders;


    private static GUI gui;

    public void setGUI(GUI gui) {
        SelectLeaderController.gui = gui;
    }



    public void start() throws IOException {
        showCards();
    }


    /**
     * Method showCards loads the images of the leader cards received
     */
    public void showCards() {
        card1ImageView.setImage(new Image("/view/images/leaderCards/leaderCard"+ leaderIds.get(0) +".png"));
        card2ImageView.setImage(new Image("/view/images/leaderCards/leaderCard"+ leaderIds.get(1) +".png"));
        card3ImageView.setImage(new Image("/view/images/leaderCards/leaderCard"+ leaderIds.get(2) +".png"));
        card4ImageView.setImage(new Image("/view/images/leaderCards/leaderCard"+ leaderIds.get(3) +".png"));


        card1Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
        card2Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
        card3Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
        card4Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
    }

    public void setLeadersIds(ArrayList<Integer> ids) {
        leaderIds = ids;
        selectedLeaders = new ArrayList<>();
    }

    /**
     * Method selectButtonClicked checks that two leadercards have been selected.
     * It they haven't, it shows an alert, otherwise it sends a message with the cards to remove.
     */
    public void selectButtonClicked() {
        if(selectedLeaders.size() != 2) {
            gui.showAlert("Select two leaders cards", Alert.AlertType.ERROR);
        }
        else {
            leaderIds.remove(selectedLeaders.get(0));
            leaderIds.remove(selectedLeaders.get(1));
            gui.send(new SelectLeaderCards(leaderIds.get(0), leaderIds.get(1)));
            Platform.runLater(() -> gui.startGame());
        }
    }

    /**
     * Method cardButtonClicked manages the selection of the leadercards.
     * When a card is selected it will show a colored background,
     * when it's unselected it will show a transparent background
     */
    public void cardButtonClicked(ActionEvent actionEvent) {
        String selectedColor = "DarkSeaGreen";
        if (actionEvent.getSource().equals(card1Button) ) {
            if (selectedLeaders.contains(leaderIds.get(0)) ) {
                card1Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
                selectedLeaders.remove(leaderIds.get(0));
            }
            else{
                selectedLeaders.add(leaderIds.get(0));
                card1Button.setStyle("-fx-background-color: "+selectedColor+" ; -fx-border-width: 2px ;");
            }
        }
        else if (actionEvent.getSource().equals(card2Button) ) {
            if (selectedLeaders.contains(leaderIds.get(1)) ) {
                card2Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
                selectedLeaders.remove(leaderIds.get(1));
            }
            else{
                selectedLeaders.add(leaderIds.get(1));
                card2Button.setStyle("-fx-background-color: "+selectedColor+" ; -fx-border-width: 2px ;");
            }
        }
        else if (actionEvent.getSource().equals(card3Button) ) {

            if (selectedLeaders.contains(leaderIds.get(2)) ) {
                card3Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
                selectedLeaders.remove(leaderIds.get(2));
            }
            else{
                selectedLeaders.add(leaderIds.get(2));
                card3Button.setStyle("-fx-background-color: "+selectedColor+" ; -fx-border-width: 2px ;");
            }
        }
        else if (actionEvent.getSource().equals(card4Button) ) {

            if (selectedLeaders.contains(leaderIds.get(3)) ) {
                card4Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
                selectedLeaders.remove(leaderIds.get(3));
            }
            else{
                selectedLeaders.add(leaderIds.get(3));
                card4Button.setStyle("-fx-background-color: "+selectedColor+" ; -fx-border-width: 2px ;");
            }
        }
    }
}
