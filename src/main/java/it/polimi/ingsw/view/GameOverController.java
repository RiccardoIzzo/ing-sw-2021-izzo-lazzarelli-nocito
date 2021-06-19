package it.polimi.ingsw.view;

import javafx.scene.control.ListView;

import java.util.Map;

public class GameOverController {
    //Game over Scene
    public ListView<Integer> pointsListView;
    public ListView<String> playersListView;
    Map<String, Integer> map;


    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
    public void start() {
        for(Map.Entry<String, Integer> player : map.entrySet()) {
            playersListView.getItems().add(player.getKey());
            pointsListView.getItems().add(player.getValue());
        }
    }
}
