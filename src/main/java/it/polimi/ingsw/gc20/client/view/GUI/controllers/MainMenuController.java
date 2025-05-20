package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class MainMenuController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button viewLobbiesButton;

    @FXML
    private Button createLobbyButton;

    @FXML
    private Button logoutButton;

    private GUIView guiView;
    private String username;

    @FXML
    public void initialize() {
        guiView = (GUIView) ClientGameModel.getInstance();

        createLobbyButton.setOnAction(event -> handleCreateLobby());
        viewLobbiesButton.setOnAction( event -> handleViewLobbies());
        logoutButton.setOnAction(event -> handleLogout());
    }

    public void setUsername(String username) {
        this.username = username;
        welcomeLabel.setText("Benvenuto, " + username + "!");
    }

    private void handleCreateLobby() {
        guiView.showScene("createLobby");
    }
    private void handleViewLobbies() {
        guiView.showScene("lobbiesList");
    }
    private void handleLogout() {
        // TODO Disconnect
        guiView.showScene("network");
    }
}