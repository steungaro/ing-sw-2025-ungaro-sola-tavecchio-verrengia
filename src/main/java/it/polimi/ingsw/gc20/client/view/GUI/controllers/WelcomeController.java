package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller class for the welcome screen of the application.
 * Handles the initialization and user interactions on the welcome view.
 */
public class WelcomeController {

    @FXML
    private Button playButton;

    private GUIView guiView;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the GUI view reference and configures event handlers for UI components.
     * This method is automatically called by the FXMLLoader when the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        guiView = (GUIView) ClientGameModel.getInstance();
        playButton.setOnAction(_ -> handlePlay());
    }

    private void handlePlay() {
        guiView.showScene("network");
    }
}