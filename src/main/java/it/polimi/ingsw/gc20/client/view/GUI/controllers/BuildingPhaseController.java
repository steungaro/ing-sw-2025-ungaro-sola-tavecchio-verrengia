package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BuildingPhaseController implements Initializable {

    @FXML
    private BorderPane shipContainer;

    @FXML
    private Pane componentInHandPane;

    @FXML
    private Pane coveredDeckPane;

    @FXML
    private FlowPane uncoveredComponentsPane;

    @FXML
    private HBox nonLearnerButtonsContainer;

    @FXML
    private HBox nonLearnerComponentInHandButtons;

    private ViewComponent selectedComponent;
    private ShipController shipController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadShip();
        loadCoveredDeck();
        loadUncoveredComponents();

        String username = ClientGameModel.getInstance().getUsername();
        boolean isLearner = ClientGameModel.getInstance().getShip(username).isLearner;
        nonLearnerButtonsContainer.setVisible(!isLearner);
        nonLearnerButtonsContainer.setManaged(!isLearner);
    }

    /**
     * Loads the ship (ship0.fxml or ship2.fxml) based on the player's ship type
     */
    private void loadShip() {
        try {
            String username = ClientGameModel.getInstance().getUsername();
            ViewShip playerShip = ClientGameModel.getInstance().getShip(username);

            String shipFileName;
            if (playerShip != null && playerShip.getClass().getSimpleName().equals("ViewShip0")) {
                shipFileName = "ship0";
            } else {
                shipFileName = "ship2";
            }

            String path = "/fxml/" + shipFileName + ".fxml";
            URL resourceUrl = getClass().getResource(path);

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent shipRoot = loader.load();

            shipController = loader.getController();
            shipController.setShipState(ShipController.ShipState.Building);

            shipContainer.setCenter(shipRoot);

        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading ship: " + e.getMessage());
        }
    }

    /**
     * Loads the covered deck (shows only the back of a card)
     */
    private void loadCoveredDeck() {
        Rectangle coveredCard = new Rectangle(0, 0, 120, 80);
        coveredCard.setFill(Color.DARKGRAY);
        coveredCard.setStroke(Color.BLACK);
        coveredCard.setArcWidth(10);
        coveredCard.setArcHeight(10);
        coveredDeckPane.getChildren().add(coveredCard);
    }

    /**
     * Loads the uncovered components from the model
     */
    private void loadUncoveredComponents() {
        uncoveredComponentsPane.getChildren().clear();

        if (ClientGameModel.getInstance().getBoard() != null &&
                ClientGameModel.getInstance().getBoard().viewedPile != null) {

            List<ViewComponent> uncoveredComponents = ClientGameModel.getInstance().getBoard().viewedPile;

            for (ViewComponent component : uncoveredComponents) {
                Pane componentPane = createComponentPane(component);
                componentPane.setOnMouseClicked(event -> selectComponent(component, componentPane));
                uncoveredComponentsPane.getChildren().add(componentPane);
            }
        }
    }

    /**
     * Creates a pane that visually represents a component
     */
    private Pane createComponentPane(ViewComponent component) {
        Pane pane = new Pane();
        pane.setPrefSize(80, 80);

        return pane;
    }

    /**
     * Rotates the selected component
     */
    @FXML
    private void rotateComponent() {
        if (selectedComponent != null) {
            componentInHandPane.getChildren().clear();
            componentInHandPane.getChildren().add(createComponentPane(selectedComponent));
        } else {
            showError("No component selected!");
        }
    }

    /**
     * Places the selected component on the ship
     */
    @FXML
    private void placeComponent() {
        if (selectedComponent == null) {
            showError("No component selected!");
            return;
        }

        if (shipController != null) {
            selectedComponent = null;
            componentInHandPane.getChildren().clear();

            loadUncoveredComponents();
        }
    }

    /**
     * Discards the selected component
     */
    @FXML
    private void discardComponent() {
        if (selectedComponent == null) {
            showError("No component selected!");
            return;
        }
        selectedComponent = null;
        componentInHandPane.getChildren().clear();

        loadUncoveredComponents();
    }

    /**
     * Shows an error message in a dialog window
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Stops ship assembly and finishes the building phase
     */
    @FXML
    private void stopAssembling() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Stop Assembling Ship");
        dialog.setHeaderText("Choose your starting board position");
        dialog.setContentText("Enter board index (0-4):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(boardIndex -> {
            try {
                int index = Integer.parseInt(boardIndex);
                if (index >= 0 && index <= 4) {
                    ClientGameModel.getInstance().getClient().stopAssembling(
                            ClientGameModel.getInstance().getUsername(),
                            index
                    );
                } else {
                    showError("Invalid board index. Please enter a number between 0 and 4.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        });
    }

    /**
     * Takes a component from the booked components
     */
    @FXML
    private void takeBookedComponent() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Take Booked Component");
        dialog.setHeaderText("Select a booked component to take");
        dialog.setContentText("Enter component index (0 or 1):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(indexStr -> {
            try {
                int index = Integer.parseInt(indexStr);
                if (index == 0 || index == 1) {
                    String username = ClientGameModel.getInstance().getUsername();
                    ClientGameModel.getInstance().getClient().takeComponentFromBooked(username, index);
                } else {
                    showError("Invalid index. Please enter 0 or 1.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        });
    }

    /**
     * Turns the hourglass
     */
    @FXML
    private void turnHourglass() {
        try {
            String username = ClientGameModel.getInstance().getUsername();
            ClientGameModel.getInstance().getClient().turnHourglass(username);
        } catch (Exception e) {
            showError("Error turning hourglass: " + e.getMessage());
        }
    }

    /**
     * Peeks at a deck of cards
     */
    @FXML
    private void peekDeck() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Peek Deck");
        dialog.setHeaderText("Select a deck to peek");
        dialog.setContentText("Enter deck index (0-2):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(indexStr -> {
            try {
                int index = Integer.parseInt(indexStr);
                if (index >= 0 && index <= 2) {
                    String username = ClientGameModel.getInstance().getUsername();
                    ClientGameModel.getInstance().getClient().peekDeck(username, index);
                } else {
                    showError("Invalid index. Please enter a number between 0 and 2.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        });
    }

    /**
     * Books the component currently in hand
     */
    @FXML
    private void bookComponent() {
        if (selectedComponent == null) {
            showError("No component selected!");
            return;
        }

        try {
            String username = ClientGameModel.getInstance().getUsername();
            ClientGameModel.getInstance().getClient().addComponentToBooked(username);
            selectedComponent = null;
            componentInHandPane.getChildren().clear();
        } catch (Exception e) {
            showError("Error booking component: " + e.getMessage());
        }
    }

    /**
     * Seleziona un componente e lo mostra nel pannello "componente in mano"
     */
    private void selectComponent(ViewComponent component, Pane sourcePane) {
        selectedComponent = component;

        componentInHandPane.getChildren().clear();
        componentInHandPane.getChildren().add(createComponentPane(component));

        uncoveredComponentsPane.getChildren().forEach(node -> {
            node.setStyle(node.getStyle().replace("-fx-border-width: 3;", ""));
        });
        sourcePane.setStyle(sourcePane.getStyle() + "-fx-border-width: 3;");

        // Mostra i pulsanti specifici per quando c'è un componente in mano per navi non-learner
        String username = ClientGameModel.getInstance().getUsername();
        boolean isLearner = ClientGameModel.getInstance().getShip(username).isLearner;
        nonLearnerComponentInHandButtons.setVisible(!isLearner);
        nonLearnerComponentInHandButtons.setManaged(!isLearner);
    }
}