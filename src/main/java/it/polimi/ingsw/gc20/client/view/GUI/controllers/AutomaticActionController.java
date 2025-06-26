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

            try{
                ShipController shipController = (ShipController) controller;
            } catch (ClassCastException e) {
                showError("Unable to get the ship controller");
            }
        } catch (IOException e) {
            showError("Error while loading the ship view: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void setMessage(String message) {
        if (messageLabel != null) {
            messageLabel.setText(message);
        }
    }

    public void initializeWithMessage(String message) {
        setMessage(message);
    }

    @Override
    public void setContextData(Map<String, Object> contextData) {
        if(contextData.containsKey("message")) {
            String message = (String) contextData.get("message");
            initializeWithMessage(message);
        }
    }

    @Override
    public void cleanup() {
        System.out.println("AutomaticActionController: Starting cleanup...");

        if (shipController != null) {
            try{
                (shipController).cleanup();
                System.out.println("AutomaticActionController: ShipController cleaned up");
            } catch (ClassCastException e){
                System.err.println("AutomaticActionController: ShipController does not implement BindCleanUp, skipping cleanup");
            } catch (Exception e) {
                System.err.println("Error during ShipController cleanup: " + e.getMessage());
            }

            try{
                ClientGameModel gameModel = ClientGameModel.getInstance();
                if (gameModel != null) {
                    gameModel.removeListener( shipController);
                    System.out.println("AutomaticActionController: ShipController removed from GameModel listeners");
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
            System.out.println("AutomaticActionController: ShipPane cleared and unbound");
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

        System.out.println("AutomaticActionController: Cleanup completed");
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