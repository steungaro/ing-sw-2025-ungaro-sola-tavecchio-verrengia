package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    /**
     * Initializes the login controller and sets up the user interface components.
     * This method is called automatically by JavaFX after loading the FXML file.
     * It configures the login button to handle the login action when clicked,
     * setting up the event handler for user authentication.
     */
    @FXML
    public void initialize() {

        loginButton.setOnAction(_ -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            setErrorLabel("Username cannot be empty");
            return;
        }

        ClientGameModel.getInstance().setUsername(username);
        ClientGameModel.getInstance().login();
    }

    /**
     * Sets and displays an error message to the user.
     * This method updates the error label with the specified error message
     * and makes it visible to provide feedback when login operations fail
     * or when validation errors occur.
     * 
     * @param errorMessage the error message to display to the user
     */
    public void setErrorLabel(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
    }
}