package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;

import java.rmi.RemoteException;

public class KeepPlayingMenuController {
    @FXML
    private ProgressIndicator loadingIndicator;

    /**
     * Initializes the keep playing menu controller and sets up the loading indicator.
     * This method is called automatically by JavaFX after loading the FXML file.
     * It configures the progress indicator to show an indeterminate progress animation,
     * indicating that the system is waiting for the player's decision or processing.
     */
    public void initialize() {
        loadingIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    }

    @FXML
    private void handleGiveUp() {
        try {
            ClientGameModel.getInstance().getClient().giveUp(ClientGameModel.getInstance().getUsername());
        } catch (RemoteException e) {
            ClientGameModel.getInstance().setErrorMessage("Connection error: " + e.getMessage());
        }
    }
}