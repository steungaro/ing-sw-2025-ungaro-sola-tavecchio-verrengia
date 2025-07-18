package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Map;

public class AutomaticActionController implements MenuController.ContextDataReceiver, BindCleanUp {

    @FXML
    public Pane shipPane;
    @FXML
    public Label errorLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label messageLabel;

    private String username;
    private ViewShip ship;
    private ShipController shipController;


    /**
     * Initializes the JavaFX components and sets up the initial state of the controller.
     * This method is automatically called by JavaFX after loading the FXML file.
     * It retrieves the current username and ship from the game model and loads the ship view.
     */
    @FXML
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
        ship = ClientGameModel.getInstance().getShip(username);
        loadShipView();
    }

    private void loadShipView() {
        try {
            if (ship == null) {
                showError("Error, ship not found: " + username);
                return;
            }

            String fxmlPath = ship.isLearner ? "/fxml/ship0.fxml" : "/fxml/ship2.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent shipView = loader.load();

            shipPane.getChildren().clear();
            shipPane.getChildren().add(shipView);

            shipPane.applyCss();
            shipPane.layout();

            Pane shipPaneTyped = (Pane) shipView;

            shipPaneTyped.setMinWidth(150);
            shipPaneTyped.setMinHeight(120);

            shipPaneTyped.setMaxWidth(Region.USE_COMPUTED_SIZE);
            shipPaneTyped.setMaxHeight(Region.USE_COMPUTED_SIZE);

            shipPaneTyped.prefWidthProperty().bind(shipPane.widthProperty());
            shipPaneTyped.prefHeightProperty().bind(shipPane.heightProperty());

            try{
                StackPane stackPane = (StackPane)shipPaneTyped;
                stackPane.setAlignment(javafx.geometry.Pos.CENTER);

            } catch (ClassCastException e) {
                showError("Error casting shipPaneTyped to StackPane: " + e.getMessage());
            }

            Platform.runLater(() -> {
                shipPane.requestLayout();
                shipPaneTyped.requestLayout();
            });

            Object controller = loader.getController();
        } catch (IOException e) {
            showError("Error while loading the ship view: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Sets the message to be displayed in the message label.
     * If the message label is not null, updates its text content with the provided message.
     *
     * @param message the message text to display
     */
    public void setMessage(String message) {
        if (messageLabel != null) {
            messageLabel.setText(message);
        }
    }

    /**
     * Initializes the controller with a specific message.
     * This method sets the message and performs any necessary initialization.
     *
     * @param message the message to initialize the controller with
     */
    public void initializeWithMessage(String message) {
        setMessage(message);
    }

    /**
     * Sets the context data for this controller from the menu system.
     * Extracts the message from the context data if present and initializes the controller with it.
     *
     * @param contextData a map containing context data, expected to contain a "message" key with a String value
     */
    @Override
    public void setContextData(Map<String, Object> contextData) {
        if(contextData.containsKey("message")) {
            String message = (String) contextData.get("message");
            initializeWithMessage(message);
        }
    }

    /**
     * Performs cleanup operations for this controller to prevent memory leaks.
     * This method:
     * - Cleans up the ship controller if it implements BindCleanUp
     * - Removes the ship controller from game model listeners if it implements GameModelListener
     * - Unbinds properties from all child nodes in the ship pane
     * - Clears the ship pane children
     * - Nullifies object references
     * - Resets all labels to empty state
     */
    public void cleanup() {

        if (shipController != null) {
            try{
                (shipController).cleanup();
            } catch (ClassCastException e){
                System.err.println("AutomaticActionController: ShipController does not implement BindCleanUp, skipping cleanup");
            } catch (Exception e) {
                System.err.println("Error during ShipController cleanup: " + e.getMessage());
            }

            try{
                ClientGameModel gameModel = ClientGameModel.getInstance();
                if (gameModel != null) {
                    gameModel.removeListener( shipController);
                }
            } catch (ClassCastException e) {
                System.err.println("AutomaticActionController: ShipController does not implement GameModelListener, skipping removal");
            } catch (Exception e) {
                System.err.println("Error removing ShipController from GameModel listeners: " + e.getMessage());
            }

            shipController = null;
        }

        if (shipPane != null && !shipPane.getChildren().isEmpty()) {
            for (javafx.scene.Node child : shipPane.getChildren()) {
                unbindNodeProperties(child);
            }

            shipPane.getChildren().clear();
        }

        ship = null;
        username = null;

        if (titleLabel != null) {
            titleLabel.setText("");
        }
        if (messageLabel != null) {
            messageLabel.setText("");
        }
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }

    }

    private void unbindNodeProperties(javafx.scene.Node node) {
        if (node == null) return;

        try {
            try{
                Region region = (Region) node;
                region.prefWidthProperty().unbind();
                region.prefHeightProperty().unbind();
                region.minWidthProperty().unbind();
                region.minHeightProperty().unbind();
                region.maxWidthProperty().unbind();
                region.maxHeightProperty().unbind();
            } catch (ClassCastException e) {
                // If the node is not a Region, we skip this part
            }

            try{
                Pane pane = (Pane) node;
                pane.prefWidthProperty().unbind();
                pane.prefHeightProperty().unbind();

                for (Node child : pane.getChildren()) {
                    unbindNodeProperties(child);
                }
            } catch (ClassCastException e) {
                // If the node is not a Pane, we skip this part
            }

            node.setOnMouseClicked(null);
            node.setOnMouseEntered(null);
            node.setOnMouseExited(null);

        } catch (Exception e) {
            System.err.println("Error unbinding node properties: " + e.getMessage());
        }
    }

}