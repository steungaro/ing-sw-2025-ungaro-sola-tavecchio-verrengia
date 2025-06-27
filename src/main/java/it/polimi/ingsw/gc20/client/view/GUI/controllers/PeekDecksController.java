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

/**
 * Controller class for the deck peeking interface.
 * Displays adventure cards in a scrollable flow layout with interactive card containers.
 * Implements ContextDataReceiver to receive card data and BindCleanUp for resource management.
 */
public class PeekDecksController implements MenuController.ContextDataReceiver, BindCleanUp {

    @FXML
    private ScrollPane DeckPanel;

    /**
     * Initializes the controller with a list of adventure cards to display.
     * Sets up a responsive flow layout with card containers that dynamically adjust size
     * based on the number of cards to display.
     *
     * @param deck The list of adventure cards to display
     */
    public void initializeWithCards(List<ViewAdventureCard> deck) {
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setHgap(15);
        flowPane.setVgap(15);
        flowPane.setPadding(new Insets(10));
        flowPane.setStyle("-fx-background-color: transparent;");
        
        double cardWidth = calculateCardWidth(deck.size());
        double cardHeight = calculateCardHeight(deck.size());
        
        for (ViewAdventureCard card : deck) {
            VBox cardContainer = createCardContainer(card, cardWidth, cardHeight);
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
    
    private double calculateCardHeight(int numberOfCards) {
        if (numberOfCards <= 3) {
            return 250;
        } else if (numberOfCards <= 6) {
            return 200;
        } else if (numberOfCards <= 10) {
            return 170;
        } else {
            return 140;
        }
    }
    
    private VBox createCardContainer(ViewAdventureCard card, double cardWidth, double cardHeight) {
        VBox cardContainer = new VBox();
        cardContainer.setAlignment(Pos.CENTER);
        cardContainer.setSpacing(5);
        cardContainer.setPadding(new Insets(8));
        cardContainer.setStyle(
            "-fx-background-color: #333344; " +
            "-fx-background-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 6, 0, 0, 2);"
        );
        
        cardContainer.setOnMouseEntered(_ -> cardContainer.setStyle(
            "-fx-background-color: #3c4a5e; " +
            "-fx-background-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 10, 0, 0, 3); " +
            "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
        ));
        
        cardContainer.setOnMouseExited(_ -> cardContainer.setStyle(
            "-fx-background-color: #333344; " +
            "-fx-background-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 6, 0, 0, 2); " +
            "-fx-scale-x: 1.0; -fx-scale-y: 1.0;"
        ));
        
        Image cardImage = getCardImage(card.id);
        ImageView imageView = new ImageView(cardImage);
        imageView.setFitWidth(cardWidth);
        imageView.setFitHeight(cardHeight);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        
        cardContainer.getChildren().add(imageView);
        
        return cardContainer;
    }

    private Image getCardImage(int id) {
        String imagePath = "/fxml/cards/GT-cards_" + (id > 20 ? "II" : "I") + "_IT_0" + (id > 20 ? id - 20 : id) + ".jpg";
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    /**
     * Sets contextual data received from a parent controller.
     * Extracts a list of adventure cards from the context data and initializes the controller with it.
     * 
     * @param contextData A map containing contextual data, must include a "cards" key with a List of ViewAdventureCard objects
     * @throws IllegalArgumentException if the required cards data is not present or is of incorrect type
     */
    @SuppressWarnings("unchecked")
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

    /**
     * Cleans up resources used by this controller.
     * Removes event handlers, clears images, and nullifies references to avoid memory leaks.
     * Should be called when the view is no longer needed.
     */
    public void cleanup() {

        if (DeckPanel != null) {
            if (DeckPanel.getContent() != null) {
                try {
                    FlowPane flowPane = (FlowPane) DeckPanel.getContent();

                    for (javafx.scene.Node node : flowPane.getChildren()) {
                        try {
                            VBox cardContainer = (VBox) node;
                            cardContainer.setOnMouseEntered(null);
                            cardContainer.setOnMouseExited(null);

                            for (javafx.scene.Node child : cardContainer.getChildren()) {
                                try {
                                    ImageView imageView = (ImageView) child;
                                    imageView.setImage(null);
                                    imageView.setFitWidth(0);
                                    imageView.setFitHeight(0);
                                } catch (Exception e) {
                                    System.err.println("Error cleaning up image view: " + e.getMessage());
                                }
                            }

                            cardContainer.getChildren().clear();
                        } catch (Exception e) {
                            System.err.println("Error cleaning up card container: " + e.getMessage());
                        }
                    }

                    flowPane.getChildren().clear();
                } catch (Exception e) {
                    System.err.println("Error cleaning up flow pane: " + e.getMessage());
                }
            }

            DeckPanel.setContent(null);
            DeckPanel = null;
        }
    }
}