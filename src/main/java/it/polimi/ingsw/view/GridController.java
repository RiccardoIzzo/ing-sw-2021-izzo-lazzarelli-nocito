package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.BuyCard;
import it.polimi.ingsw.model.JsonCardsCreator;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.DiscountLeaderCard;
import it.polimi.ingsw.model.card.ResourceRequirement;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * GridController class manages the Grid scene.
 * @author Andrea Nocito
 */
public class GridController {
    public ImageView card1ImageView;
    public Button card1Button;
    public ImageView card2ImageView;
    public Button card2Button;
    public ImageView card3ImageView;
    public Button card3Button;
    public ImageView card4ImageView;
    public Button card4Button;
    public ImageView card5ImageView;
    public Button card5Button;
    public ImageView card6ImageView;
    public Button card6Button;
    public ImageView card7ImageView;
    public Button card7Button;
    public ImageView card8ImageView;
    public Button card8Button;
    public ImageView card9ImageView;
    public Button card9Button;
    public ImageView card10ImageView;
    public Button card10Button;
    public ImageView card11ImageView;
    public Button card11Button;
    public ImageView card12ImageView;
    public Button card12Button;

    public TextField slotTextField;
    public Pane gridPane;

    private ImageView[] imageViews;
    private Integer[] ids;
    private Integer selectedId = 0;
    private ArrayList<Button> buttons;
    private GUI gui;

    public ImageView cardSelectedImageView;
    public Button buyCardButton;
    private ModelView modelView;
    /**
     * Method cardButtonClicked is called when a card is selected and updates cardSelectedImageView
     */
    public void cardButtonClicked(ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();
        int pos = buttons.indexOf(clickedButton);

        if(imageViews[pos].getImage().getUrl().contains("Empty")) {
            selectedId = 0;
        }
        else {
            cardSelectedImageView.setImage(imageViews[pos].getImage());
            selectedId = ids[pos];
        }
    }

    /**
     * Method buyCardButtonClicked checks if a card has been selected.
     * If it hasn't, it shows an alert, otherwise checks if the requirement
     * to buy the card are met and sends a buyCard message
     */
    public void buyCardButtonClicked() {
        if(selectedId != 0) {
            DevelopmentCard cardToBuy = JsonCardsCreator.generateDevelopmentCard(selectedId);
            ArrayList<Integer> activeDevelopments = modelView.getMyDashboard().getActiveDevelopments();
            ResourceMap discount = modelView.getMyDashboard().getLeaderCards()
                    .entrySet()
                    .stream()
                    .filter(Map.Entry::getValue)
                    .map(Map.Entry::getKey)
                    .filter(leader -> JsonCardsCreator.generateLeaderCard(leader) instanceof DiscountLeaderCard)
                    .map(leader -> ((DiscountLeaderCard) JsonCardsCreator.generateLeaderCard(leader)).getDiscount())
                    .reduce(new ResourceMap(), ResourceMap::addResources);

            List<DevelopmentCard> cardsToCover = activeDevelopments.stream()
                    .filter(Objects::nonNull)
                    .map(JsonCardsCreator::generateDevelopmentCard)
                    .filter(card -> card.getLevel() == cardToBuy.getLevel() - 1).collect(Collectors.toList());

            boolean conditionsMet = false;
            int slotIndex = 0;
            if (!modelView.getMyDashboard().getTotalResources().addResources(discount).removeResources(((ResourceRequirement) cardToBuy.getRequirement()).getResources())) {
                gui.showAlert("Cannot buy card. Not enough resources.", Alert.AlertType.ERROR);
            } else if (cardToBuy.getLevel() > 1 && cardsToCover.size() == 0) {
                gui.showAlert("Cannot buy card. You don't own a level " + (cardToBuy.getLevel() - 1) + " card.", Alert.AlertType.ERROR);
            } else if (cardToBuy.getLevel() == 1 && activeDevelopments.stream().filter(Objects::nonNull).count() > 2){
                gui.showAlert("Cannot buy card. Not enough space for a level 1 card.", Alert.AlertType.ERROR);
            } else {
                if (slotTextField.getText().length() > 0 ) {
                    slotIndex = Integer.parseInt(slotTextField.getText());

                    Integer cardToCover = modelView.getMyDashboard().getActiveDevelopments().get(slotIndex - 1);
                    if (cardToBuy.getLevel() > 1 && cardToCover != null) {
                        if (JsonCardsCreator.generateDevelopmentCard(cardToCover).getLevel() + 1 == cardToBuy.getLevel()) {
                            conditionsMet = true;
                        }
                        else {
                            gui.showAlert("Cannot place your card at slot number " + slotIndex + "." + "Try again:", Alert.AlertType.ERROR);
                        }
                    } else if (cardToBuy.getLevel() == 1 && cardToCover == null) {
                        conditionsMet = true;
                    } else {
                        gui.showAlert("Cannot place your card at slot number " + slotIndex + "." + "Try again:", Alert.AlertType.ERROR);
                    }
                }
                else {
                    gui.showAlert("Select the slot number and try again:", Alert.AlertType.ERROR);

                }
            }
            if (conditionsMet) {
                int index = modelView.getGrid().indexOf(selectedId);
                gui.send(new BuyCard(index / 4, index % 4, slotIndex));
                gui.basicActionPlayed();
                gui.updateDashboard();
                Stage stage = (Stage) buyCardButton.getScene().getWindow();
                stage.close();
            }
        }
    }

