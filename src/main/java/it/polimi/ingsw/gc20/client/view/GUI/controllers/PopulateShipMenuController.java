package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.javatuples.Pair;

import java.rmi.RemoteException;

public class PopulateShipMenuController {
    @FXML
    private Pane shipPane;

    @FXML
    private VBox alienOptionsBox;

    @FXML
    private TextField rowField;

    @FXML
    private TextField colField;

    @FXML
    private ComboBox<AlienColor> colorComboBox;

    @FXML
    private Label errorLabel;

    private String username;
    private boolean isLearnerShip;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();

        colorComboBox.setItems(FXCollections.observableArrayList(AlienColor.BROWN, AlienColor.PURPLE));
        colorComboBox.getSelectionModel().selectFirst();

        loadShipData();
    }

    private void loadShipData() {
        ViewShip ship = ClientGameModel.getInstance().getShip(username);
        isLearnerShip = ship != null && ship.isLearner;

        // Hide alien options for learner ships
        if (isLearnerShip) {
            alienOptionsBox.setVisible(false);
            alienOptionsBox.setManaged(false);
        }
    }

    @FXML
    private void handleAddAlien() {
        if (isLearnerShip) {
            showError("Cannot add aliens in learner mode");
            return;
        }

        try {
            int row = Integer.parseInt(rowField.getText().trim()) - 5;
            int col = Integer.parseInt(colField.getText().trim()) - (isLearnerShip ? 5 : 4);

            if (row < 0 || row > 4 || col < 0 || col > 6) {
                showError("Invalid coordinates. Row must be between 5-9 and column between " +
                        (isLearnerShip ? "5-11" : "4-10"));
                return;
            }

            AlienColor color = colorComboBox.getValue();
            if (color == null) {
                showError("Please select an alien color");
                return;
            }

            Pair<Integer, Integer> coordinates = new Pair<>(row, col);
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().addAlien(username, color, coordinates);
            ClientGameModel.getInstance().setFree();

            rowField.clear();
            colField.clear();

        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for coordinates");
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleFinishPopulation() {
        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().endMove(username);
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