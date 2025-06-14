package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class PlanetMenuController {
    @FXML
    private ListView<Planet> planetsListView;

    @FXML
    private Label errorLabel;

    @FXML
    private Pane shipPane;

    private ViewShip ship;
    private List<Planet> planets;
    private String username;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
        ship = ClientGameModel.getInstance().getShip(username);
        loadShipView();
    }

    public void initializeWithPlanets(List<Planet> planets) {
        this.planets = planets;
        planetsListView.getItems().clear();
        planetsListView.getItems().addAll(planets);
    }

    private void loadShipView() {
        try {
            if (ship == null) {
                showError("Error, ship not found: " + username);
                return;
            }

            String fxmlPath = ship.isLearner ? "/fxml/ship0.fxml" : "/fxml/ship2.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent shipView = loader.load();

            shipPane.getChildren().clear();
            shipPane.getChildren().add(shipView);

            ((Pane) shipView).prefWidthProperty().bind(shipPane.widthProperty());
            ((Pane) shipView).prefHeightProperty().bind(shipPane.heightProperty());

        } catch (IOException e) {
            showError("Error uploading ship: " + e.getMessage());
        }
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
            ClientGameModel.getInstance().getClient().landOnPlanet(username, planetIndex);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleContinueJourney() {
        try {
            ClientGameModel.getInstance().getClient().endMove(username);
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