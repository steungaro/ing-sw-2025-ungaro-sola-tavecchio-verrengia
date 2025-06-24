package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Map;

public class IdleMenuController implements MenuController.ContextDataReceiver {
    @FXML
    private Label messageLabel;

    public void initialize() {
    }

    public void initializeWithMessage(String message) {
        messageLabel.setText(message);
    }

    @FXML
    private void handleViewOptions() {
        // TODO
    }

    @FXML
    private void handleQuitGame() {
        // TODO
    }

    @Override
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("message")) {
            String message = (String) contextData.get("message");
            initializeWithMessage(message);
        } else {
            throw new IllegalArgumentException("Context data must contain 'message'");
        }
    }

}