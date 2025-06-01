package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class EngineMenuController {
    @FXML private Label messageLabel;
    @FXML private Label errorLabel;
    @FXML private Pane shipPane;
    @FXML private TextField enginesField;
    @FXML private TextField batteriesField;

    private String username;

    @FXML
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
    }

    public void initializeWithMessage(String message) {
        messageLabel.setText(message);

        loadShipView();
    }

    private void loadShipView() {
        ViewShip ship = ClientGameModel.getInstance().getShip(username);
    }

    @FXML
    private void handleActivateEngines() {
        try {
            List<Pair<Integer, Integer>> engines = parseCoordinates(enginesField.getText());
            List<Pair<Integer, Integer>> batteries = parseCoordinates(batteriesField.getText());

            if (engines == null || batteries == null) {
                return; // Error message already shown by parseCoordinates
            }

            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().activateEngines(username, engines, batteries);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
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