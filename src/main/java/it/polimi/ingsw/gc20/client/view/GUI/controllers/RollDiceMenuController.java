package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.rmi.RemoteException;
import java.util.Map;

public class RollDiceMenuController implements MenuController.ContextDataReceiver {

    @FXML
    private Label messageLabel;

    @FXML
    private Label errorLabel;

    private String username;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
    }

    public void initializeWithMessage(String message) {
        messageLabel.setText(message);
    }

    @FXML
    private void handleRollDice() {
        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().rollDice(username);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @Override
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("message")) {
            String message = (String) contextData.get("message");
            initializeWithMessage(message);
        } else {
            throw new IllegalArgumentException("Context data must contain a message");
        }
    }
}