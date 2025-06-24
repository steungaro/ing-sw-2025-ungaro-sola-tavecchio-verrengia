package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;

public class ShieldsMenuController {

    @FXML
    private Label messageLabel;

    @FXML
    private Pane shipPane;

    @FXML
    private Label errorLabel;

    private String username;
    private ViewShip ship;
    int shieldRow;
    int shieldCol;
    private ShipController shipController;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();

        ship = ClientGameModel.getInstance().getShip(username);

        loadShipView();
    }

    public void initializeWithMessage(String message) {
        messageLabel.setText(message);
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

            ((Pane) shipView).prefWidthProperty().bind(shipPane.widthProperty());
            ((Pane) shipView).prefHeightProperty().bind(shipPane.heightProperty());

            Object controller = loader.getController();

            try{
                shipController = (ShipController) controller;
                shipController.enableCellClickHandler(this::selectShieldToActivate);

            } catch (ClassCastException e) {
                showError("Unable to get the ship controller");
                return;
            }

        } catch (IOException e) {
            showError("Error uploading ship: " + e.getMessage());
        }
    }

    @FXML
    private void selectShieldToActivate(int row, int col) {
        shieldRow = row;
        shieldCol = col;
        shipController.enableCellClickHandler(this::selectBatteryToActivate);
    }

    @FXML
    private void selectBatteryToActivate(int row, int col) {
        try {
            Pair<Integer, Integer> shieldCoordinates = new Pair<>(shieldRow, shieldCol);
            Pair<Integer, Integer> batteryCoordinates = new Pair<>(row, col);

            ClientGameModel.getInstance().getClient().activateShield(username, shieldCoordinates, batteryCoordinates);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleSkipActivation() {
        try {
            ClientGameModel.getInstance().getClient().activateShield(username, null, null);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}