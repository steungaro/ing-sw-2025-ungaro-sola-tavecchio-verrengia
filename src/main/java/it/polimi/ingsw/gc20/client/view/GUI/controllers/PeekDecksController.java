package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Objects;

public class PeekDecksController {

    @FXML
    private ScrollPane DeckPanel;

    public void initializeWithCards(List<ViewAdventureCard> deck) {
        javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5);
        for (ViewAdventureCard card : deck) {
            Image cardImage = getCardImage(card.id);
            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(cardImage);
            imageView.setFitWidth(javafx.stage.Screen.getPrimary().getVisualBounds().getWidth() / 7);
            imageView.setPreserveRatio(true);
            hbox.getChildren().add(imageView);
        }
        DeckPanel.setContent(hbox);
    }

    private Image getCardImage(int id) {
        String imagePath = "/fxml/cards/GT-cards_" + (id > 20 ? "II" : "I") + "_IT_0" + (id > 20 ? id - 20 : id) + ".png";
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }
}
