package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeController {

    @FXML
    private Button playButton;

    private GUIView guiView;

    @FXML
    public void initialize() {
        guiView = (GUIView) ClientGameModel.getInstance();
        playButton.setOnAction(_ -> handlePlay());
    }

    private void handlePlay() {
        guiView.showScene("network");
    }
}