package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.Map;

public class AutomaticActionController implements MenuController.ContextDataReceiver{

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

            shipPaneTyped.setMaxWidth(Region.USE_COMPUTED_SIZE);
            shipPaneTyped.setMaxHeight(Region.USE_COMPUTED_SIZE);

            shipPaneTyped.prefWidthProperty().bind(shipPane.widthProperty());
            shipPaneTyped.prefHeightProperty().bind(shipPane.heightProperty());


            Object controller = loader.getController();
            try {
                ShipController shipController = (ShipController) controller;
            } catch (ClassCastException e) {
                showError("Unable to get the ship controller");
            }

        } catch (IOException e) {
            showError("Error uploading ship: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void setTitle(String title) {
        if (titleLabel != null) {
            titleLabel.setText(title);
        }
    }

    public void setMessage(String message) {
        if (messageLabel != null) {
            messageLabel.setText(message);
        }
    }

    public void initializeWithMessage(String title, String message) {
        setTitle(title);
        setMessage(message);
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
}