package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanetMenuController implements MenuController.ContextDataReceiver {
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
        setupPlanetListView();
    }

    public void initializeWithPlanets(List<Planet> planets) {
        this.planets = planets;
        planetsListView.getItems().clear();
        planetsListView.getItems().addAll(planets);
    }

    private void setupPlanetListView() {
        planetsListView.setCellFactory(listView -> new ListCell<Planet>() {
            @Override
            protected void updateItem(Planet planet, boolean empty) {
                super.updateItem(planet, empty);

                if (empty || planet == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    VBox planetContainer = new VBox(5);
                    planetContainer.setPadding(new Insets(8, 10, 8, 10));

                    // Planet name label
                    int planetNumber = planetsListView.getItems().indexOf(planet) + 1;
                    Label planetNameLabel = new Label("Planet " + planetNumber + (planet.getAvailable() ? " (Available)" : " (Not Available)"));
                    planetNameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

                    // Cargo display
                    HBox cargoContainer = new HBox(3);
                    Label cargoLabel = new Label("Cargos: ");
                    cargoLabel.setFont(Font.font("System", 12));
                    cargoContainer.getChildren().add(cargoLabel);

                    // Add colored squares for each cargo
                    if (planet.getReward() != null && !planet.getReward().isEmpty()) {
                        for (CargoColor cargo : planet.getReward()) {
                            Rectangle cargoSquare = createCargoSquare(cargo);
                            cargoContainer.getChildren().add(cargoSquare);
                        }
                    }

                    planetContainer.getChildren().addAll(planetNameLabel, cargoContainer);
                    setGraphic(planetContainer);
                    setText(null);
                }
            }
        });
    }

    private Rectangle createCargoSquare(CargoColor cargo) {
        Rectangle square = new Rectangle(16, 16);
        square.setStroke(Color.DARKGRAY);
        square.setStrokeWidth(1);

        // Determine color based on cargo type/color
        switch (cargo) {
            case GREEN -> square.setFill(Color.GREEN);
            case BLUE -> square.setFill(Color.BLUE);
            case RED -> square.setFill(Color.RED);
            case YELLOW -> square.setFill(Color.YELLOW);
        }
        return square;
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

    @Override
    @SuppressWarnings("unchecked")
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("planets")) {
            try {
                planets = (List<Planet>) contextData.get("planets");
            } catch (ClassCastException e) {
                System.err.println("Error while creating planets list: " + e.getMessage());
                planets = new ArrayList<>();
            }
            initializeWithPlanets(planets);
        } else {
            showError("No planets data provided");
        }
    }
}