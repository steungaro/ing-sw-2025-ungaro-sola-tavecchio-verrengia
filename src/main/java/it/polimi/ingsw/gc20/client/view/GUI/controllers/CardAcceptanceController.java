package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class CardAcceptanceController {

    @FXML
    private Label messageLabel;

    @FXML
    private Button acceptButton;

    @FXML
    private Button rejectButton;

    @FXML
    private ImageView cardImageView;

    @FXML
    private VBox mainContainer;

    private String message;

    public void initialize() {
        mainContainer.setUserData(this);
        ViewAdventureCard viewAdventureCard = ClientGameModel.getInstance().getCurrentCard();
        if (viewAdventureCard != null) {
            cardImageView.setImage(getImage(viewAdventureCard));
            cardImageView.setVisible(true);
        } else {
            cardImageView.setVisible(false);
        }
    }

    public void updateMessage(String message) {
        this.message = message;
        if (messageLabel != null) {
            messageLabel.setText(message);
        }
    }

    private javafx.scene.image.Image getImage(ViewAdventureCard viewAdventureCard) {
        String series = (viewAdventureCard.id > 20) ? "II" : "I";
        int adjustedId = (viewAdventureCard.id > 20) ? (viewAdventureCard.id - 20) : viewAdventureCard.id;
        String imagePath = "/fxml/cards/GT-cards_" + series + "_IT_0" + adjustedId + ".jpg";
        return new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    @FXML
    private void handleAccept() {
        if (ClientGameModel.getInstance().getClient() != null) {
            try{
                ClientGameModel.getInstance().getClient().acceptCard(
                        ClientGameModel.getInstance().getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleReject() {
        if (ClientGameModel.getInstance().getClient() != null) {
            try {
                ClientGameModel.getInstance().getClient().endMove(
                        ClientGameModel.getInstance().getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleBackToMenu() {
        ClientGameModel.getInstance().mainMenuState();
    }
}