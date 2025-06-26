package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanetMenuController implements MenuController.ContextDataReceiver, BindCleanUp {
    @FXML
    private HBox planetsContainer;

    @FXML
    private Label errorLabel;

    private List<Planet> planets;
    private String username;
    private Planet selectedPlanet;
    private VBox selectedPlanetBox;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
    }

    public void initializeWithPlanets(List<Planet> planets) {
        this.planets = planets;
        setupPlanetsDisplay();
    }

    private void setupPlanetsDisplay() {
        planetsContainer.getChildren().clear();
        planetsContainer.setSpacing(10);
        planetsContainer.setPadding(new Insets(10));

        for (int i = 0; i < planets.size(); i++) {
            Planet planet = planets.get(i);
            VBox planetBox = createPlanetBox(planet, i + 1);
            planetsContainer.getChildren().add(planetBox);
        }
    }

    private VBox createPlanetBox(Planet planet, int planetNumber) {
        VBox planetContainer = new VBox(5);
        planetContainer.setPadding(new Insets(10));
        planetContainer.setStyle("-fx-border-color: #4a7eb3; -fx-border-width: 2; -fx-background-color: #333344; -fx-background-radius: 8;");
        planetContainer.setMinWidth(150);

        Label planetNameLabel = new Label("Planet " + planetNumber);
        planetNameLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        planetNameLabel.setTextFill(Color.WHITE);
        planetNameLabel.setWrapText(true);

        VBox cargoContainer = new VBox(3);
        Label cargoLabel = new Label("Cargos:");
        cargoLabel.setFont(Font.font("System", 10));
        cargoLabel.setTextFill(Color.web("#80ffaa"));
        
        HBox cargoSquares = new HBox(2);
        
        if (planet.getReward() != null && !planet.getReward().isEmpty()) {
            for (CargoColor cargo : planet.getReward()) {
                Rectangle cargoSquare = createCargoSquare(cargo);
                cargoSquares.getChildren().add(cargoSquare);
            }
        }

        cargoContainer.getChildren().addAll(cargoLabel, cargoSquares);
        planetContainer.getChildren().addAll(planetNameLabel, cargoContainer);

        planetContainer.setDisable(!planet.getAvailable());

        planetContainer.setOnMouseClicked(_ -> selectPlanet(planet, planetContainer));

        planetContainer.setOnMouseEntered(_ ->
            planetContainer.setStyle("-fx-border-color: #80ffaa; -fx-border-width: 3; -fx-background-color: #3e4f5e; -fx-background-radius: 8;"));
        
        planetContainer.setOnMouseExited(_ -> {
            if (selectedPlanetBox != planetContainer) {
                planetContainer.setStyle("-fx-border-color: #4a7eb3; -fx-border-width: 2; -fx-background-color: #333344; -fx-background-radius: 8;");
            }
        });

        return planetContainer;
    }

    private void selectPlanet(Planet planet, VBox planetBox) {
        if (selectedPlanetBox != null) {
            selectedPlanetBox.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-color: #FAFAFA;");
        }
        
        selectedPlanet = planet;
        selectedPlanetBox = planetBox;
        planetBox.setStyle("-fx-border-color: #0078D4; -fx-border-width: 2; -fx-background-color: #E6F3FF;");
    }

    private Rectangle createCargoSquare(CargoColor cargo) {
        Rectangle square = new Rectangle(14, 14);
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

    @FXML
    private void handleLandOnPlanet() {
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

    public void cleanup() {
        System.out.println("PlanetMenuController: Starting cleanup...");

        if (planetsContainer != null) {
            for (javafx.scene.Node node : planetsContainer.getChildren()) {
                try {
                    VBox planetContainer = (VBox) node;
                    planetContainer.setOnMouseClicked(null);
                    planetContainer.setOnMouseEntered(null);
                    planetContainer.setOnMouseExited(null);

                    for (javafx.scene.Node child : planetContainer.getChildren()) {
                        if (child.getClass().equals(VBox.class)) {
                            try {
                                VBox cargoContainer = (VBox) child;
                                for (javafx.scene.Node cargoChild : cargoContainer.getChildren()) {
                                    if (cargoChild.getClass().equals(HBox.class)) {
                                        try {
                                            HBox cargoSquares = (HBox) cargoChild;
                                            for (javafx.scene.Node square : cargoSquares.getChildren()) {
                                                try {
                                                    Rectangle rect = (Rectangle) square;
                                                    rect.setFill(null);
                                                    rect.setStroke(null);
                                                } catch (Exception e) {
                                                    System.err.println("Error cleaning up cargo square: " + e.getMessage());
                                                }
                                            }
                                            cargoSquares.getChildren().clear();
                                        } catch (Exception e) {
                                            System.err.println("Error cleaning up cargo squares container: " + e.getMessage());
                                        }
                                    }
                                }
                                cargoContainer.getChildren().clear();
                            } catch (Exception e) {
                                System.err.println("Error cleaning up cargo container: " + e.getMessage());
                            }
                        }
                    }

                    planetContainer.getChildren().clear();
                } catch (Exception e) {
                    System.err.println("Error cleaning up planet container: " + e.getMessage());
                }
            }
            planetsContainer.getChildren().clear();
        }

        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }

        if (planets != null) {
            planets.clear();
            planets = null;
        }

        username = null;
        selectedPlanet = null;
        selectedPlanetBox = null;
        planetsContainer = null;
        errorLabel = null;

        System.out.println("PlanetMenuController: Cleanup completed");
    }
}