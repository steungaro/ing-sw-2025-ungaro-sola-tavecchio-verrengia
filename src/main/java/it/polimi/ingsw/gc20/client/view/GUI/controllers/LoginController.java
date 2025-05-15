package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField serverField;

    @FXML
    private TextField portField;

    @FXML
    private Button loginButton;

    @FXML
    private Label statusLabel;

    private GUIView guiView;

    @FXML
    public void initialize() {
        guiView = (GUIView) View.getInstance();

        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String server = serverField.getText().trim();
        String portText = portField.getText().trim();

        if (username.isEmpty() || server.isEmpty() || portText.isEmpty()) {
            statusLabel.setText("Tutti i campi sono obbligatori!");
            return;
        }

        try {
            int port = Integer.parseInt(portText);
            guiView.login(username, server, port);
        } catch (NumberFormatException e) {
            statusLabel.setText("La porta deve essere un numero!");
        }
    }
}
