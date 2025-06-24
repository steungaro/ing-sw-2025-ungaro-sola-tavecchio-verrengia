package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
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
import java.util.Optional;
import java.util.regex.Pattern;

public class CargoMenuController implements MenuController.ContextDataReceiver {
    @FXML private Label messageLabel;
    @FXML private Label errorLabel;
    @FXML private Pane shipPane;
    @FXML private Button loadButton;
    private String username;
    private final Pattern coordinatePattern = Pattern.compile("^\\d+\\s+\\d+$");
    private ViewShip ship;
    private ShipController shipController;
    private int losing; // 0 aren't initialized, 1 losing cargo, 2 gaining cargo

    @FXML
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
        ship = ClientGameModel.getInstance().getShip(username);


        loadShipView();
    }

    public void initializeWithParameters(String message) {
        messageLabel.setText(message);

        while (shipController==null) {
            try {
                wait(100);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                showError("Error initializing ship controller: " + e.getMessage());
            }
        }
        if(losing==1){
            shipController.enableCellClickHandler(this::handleUnloadCargo);
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
            try {
                this.shipController = (ShipController) controller;
            } catch (ClassCastException e) {
                showError("Unable to get the ship controller");
            }

        } catch (IOException e) {
            showError("Error uploading ship: " + e.getMessage());
        }
    }

    @FXML
    private void handleUnloadCargo(int row, int col) {
        Pair<Integer, Integer> coords = new Pair<>(row, col);
        CargoColor color = showColorSelectionDialog();

        if (color == null) {
            return;
        }

        try {
            ClientGameModel.getInstance().getClient().unloadCargo(username, color, coords);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    public static CargoColor showColorSelectionDialog() {
        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setTitle("Cargo Selection");
        dialog.setHeaderText("Select Cargo Color");
        dialog.setContentText("Choose one of the available colors:");

        ButtonType redButton = new ButtonType("Red", ButtonBar.ButtonData.OK_DONE);
        ButtonType yellowButton = new ButtonType("Yellow", ButtonBar.ButtonData.OK_DONE);
        ButtonType blueButton = new ButtonType("Blue", ButtonBar.ButtonData.OK_DONE);
        ButtonType greenButton = new ButtonType("Green", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(redButton, yellowButton, blueButton, greenButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (result.get() == redButton) {
                return CargoColor.RED;
            } else if (result.get() == yellowButton) {
                return CargoColor.YELLOW;
            } else if (result.get() == blueButton) {
                return CargoColor.BLUE;
            } else if (result.get() == greenButton) {
                return CargoColor.GREEN;
            }
        }
        return null;
    }


    @FXML
    private void handleMoveCargo() {
        if (!validateCargoColor() || !validateFromCoordinates() || validateToCoordinates()) {
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
        if (!validateCargoColor() || validateToCoordinates()) {
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
            return true;
        }
        return false;
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

    @Override
    public void setContextData(Map<String, Object> contextData) {
        int losing = 1;
        List<CargoColor> cargoToGain = null;
        if (contextData.containsKey("cargoNum")) {
            int cargoToLose = (int) contextData.get("cargoNum");
            if (cargoToLose <= 0) {
                showError("No cargo available to load/unload");
                return;
            }
            String message = "Select cargo to remove: " + cargoToLose;
            initializeWithParameters(message);
        } else {
           showError("No cargo available to load/unload");
        }
    }
}