package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.javatuples.Pair;

import java.rmi.RemoteException;
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

    private String username;
    private int cargoToLose;
    private Map<CargoColor, Integer> cargoToGain;
    private boolean losing;
    private Pattern coordinatePattern = Pattern.compile("^\\d+\\s+\\d+$");

    @FXML
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
        cargoColorComboBox.setItems(FXCollections.observableArrayList(CargoColor.values()));
    }

    public void initializeWithParameters(String message, int cargoToLose, Map<CargoColor, Integer> cargoToGain, boolean losing) {
        this.cargoToLose = cargoToLose;
        this.cargoToGain = cargoToGain;
        this.losing = losing;

        messageLabel.setText(message);

        if (losing) {
            operationTitle.setText("Devi perdere " + cargoToLose + " cargo");
            loadButton.setDisable(true);
        } else {
            StringBuilder cargoMessage = new StringBuilder("Devi guadagnare: ");
            cargoToGain.forEach((color, quantity) ->
                    cargoMessage.append(color).append(" ").append(quantity).append(", "));
            operationTitle.setText(cargoMessage.toString());
        }

        loadShipView();
    }

    private void loadShipView() {
        ViewShip ship = ClientGameModel.getInstance().getShip(username);
    }

    @FXML
    private void handleUnloadCargo() {
        if (!validateCargoColor() || !validateFromCoordinates()) {
            return;
        }

        CargoColor color = cargoColorComboBox.getValue();
        Pair<Integer, Integer> coords = parseCoordinates(fromCoordinatesField.getText());

        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().unloadCargo(username, color, coords);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Errore di connessione: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
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
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().moveCargo(username, color, from, to);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Errore di connessione: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
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
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().loadCargo(username, color, coords);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Errore di connessione: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleEndTurn() {
        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().endMove(username);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Errore di connessione: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    private boolean validateCargoColor() {
        if (cargoColorComboBox.getValue() == null) {
            showError("Seleziona un colore del carico");
            return false;
        }
        return true;
    }

    private boolean validateFromCoordinates() {
        String text = fromCoordinatesField.getText();
        if (text == null || text.isEmpty() || !coordinatePattern.matcher(text).matches()) {
            showError("Inserisci coordinate valide per l'origine (riga col)");
            return false;
        }
        return true;
    }

    private boolean validateToCoordinates() {
        String text = toCoordinatesField.getText();
        if (text == null || text.isEmpty() || !coordinatePattern.matcher(text).matches()) {
            showError("Inserisci coordinate valide per la destinazione (riga col)");
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