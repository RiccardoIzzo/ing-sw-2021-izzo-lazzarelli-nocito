package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.events.clientmessages.CreateLobby;
import it.polimi.ingsw.events.clientmessages.GetLobbies;
import it.polimi.ingsw.events.clientmessages.JoinLobby;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class LobbiesController {
    public ListView<String> lobbiesListView;
    public VBox lobbiesNumberVBox;
    // Lobbies Scene
    @FXML TextField lobbyTextField;
    @FXML TextField numPlayersTextField;

    private static GUI gui;
    Map<String, Integer> lobbies;

    public void setLobbies(Map<String,Integer> lobbies) {
        this.lobbies = lobbies;
    }


    public void setGUI(GUI gui) {
        LobbiesController.gui = gui;
    }

    public void start() throws IOException {
//        gui.mainStage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scenes/sceneLobbies"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        LobbiesController controller = loader.getController();

            for(Map.Entry<String,Integer> lobby : lobbies.entrySet()) {
                controller.lobbiesListView.getItems().add("["+lobby.getValue()+" players] - " + lobby.getKey());
            }
        controller.lobbiesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }
    public void joinButtonClicked() {
        String selectedItem = lobbiesListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            gui.showAlert("Select the lobby you want to join", Alert.AlertType.ERROR);
        }
        else {
            String lobbyID = selectedItem.substring(selectedItem.indexOf(" - ")+3);
            gui.send(new JoinLobby(lobbyID));
        }
    }

    public void createButtonClicked() {
        String lobbyID = lobbyTextField.getText();
        if (lobbyID == null || lobbyID.length() < 1) {
            gui.showAlert("Insert a lobby ID!", Alert.AlertType.ERROR);
            return;
        }
        if (lobbiesListView.getItems().contains(lobbyID) ) {
            gui.showAlert("Already exists a lobby with this id! Try again.", Alert.AlertType.ERROR);
            gui.send(new GetLobbies());
        }
        else {

            if (isParsable(numPlayersTextField.getText())) {
                int numPlayers = Integer.parseInt(numPlayersTextField.getText());
                if (numPlayers > 4 )
                    gui.showAlert("The maximum number of players is four, choose again.", Alert.AlertType.ERROR);
                else {
                    gui.send(new CreateLobby(lobbyID, numPlayers));
                }
            }
            else {
                gui.showAlert("Input not valid! Choose a number between one and four!", Alert.AlertType.ERROR);
            }
        }

    }

    public static boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
