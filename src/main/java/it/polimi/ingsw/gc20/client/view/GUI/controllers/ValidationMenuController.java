package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;

public class ValidationMenuController {

    @FXML
    private Label validationStatusLabel;

    @FXML
    private Pane shipPane;

    @FXML
    private VBox removeComponentBox;

    @FXML
    private TextField rowField;

    @FXML
    private TextField colField;

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

        // TODO: Load the ship view into the shipPane
    }

    private void updateValidationStatus() {
        boolean isValid = ClientGameModel.getInstance().getShip(username).isValid();

        if (isValid) {
            validationStatusLabel.setText("Ship is already valid! Wait for other players before going to the next phase.");
            validationStatusLabel.setStyle("-fx-text-fill: #80ffaa;");
            removeComponentBox.setDisable(true);
            validateButton.setDisable(true);
        } else {
            validationStatusLabel.setText("Ship is not valid");
            validationStatusLabel.setStyle("-fx-text-fill: #ff6b6b;"); // Red text
            removeComponentBox.setDisable(false);
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

        } catch (IOException e) {
            showError("Errore durante il caricamento della vista della nave: " + e.getMessage());
        }
    }

    @FXML
    private void handleValidateShip() {
        try {
            // TODO
            updateValidationStatus();
        } catch (Exception e) {
            // TODO
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleRemoveComponent() {
        try {
            int row = Integer.parseInt(rowField.getText().trim()) - 5;
            int col = Integer.parseInt(colField.getText().trim()) - (isLearnerShip ? 5 : 4);

            if (row < 0 || row > 4 || col < 0 || col > 6) {
                showError("Invalid coordinates. Row must be between 5-9 and column between " +
                        (isLearnerShip ? "5-11" : "4-10"));
                return;
            }

            Pair<Integer, Integer> coordinates = new Pair<>(row, col);

            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().removeComponentFromShip(username, coordinates);
            ClientGameModel.getInstance().setFree();

            rowField.clear();
            colField.clear();

            loadShipView();
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for coordinates");
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