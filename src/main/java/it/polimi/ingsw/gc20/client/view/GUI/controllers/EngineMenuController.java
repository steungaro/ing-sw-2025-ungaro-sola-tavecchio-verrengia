package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class EngineMenuController {
    @FXML private Label messageLabel;
    @FXML private Label errorLabel;
    @FXML private Pane shipPane;

    private ViewShip ship;
    private String username;
    private int engineRow;
    private int engineCol;

    @FXML
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
            if (controller instanceof ShipController shipController) {
                shipController.enableCellClickHandler(this::selectEngineToActivate);
                shipController.enableCellClickHandler(this::selectBatteryToActivate);
            } else {
                showError("Unable to get the ship controller");
            }

        } catch (IOException e) {
            showError("Error uploading ship: " + e.getMessage());
        }
    }

    private void selectEngineToActivate(int row, int col) {
        engineRow = row - 5;
        engineCol = col - (ship.isLearner ? 5 : 4);
    }

    @FXML
    private void selectBatteryToActivate(int row, int col) {
        int battetyRow = row - 5;
        int batteryCol = col - (ship.isLearner ? 5 : 4);
        /*
        try {
            List<Pair<Integer, Integer>> engines = parseCoordinates(enginesField.getText());
            List<Pair<Integer, Integer>> batteries = parseCoordinates(batteriesField.getText());

            if (engines == null || batteries == null) {
                return; // Error message already shown by parseCoordinates
            }

            ClientGameModel.getInstance().getClient().activateEngines(username, engines, batteries);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }*/
    }

    @FXML
    private void handleSkipActivation() {
        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().activateEngines(username, null, null);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    private List<Pair<Integer, Integer>> parseCoordinates(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new ArrayList<>(); // Return empty list for no coordinates
        }

        List<Pair<Integer, Integer>> coordinates = new ArrayList<>();
        String[] parts = input.trim().split("\\s+");

        if (parts.length % 2 != 0) {
            showError("Invalid coordinates format. Please enter pairs of numbers.");
            return null;
        }

        ViewShip ship = ClientGameModel.getInstance().getShip(username);
        boolean isLearner = ship != null && ship.isLearner;
        int offset = isLearner ? 5 : 4;

        try {
            for (int i = 0; i < parts.length; i += 2) {
                int row = Integer.parseInt(parts[i]) - 5;
                int col = Integer.parseInt(parts[i + 1]) - offset;

                if (row < 0 || row > 4 || col < 0 || col > 6) {
                    showError("Coordinates out of bounds. Row must be between 5-9 and column between " +
                            (offset) + "-" + (offset+6));
                    return null;
                }

                coordinates.add(new Pair<>(row, col));
            }
        } catch (NumberFormatException e) {
            showError("Invalid coordinates. Please enter valid numbers.");
            return null;
        }

        return coordinates;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}