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
import java.util.Map;

public class LoseEnergyMenuController implements MenuController.ContextDataReceiver {
    @FXML
    private Label energyToLoseLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Pane shipPane;

    @FXML
    private TextField rowField;

    @FXML
    private TextField colField;

    private int energyToLose;
    private String username;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
    }

    public void initializeWithEnergyToLose(int energyToLose) {
        this.energyToLose = energyToLose;
        energyToLoseLabel.setText("You have to lose " + energyToLose +
                " energy because you are short on cargo!");

        loadShipView();
    }

    private void loadShipView() {
        ViewShip ship = ClientGameModel.getInstance().getShip(username);
    }

    @FXML
    private void handleContinue() {
        try {
            int row = Integer.parseInt(rowField.getText().trim()) - 5;
            boolean isLearner = ClientGameModel.getInstance().getShip(username).isLearner;
            int col = Integer.parseInt(colField.getText().trim()) - (isLearner ? 5 : 4);

            if (row < 0 || row > 4 || col < 0 || col > 6) {
                showError("Invalid coordinates. Row must be between 5-9 and column between " +
                        (isLearner ? "5-11" : "4-10"));
                return;
            }

            Pair<Integer, Integer> batteryCoordinates = new Pair<>(row, col);

            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().loseEnergy(username, batteryCoordinates);
            ClientGameModel.getInstance().setFree();
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for coordinates");
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

    @Override
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("batteryNum")) {
            int energyToLose = (int) contextData.get("batteryNum");
            initializeWithEnergyToLose(energyToLose);
        } else {
            showError("No energy to lose data provided.");
        }
    }
}