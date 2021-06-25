package it.polimi.ingsw.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.Map;

/**
 * GameOverController class manages the GameOver scene.
 * @author Andrea Nocito
 */
public class GameOverController {
    //Game over Scene
    public ListView<Integer> pointsListView;
    public ListView<String> playersListView;
    public Label pointsTitle;
    public Label playerTitle;
    @FXML Pane gameOverPane;
    Map<String, Integer> map;


    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    /**
     * Method start shows the result of the game.
     * In a multiplayer game, it shows the final point chart
     */
    public void start() {
            if (map != null && map.values().size() > 1) {
                for (Map.Entry<String, Integer> player : map.entrySet()) {
                    playersListView.getItems().add(player.getKey());
                    pointsListView.getItems().add(player.getValue());
                }
            }
           else {
                Label endGameLabel = new Label();
                endGameLabel.setLayoutX((gameOverPane.getWidth() - 200) / 2);
                endGameLabel.setLayoutY((gameOverPane.getHeight() - 30) / 2);
                endGameLabel.setPrefWidth(200);
                endGameLabel.setPrefHeight(30);
                playersListView.setOpacity(0);
                pointsListView.setOpacity(0);
                playerTitle.setOpacity(0);
                pointsTitle.setOpacity(0);
               if(map != null) {
                   int points = (int) map.values().toArray()[0];
                   endGameLabel.setText("You won! Total points: " + points);
               }
               else {
                   endGameLabel.setText("You lost!");
               }
               Platform.runLater(() -> gameOverPane.getChildren().add(endGameLabel));
           }

    }
}