    /**
     * Method setup sets up the scene and calls setGrid
     */
    public void setup(ModelView modelView) {
        slotTextField = new TextField();
        slotTextField.setPromptText("Slot num.");
        slotTextField.setLayoutX(90);
        slotTextField.setLayoutY(300);
        slotTextField.setPrefWidth(77);
        slotTextField.setPrefHeight(20);

        slotTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                slotTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (slotTextField.getText().length() > 1) {
                slotTextField.setText(String.valueOf(slotTextField.getText().charAt(0)));
            }
            if(slotTextField.getText().length() == 1 && ( Integer.parseInt(slotTextField.getText()) > 3 || Integer.parseInt(slotTextField.getText()) < 1)  ) {
                slotTextField.setText(String.valueOf(1));
            }
        });
        gridPane.getChildren().add(slotTextField);
        this.modelView = modelView;
        ids = new Integer[12];
        imageViews = new ImageView[12];
        buttons = new ArrayList<>();

        imageViews[0] = card1ImageView;
        imageViews[1] = card2ImageView;
        imageViews[2] = card3ImageView;
        imageViews[3] = card4ImageView;
        imageViews[4] = card5ImageView;
        imageViews[5] = card6ImageView;
        imageViews[6] = card7ImageView;
        imageViews[7] = card8ImageView;
        imageViews[8] = card9ImageView;
        imageViews[9] = card10ImageView;
        imageViews[10] = card11ImageView;
        imageViews[11] = card12ImageView;

        buttons.add(card1Button);
        buttons.add(card2Button);
        buttons.add(card3Button);
        buttons.add(card4Button);
        buttons.add(card5Button);
        buttons.add(card6Button);
        buttons.add(card7Button);
        buttons.add(card8Button);
        buttons.add(card9Button);
        buttons.add(card10Button);
        buttons.add(card11Button);
        buttons.add(card12Button);

        setGrid(modelView.getGrid());
        buyCardButton.setDisable(!((modelView.getCurrPlayer().equals(gui.getNickname()) || modelView.getCurrPlayer().length() < 1) && gui.getValidActions().contains(Action.BUY_CARD)));
    }

    /**
     * Method setGUI sets up the GUI.
     * @param gui GUI reference.
     */
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * Method setGrid sets the correct image for each card in the grid
     * @param grid an arrayList of ids of cards that are visible
     */
    public void setGrid(ArrayList<Integer> grid) {
        Image[] devImages = new Image[12];
        int slot = 0;
        for (Integer id: grid) {
            if (id != null) {
                devImages[slot] = new Image("/view/images/developments/developmentCard"+id+".png");
            } else {
                devImages[slot] = new Image("/view/images/developments/developmentCardEmpty.png");
            }
            ids[slot] = id;
            imageViews[slot].setImage(devImages[slot]);
            slot++;
        }
    }
}
