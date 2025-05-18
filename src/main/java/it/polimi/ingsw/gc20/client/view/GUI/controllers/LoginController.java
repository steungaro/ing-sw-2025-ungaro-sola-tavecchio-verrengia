package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    private GUIView guiView;

    @FXML
    public void initialize() {
        guiView = (GUIView) View.getInstance();

        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty");
            return;
        }

        // Passa al MainMenu
        guiView.showMainMenuScene(username);
    }
}