package it.polimi.ingsw.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

/**
 * GameOverController class manages the GameOver scene.
 * @author Andrea Nocito
 */
public class GameOverController {
    //Game over Scene
    public ImageView scoreboard;
    public ImageView boardbw;
    @FXML Pane gameOverPane;
    Map<String, Integer> map;
    private GUI gui;

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }


    /**
     * Method start shows the result of the game.
     * In a multiplayer game, it shows the final point chart
     */
    public void start() {
        Light.Distant light = new Light.Distant();
        light.setAzimuth(50);
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setInput(lighting);
        dropShadow.setOffsetY(3.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setColor(Color.BLACK);

            if (map != null && map.values().size() > 1) {
                int k = 0;


                InnerShadow is = new InnerShadow();
                is.setOffsetX(0.5);
                is.setOffsetY(0.5);
                is.setColor(Color.web("black"));
                LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
                map.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

                for (Map.Entry<String, Integer> entry : reverseSortedMap.entrySet()) {

                    String player = entry.getKey();
                    String playerPoints = String.valueOf(entry.getValue());
                    Label playerLabel = new Label();
                    playerLabel.setText(playerPoints + "pt - " + player );
                    playerLabel.setFont(new Font("System Black", 25));
                    playerLabel.setLayoutX(230);
                    playerLabel.setLayoutY(70+k*42);
                    playerLabel.setPrefWidth(150);
                    playerLabel.setPrefHeight(40);
                    if(k == 0) {
                        playerLabel.setTextFill(player.equals(gui.getNickname()) ? Color.web("Lime") : Color.web("Red"));
                    }
                    else {
                        playerLabel.setTextFill(Color.web("DeepSkyBlue"));
                    }

                    DropShadow ds = new DropShadow();
                    ds.setInput(is);
                    ds.setOffsetY(1.0);
                    ds.setOffsetX(1.0);
                    ds.setColor(Color.WHITE);
                    playerLabel.setEffect(ds);
                    Platform.runLater(() -> gameOverPane.getChildren().add(playerLabel));
                    k++;
                }
                boardbw.setOpacity(0);
                scoreboard.setOpacity(1);
            }
           else {
                boardbw.setOpacity(1);
                scoreboard.setOpacity(0);
                Label endGameLabel = new Label();
                endGameLabel.setLayoutX((gameOverPane.getWidth() - 350) / 2);
                endGameLabel.setLayoutY((gameOverPane.getHeight() - 150) / 2);
                endGameLabel.setPrefWidth(350);
                endGameLabel.setPrefHeight(150);
                endGameLabel.setAlignment(Pos.CENTER);

               if(map != null) {
                   int points = (int) map.values().toArray()[0];
                   endGameLabel.setFont(new Font("System Black", 50));
                   endGameLabel.setTextFill(Color.web("Lime"));
                   endGameLabel.setStyle("-fx-text-alignment: center");
                   endGameLabel.setText("You won!\n Total points: " + points);
               }
               else {
                   endGameLabel.setFont(new Font("System Black", 50));
                   endGameLabel.setTextFill(Color.web("red"));
                   endGameLabel.setText("GAME OVER");


               }
                endGameLabel.setEffect(dropShadow);
                Platform.runLater(() -> gameOverPane.getChildren().add(endGameLabel));
           }

    }

    public void exitGame() {
        System.exit(0);
    }
}
