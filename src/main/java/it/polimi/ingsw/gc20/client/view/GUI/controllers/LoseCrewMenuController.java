package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class LoseCrewMenuController {
    @FXML
    private Label crewToLoseLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Pane shipPane;

    @FXML
    private TextField cabinsField;

    private int crewToLose;
    private String username;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
    }

    public void initializeWithCrewToLose(int crewToLose) {
        this.crewToLose = crewToLose;
        crewToLoseLabel.setText("You need to lose " + crewToLose + " crew members!");

        loadShipView();
    }

    private void loadShipView() {
        ViewShip ship = ClientGameModel.getInstance().getShip(username);
    }

    @FXML
    private void handleContinue() {
        String input = cabinsField.getText().trim();
        List<Pair<Integer, Integer>> cabins = parseCoordinates(input);

        if (cabins == null) {
            return; // Error message already shown by parseCoordinates
        }

        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().loseCrew(username, cabins);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleViewOptions() {
        ClientGameModel.getInstance().setBusy();
        // TODO Call the options menu
    }

    private List<Pair<Integer, Integer>> parseCoordinates(String input) {
        if (input == null || input.isEmpty()) {
            showError("Please enter cabin coordinates");
            return null;
        }

        List<Pair<Integer, Integer>> coordinates = new ArrayList<>();
        String[] parts = input.split("\\s+");

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