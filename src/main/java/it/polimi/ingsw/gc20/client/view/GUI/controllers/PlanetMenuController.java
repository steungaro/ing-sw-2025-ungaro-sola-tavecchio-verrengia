package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.rmi.RemoteException;
import java.util.List;

public class PlanetMenuController {
    @FXML
    private ListView<Planet> planetsListView;

    @FXML
    private Label errorLabel;

    private List<Planet> planets;
    private String username;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
    }

    public void initializeWithPlanets(List<Planet> planets) {
        this.planets = planets;
        planetsListView.getItems().clear();
        planetsListView.getItems().addAll(planets);
    }

    @FXML
    private void handleLandOnPlanet() {
        Planet selectedPlanet = planetsListView.getSelectionModel().getSelectedItem();
        if (selectedPlanet == null) {
            showError("Please select a planet to land on");
            return;
        }

        try {
            int planetIndex = planets.indexOf(selectedPlanet);
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().landOnPlanet(username, planetIndex);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleContinueJourney() {
        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().endMove(username);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleViewOptions() {
        // TODO
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}