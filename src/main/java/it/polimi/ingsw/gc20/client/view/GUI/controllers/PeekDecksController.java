package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PeekDecksController implements MenuController.ContextDataReceiver {

    @FXML
    private ScrollPane DeckPanel;

    public void initializeWithCards(List<ViewAdventureCard> deck) {
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setHgap(15);
        flowPane.setVgap(15);
        flowPane.setPadding(new Insets(10));
        flowPane.setStyle("-fx-background-color: transparent;");
        
        double cardWidth = calculateCardWidth(deck.size());
        
        for (ViewAdventureCard card : deck) {
            VBox cardContainer = createCardContainer(card, cardWidth);
            flowPane.getChildren().add(cardContainer);
        }
        DeckPanel.setContent(flowPane);
    }
    
    private double calculateCardWidth(int numberOfCards) {
        if (numberOfCards <= 3) {
            return 180;
        } else if (numberOfCards <= 6) {
            return 140;
        } else if (numberOfCards <= 10) {
            return 120;
        } else {
            return 100;
        }
    }
    
    private VBox createCardContainer(ViewAdventureCard card, double cardWidth) {
        VBox cardContainer = new VBox();
        cardContainer.setAlignment(Pos.CENTER);
        cardContainer.setSpacing(5);
        cardContainer.setPadding(new Insets(8));
        cardContainer.setStyle(
            "-fx-background-color: #333344; " +
            "-fx-background-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 6, 0, 0, 2);"
        );
        
        cardContainer.setOnMouseEntered(e -> {
            cardContainer.setStyle(
                "-fx-background-color: #3c4a5e; " +
                "-fx-background-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 10, 0, 0, 3); " +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        cardContainer.setOnMouseExited(e -> {
            cardContainer.setStyle(
                "-fx-background-color: #333344; " +
                "-fx-background-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 6, 0, 0, 2); " +
                "-fx-scale-x: 1.0; -fx-scale-y: 1.0;"
            );
        });
        
        Image cardImage = getCardImage(card.id);
        ImageView imageView = new ImageView(cardImage);
        imageView.setFitWidth(cardWidth);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        
        cardContainer.getChildren().add(imageView);
        
        return cardContainer;
    }

    private Image getCardImage(int id) {
        String imagePath = "/fxml/cards/GT-cards_" + (id > 20 ? "II" : "I") + "_IT_0" + (id > 20 ? id - 20 : id) + ".jpg";
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    @SuppressWarnings( "unchecked")
    @Override
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("cards")) {
            List<ViewAdventureCard> deck;
            try{
                deck = (List<ViewAdventureCard>) contextData.get("cards");
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Context data 'cards' must be a List<ViewAdventureCard>");
            }
            initializeWithCards(deck);
        } else {
            throw new IllegalArgumentException("Context data must contain 'decks'");
        }
    }
}