package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.events.clientmessages.SelectLeaderCards;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Set;

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

    public void selectButtonClicked(ActionEvent actionEvent) {
        if(selectedLeaders.size() != 2) {
            gui.showAlert("Select two leaders cards", Alert.AlertType.ERROR);
        }
        else {
            gui.send(new SelectLeaderCards(selectedLeaders.get(0), selectedLeaders.get(1)));
            gui.handleGameStart();
        }
    }

    public void cardButtonClicked(ActionEvent actionEvent) {

        if (actionEvent.getSource().equals(card1Button) ) {
            if (selectedLeaders.contains(leaderIds.get(0)) ) {
                card1Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
                selectedLeaders.remove(leaderIds.get(0));
            }
            else{
                selectedLeaders.add(leaderIds.get(0));
                card1Button.setStyle("-fx-background-color: white ; -fx-border-width: 2px ;");
            }
        }
        else if (actionEvent.getSource().equals(card2Button) ) {
            if (selectedLeaders.contains(leaderIds.get(1)) ) {
                card2Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
                selectedLeaders.remove(leaderIds.get(1));
            }
            else{
                selectedLeaders.add(leaderIds.get(1));
                card2Button.setStyle("-fx-background-color: white ; -fx-border-width: 2px ;");
            }
        }
        else if (actionEvent.getSource().equals(card3Button) ) {

            if (selectedLeaders.contains(leaderIds.get(2)) ) {
                card3Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
                selectedLeaders.remove(leaderIds.get(2));
            }
            else{
                selectedLeaders.add(leaderIds.get(2));
                card3Button.setStyle("-fx-background-color: white ; -fx-border-width: 2px ;");
            }
        }
        else if (actionEvent.getSource().equals(card4Button) ) {

            if (selectedLeaders.contains(leaderIds.get(3)) ) {
                card4Button.setStyle("-fx-background-color: transparent ; -fx-border-width: 0px ;");
                selectedLeaders.remove(leaderIds.get(3));
            }
            else{
                selectedLeaders.add(leaderIds.get(3));
                card4Button.setStyle("-fx-background-color: white ; -fx-border-width: 2px ;");
            }
        }
    }
}
