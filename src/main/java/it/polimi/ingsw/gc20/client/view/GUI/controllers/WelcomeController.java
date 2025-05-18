package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeController {

    @FXML
    private Button playButton;

    private GUIView guiView;

    @FXML
    public void initialize() {
        guiView = (GUIView) View.getInstance();

        playButton.setOnAction(event -> handlePlay());
    }

    private void handlePlay() {
        guiView.showNetworkScene();
    }
}