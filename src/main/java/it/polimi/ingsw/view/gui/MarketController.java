package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MarketController {
    // Market Scene
    public Button firstColumnButton;
    public Button secondColumnButton;
    public Button thirdColumnButton;
    public Button fourthColumnButton;
    public Button firstRowButton;
    public Button secondRowButton;
    public Button thirdRowButton;

    public void columnButtonClicked(ActionEvent actionEvent) {
        Button arrowButton = (Button) actionEvent.getSource();
        System.out.println(arrowButton.getId());

    }

    public void rowButtonClicked(ActionEvent actionEvent) {
        Button arrowButton = (Button) actionEvent.getSource();
        System.out.println(arrowButton.getId());
    }
}
