package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.CreateLobby;
import it.polimi.ingsw.events.clientmessages.GetLobbies;
import it.polimi.ingsw.events.clientmessages.JoinLobby;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Map;


/**
 * LobbiesController class manages the Lobbies scene.
 * @author Andrea Nocito
 */
public class LobbiesController {
    public ListView<String> lobbiesListView;

    // Lobbies Scene
    @FXML TextField lobbyTextField;
    @FXML TextField numPlayersTextField;
    @FXML Pane lobbiesPane;
    private static GUI gui;
    Map<String, Integer> lobbies;

    public void setLobbies(Map<String,Integer> lobbies) {
        this.lobbies = lobbies;
    }


    public void setGUI(GUI gui) {
        LobbiesController.gui = gui;
    }


    /**
     * Method setLobbies receives the updated map of the available lobbies
     * @param newLobbies map that associates the lobby id to the maximum number of players for that lobby.
     */
    public void refreshLobbies(Map<String, Integer> newLobbies){
        setLobbies(newLobbies);
        lobbiesListView.getItems().clear();
        for(Map.Entry<String,Integer> lobby : newLobbies.entrySet()) {
            lobbiesListView.getItems().add("["+lobby.getValue()+" players] - " + lobby.getKey());
        }
        lobbiesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    /**
     * Method enable is called by gui to reset the lobbies scene after
     * a connection or creation didn't go through
     */
    public void enable() {
        lobbiesPane.setDisable(false);
    }

    /**
     * Method joinButtonClicked checks if a lobby has been selected.
     * If it hasn't, it shows an alert, otherwise it sends to server a request to join the selected lobby
     */
    public void joinButtonClicked() {
        String selectedItem = lobbiesListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            gui.showAlert("Select the lobby you want to join", Alert.AlertType.ERROR);
        }
        else {
            String lobbyID = selectedItem.substring(selectedItem.indexOf(" - ")+3);
            gui.send(new JoinLobby(lobbyID));
            lobbiesPane.setDisable(true);
        }
    }

    /**
     * Method joinButtonClicked checks if the lobby name and the number of players have been written.
     * If they haven't, or the name is not availabble, it shows an alert, otherwise it sends to server
     * a request to create the selected lobby
     */
    public void createButtonClicked() {
        String lobbyID = lobbyTextField.getText();
        if (lobbyID == null || lobbyID.length() < 1) {
            gui.showAlert("Insert a lobby ID!", Alert.AlertType.ERROR);
            return;
        }
        if (lobbiesListView.getItems().contains(lobbyID) ) {
            gui.showAlert("It already exists a lobby with this id! Try again.", Alert.AlertType.ERROR);
            gui.send(new GetLobbies());
        }
        else {

            if (isParsable(numPlayersTextField.getText())) {
                int numPlayers = Integer.parseInt(numPlayersTextField.getText());
                if (numPlayers < 1 || numPlayers > 4)
                    gui.showAlert("The number of players must be between 1 and 4, choose again.", Alert.AlertType.ERROR);
                else {
                    gui.send(new CreateLobby(lobbyID, numPlayers));
                    lobbiesPane.setDisable(true);
                    addWaitingView("Lobby created! Waiting for other players ...");
                }
            }
            else {
                gui.showAlert("Input not valid! Choose a number between one and four!", Alert.AlertType.ERROR);
            }
        }

    }


    /**
     * Method addWaitingView is called after a successful request to join or create a lobby has been made.
     * It asks the user to wait that the conditions to start the game are met.
     * @param text the waiting message that will be shown to the user
     */
    public void addWaitingView(String text) {
        Pane waitingPane = new Pane();
        Label textLabel = new Label(text);
        double paneWidth = 230;
        double paneHeight = 100;
        waitingPane.setMinHeight(paneHeight);
        waitingPane.setMinWidth(paneWidth);
        waitingPane.setLayoutX(lobbiesPane.getWidth()/2 - (paneWidth / 2));
        waitingPane.setLayoutY(lobbiesPane.getHeight()/2 - (paneHeight / 2));
        waitingPane.setStyle("-fx-background-color: white ; -fx-border-radius: 20");
        textLabel.setMinHeight(60);
        textLabel.setLayoutX(10);
        textLabel.setMinWidth(paneWidth - 20);
        textLabel.setLayoutY(paneHeight/2-30);
        waitingPane.getChildren().add(textLabel);
        Platform.runLater(() -> lobbiesPane.getChildren().add(waitingPane));
    }
    public static boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method refreshButtonClicked sends a request to get an updated list of the available lobbies
    */
     public void refreshButtonClicked() {
            gui.send(new GetLobbies());
        }
    }
