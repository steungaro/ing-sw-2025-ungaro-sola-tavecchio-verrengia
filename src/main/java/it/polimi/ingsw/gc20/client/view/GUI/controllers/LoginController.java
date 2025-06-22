package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController implements GUIController {

    @FXML
    public Label errorLabel;
    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(_ -> handleLogin());
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            errorLabel.setText("Username cannot be empty.");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }

        ClientGameModel.getInstance().setUsername(username);
        ClientGameModel.getInstance().login();
    }

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}