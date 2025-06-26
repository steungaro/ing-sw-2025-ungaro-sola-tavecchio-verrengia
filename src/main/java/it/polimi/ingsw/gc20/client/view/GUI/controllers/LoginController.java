package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIApplication;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {

        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty");
            return;
        }

        ClientGameModel.getInstance().setUsername(username);
        ClientGameModel.getInstance().login();
    }

    public void setErrorLabel(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        return;
    }
}