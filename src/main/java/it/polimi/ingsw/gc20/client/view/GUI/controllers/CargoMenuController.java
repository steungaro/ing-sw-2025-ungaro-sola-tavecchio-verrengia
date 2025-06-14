package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CargoMenuController {
    @FXML private Label messageLabel;
    @FXML private Label errorLabel;
    @FXML private Label operationTitle;
    @FXML private Pane shipPane;
    @FXML private ComboBox<CargoColor> cargoColorComboBox;
    @FXML private TextField fromCoordinatesField;
    @FXML private TextField toCoordinatesField;
    @FXML private Button unloadButton;
    @FXML private Button moveButton;
    @FXML private Button loadButton;
    @FXML private Button endTurnButton;

    private Message message;
    private String username;
    private int cargoToLose;
    private List<CargoColor> cargoToGain;
    private boolean losing;
    private Pattern coordinatePattern = Pattern.compile("^\\d+\\s+\\d+$");
    private ViewShip ship;

    @FXML
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
        cargoColorComboBox.setItems(FXCollections.observableArrayList(CargoColor.values()));
    }

    public void initializeWithParameters(String message, int cargoToLose, List<CargoColor> cargoToGain, boolean losing) {
        this.cargoToLose = cargoToLose;
        this.cargoToGain = cargoToGain;
        this.losing = losing;

        messageLabel.setText(message);

        if (losing) {
            operationTitle.setText("You lose " + cargoToLose + " cargo");
            loadButton.setDisable(true);
        } else {
            StringBuilder cargoMessage = new StringBuilder("You gain: ");
            cargoToGain.forEach((color) ->
                    cargoMessage.append(color).append(", "));
            operationTitle.setText(cargoMessage.toString());
        }

        ship = ClientGameModel.getInstance().getShip(username);

        loadShipView();
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
    private void handleUnloadCargo() {
        if (!validateCargoColor() || !validateFromCoordinates()) {
            return;
        }

        CargoColor color = cargoColorComboBox.getValue();
        Pair<Integer, Integer> coords = parseCoordinates(fromCoordinatesField.getText());

        try {
            ClientGameModel.getInstance().getClient().unloadCargo(username, color, coords);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    @FXML
    private void handleMoveCargo() {
        if (!validateCargoColor() || !validateFromCoordinates() || !validateToCoordinates()) {
            return;
        }

        CargoColor color = cargoColorComboBox.getValue();
        Pair<Integer, Integer> from = parseCoordinates(fromCoordinatesField.getText());
        Pair<Integer, Integer> to = parseCoordinates(toCoordinatesField.getText());

        try {
            ClientGameModel.getInstance().getClient().moveCargo(username, color, from, to);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoadCargo() {
        if (!validateCargoColor() || !validateToCoordinates()) {
            return;
        }

        CargoColor color = cargoColorComboBox.getValue();
        Pair<Integer, Integer> coords = parseCoordinates(toCoordinatesField.getText());

        try {
            ClientGameModel.getInstance().getClient().loadCargo(username, color, coords);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    @FXML
    private void handleEndTurn() {
        try {
            ClientGameModel.getInstance().getClient().endMove(username);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());}
    }

    private boolean validateCargoColor() {
        if (cargoColorComboBox.getValue() == null) {
            showError("Select a cargo to load");
            return false;
        }
        return true;
    }

    private boolean validateFromCoordinates() {
        String text = fromCoordinatesField.getText();
        if (text == null || text.isEmpty() || !coordinatePattern.matcher(text).matches()) {
            showError("Enter valid coordinates for the origin (row col)");
            return false;
        }
        return true;
    }

    private boolean validateToCoordinates() {
        String text = toCoordinatesField.getText();
        if (text == null || text.isEmpty() || !coordinatePattern.matcher(text).matches()) {
            showError("Enter valid coordinates for the origin (row col)");
            return false;
        }
        return true;
    }

    private Pair<Integer, Integer> parseCoordinates(String input) {
        String[] parts = input.trim().split("\\s+");
        int row = Integer.parseInt(parts[0]) - 5;

        ViewShip ship = ClientGameModel.getInstance().getShip(username);
        boolean isLearner = ship != null && ship.isLearner;
        int offset = isLearner ? 5 : 4;

        int col = Integer.parseInt(parts[1]) - offset;

        return new Pair<>(row, col);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}