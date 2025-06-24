package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Map;

public class AutomaticActionController implements MenuController.ContextDataReceiver{

    @FXML
    private Label titleLabel;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
    }

    public void setTitle(String title) {
        if (titleLabel != null) {
            titleLabel.setText(title);
        }
    }

    public void setMessage(String message) {
        if (messageLabel != null) {
            messageLabel.setText(message);
        }
    }

    public void initializeWithMessage(String title, String message) {
        setTitle(title);
        setMessage(message);
    }

    public void initializeWithMessage(String message) {
        setMessage(message);
    }

    @Override
    public void setContextData(Map<String, Object> contextData) {
        if(contextData.containsKey("message")) {
            String message = (String) contextData.get("message");
            initializeWithMessage(message);
        }
    }
}