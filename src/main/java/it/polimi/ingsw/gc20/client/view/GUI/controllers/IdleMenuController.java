package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class IdleMenuController {
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
}