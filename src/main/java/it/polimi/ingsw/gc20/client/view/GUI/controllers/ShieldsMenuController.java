package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
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
    private TextField shieldRowField;

    @FXML
    private TextField shieldColField;

    @FXML
    private TextField batteryRowField;

    @FXML
    private TextField batteryColField;

    @FXML
    private Label errorLabel;

    private String username;
    private boolean isLearnerShip;
    private ViewShip ship;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();

        ship = ClientGameModel.getInstance().getShip(username);
        isLearnerShip = ship != null && ship.isLearner;

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

        } catch (IOException e) {
            showError("Error uploading ship: " + e.getMessage());
        }
    }

    @FXML
    private void handleActivateShield() {
        try {
            int shieldRow = Integer.parseInt(shieldRowField.getText().trim()) - 5;
            int shieldCol = Integer.parseInt(shieldColField.getText().trim()) - (isLearnerShip ? 5 : 4);

            if (shieldRow < 0 || shieldRow > 4 || shieldCol < 0 || shieldCol > 6) {
                showError("Invalid shield coordinates. Row must be between 5-9 and column between " +
                        (isLearnerShip ? "5-11" : "4-10"));
                return;
            }

            int batteryRow = Integer.parseInt(batteryRowField.getText().trim()) - 5;
            int batteryCol = Integer.parseInt(batteryColField.getText().trim()) - 4; // Battery columns are always offset by 4

            if (batteryRow < 0 || batteryRow > 4 || batteryCol < 0 || batteryCol > 6) {
                showError("Invalid battery coordinates. Row must be between 5-9 and column between 4-10");
                return;
            }

            Pair<Integer, Integer> shieldCoordinates = new Pair<>(shieldRow, shieldCol);
            Pair<Integer, Integer> batteryCoordinates = new Pair<>(batteryRow, batteryCol);

            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().activateShield(username, shieldCoordinates, batteryCoordinates);
            ClientGameModel.getInstance().setFree();
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for coordinates");
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleSkipActivation() {
        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().activateShield(username, null, null);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleViewOptions() {
        // TODO
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}