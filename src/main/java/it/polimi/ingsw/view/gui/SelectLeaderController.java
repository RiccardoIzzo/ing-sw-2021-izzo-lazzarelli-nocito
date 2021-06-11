package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
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

    static Set<Integer> leaderIds;

    private static GUI gui;

    public void setGUI(GUI gui) {
        SelectLeaderController.gui = gui;
    }



    public void start() throws IOException {
        showCards();
    }
    public void showCards() {

        int[] ids = leaderIds.stream().mapToInt(Number::intValue).toArray();
        card1ImageView.setImage(new Image("/view/images/leaderCards/leaderCard"+ids[0]+".png"));
        card2ImageView.setImage(new Image("/view/images/leaderCards/leaderCard"+ids[1]+".png"));
        card3ImageView.setImage(new Image("/view/images/leaderCards/leaderCard"+ids[2]+".png"));
        card4ImageView.setImage(new Image("/view/images/leaderCards/leaderCard"+ids[3]+".png"));
    }
    public void setLeadersIds(Set<Integer> ids) {
        leaderIds = ids;
    }

    public void selectButtonClicked(ActionEvent actionEvent) {
    }

    public void cardButtonClicked(ActionEvent actionEvent) {
    }
}
