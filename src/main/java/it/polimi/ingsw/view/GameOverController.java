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
    @FXML Pane gameOverPane;
    Map<String, Integer> map;
    Integer points;


    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    /**
     * Method start shows the result of the game.
     * In a multiplayer game, it shows the final point chart
     */
    public void start() {
        if(map != null) {
            for (Map.Entry<String, Integer> player : map.entrySet()) {
                playersListView.getItems().add(player.getKey());
                pointsListView.getItems().add(player.getValue());
            }
        }
        else {
            playersListView.setOpacity(0);
            pointsListView.setOpacity(0);
            Label endGameLabel = new Label();
            if(points != null) {
                endGameLabel.setText("You won! Total points: " + points);
            }
            else {
                endGameLabel.setText("You lost!");
            }
            endGameLabel.setLayoutX((gameOverPane.getWidth()-100)/2);
            endGameLabel.setLayoutY((gameOverPane.getHeight()-30)/2);
            endGameLabel.setPrefWidth(100);
            endGameLabel.setPrefHeight(30);

            Platform.runLater(() -> gameOverPane.getChildren().add(endGameLabel));
        }
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
