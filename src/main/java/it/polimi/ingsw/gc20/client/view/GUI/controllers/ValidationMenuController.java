package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;

public class ValidationMenuController {

    @FXML
    private Label validationStatusLabel;

    @FXML
    private Pane shipPane;

    @FXML
    private Button validateButton;

    @FXML
    private Label errorLabel;

    private String username;
    private boolean isLearnerShip;
    private ViewShip ship;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();

        ship = ClientGameModel.getInstance().getShip(username);
        isLearnerShip = ship != null && ship.isLearner;

        updateValidationStatus();
        loadShipView();
    }

    private void updateValidationStatus() {
        boolean isValid = ClientGameModel.getInstance().getShip(username).isValid();

        if (isValid) {
            validationStatusLabel.setText("Ship is already valid! Wait for other players before going to the next phase.");
            validationStatusLabel.setStyle("-fx-text-fill: #80ffaa;");
            validateButton.setDisable(true);
        } else {
            validationStatusLabel.setText("Ship is not valid");
            validationStatusLabel.setStyle("-fx-text-fill: #ff6b6b;"); // Red text
            validateButton.setDisable(false);
        }
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

            Object controller = loader.getController();
            if (controller instanceof ShipController shipController) {
                shipController.enableCellClickHandler(this::selectComponentToRemove);
            } else {
                showError("Unable to get the ship controller");
            }

        } catch (IOException e) {
            showError("Error while loading the ship view: " + e.getMessage());
        }
    }

    @FXML
    private void handleValidateShip() {
        try {
            updateValidationStatus();
        } catch (Exception e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    @FXML
    private void selectComponentToRemove(int row, int col) {
        try {
            int x = row - 5;
            int y = col - (isLearnerShip ? 5 : 4);

            Pair<Integer, Integer> coordinates = new Pair<>(row, col);

            ClientGameModel.getInstance().getClient().removeComponentFromShip(username, coordinates);

            loadShipView();
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for coordinates");
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
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