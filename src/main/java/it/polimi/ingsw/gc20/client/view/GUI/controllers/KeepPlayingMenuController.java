package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;

import java.rmi.RemoteException;

public class KeepPlayingMenuController {
    @FXML
    private ProgressIndicator loadingIndicator;

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