package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class CannonsMenuController {
    // Rimuove "implements FXMLController"

    @FXML
    private Label messageLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private GridPane cannonsGrid;

    @FXML
    private GridPane batteriesGrid;

    @FXML
    private Button activateButton;

    @FXML
    private Button skipButton;

    @FXML
    private Pane shipPane;

    private CheckBox[][] cannonCheckboxes;
    private CheckBox[][] batteryCheckboxes;
    private Circle[][] cannonsCircles;
    private Circle[][] batteriesCircles;

    private String message;

    @FXML
    public void initialize() {
        if (message != null) {
            messageLabel.setText(message);
        }

        drawShip();
        setupCannonsGrid();
        setupBatteriesGrid();
    }

    public void setMessage(String message) {
        this.message = message;
        if (messageLabel != null) {
            messageLabel.setText(message);
        }
    }

    private void drawShip() {
        shipPane.getChildren().clear();

        ViewShip ship = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());
        if (ship == null) return;

        // Disegna la base della nave
        Rectangle shipBase = new Rectangle(50, 50, 400, 150);
        shipBase.setFill(Color.LIGHTGRAY);
        shipBase.setStroke(Color.BLACK);
        shipPane.getChildren().add(shipBase);

        // Disegna i cannoni e le batterie
        int cannonSize = 15;
        int batterySize = 20;
        double startX = 100;
        double startY = 70;

        cannonsCircles = new Circle[3][4];
        batteriesCircles = new Circle[3][3];

        // Disegna i cannoni
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                Circle cannon = new Circle(startX + j * 50, startY + i * 40, cannonSize);
                cannon.setFill(Color.DARKGRAY);
                cannon.setStroke(Color.BLACK);

                Text label = new Text(String.format("(%d,%d)", i + 5, j + 4));
                label.setX(cannon.getCenterX() - 15);
                label.setY(cannon.getCenterY() - 20);

                int finalI = i;
                int finalJ = j;
                cannon.setOnMouseClicked(event -> {
                    cannonCheckboxes[finalI][finalJ].setSelected(!cannonCheckboxes[finalI][finalJ].isSelected());
                    updateCannonVisual(finalI, finalJ);
                });

                cannonsCircles[i][j] = cannon;
                shipPane.getChildren().addAll(cannon, label);
            }
        }

        // Disegna le batterie
        boolean isLearner = ship.isLearner;
        double batteryStartX = startX + 50;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Circle battery = new Circle(batteryStartX + j * 50, startY + 120 + i * 20, batterySize / 2);
                battery.setFill(Color.DARKBLUE);
                battery.setStroke(Color.BLACK);

                int offset = isLearner ? 5 : 4;
                Text label = new Text(String.format("(%d,%d)", i + 5, j + offset));
                label.setX(battery.getCenterX() - 15);
                label.setY(battery.getCenterY() - 15);

                int finalI = i;
                int finalJ = j;
                battery.setOnMouseClicked(event -> {
                    batteryCheckboxes[finalI][finalJ].setSelected(!batteryCheckboxes[finalI][finalJ].isSelected());
                    updateBatteryVisual(finalI, finalJ);
                });

                batteriesCircles[i][j] = battery;
                shipPane.getChildren().addAll(battery, label);
            }
        }
    }

    private void updateCannonVisual(int i, int j) {
        boolean selected = cannonCheckboxes[i][j].isSelected();
        cannonsCircles[i][j].setFill(selected ? Color.RED : Color.DARKGRAY);
    }

    private void updateBatteryVisual(int i, int j) {
        boolean selected = batteryCheckboxes[i][j].isSelected();
        batteriesCircles[i][j].setFill(selected ? Color.LIGHTBLUE : Color.DARKBLUE);
    }

    private void setupCannonsGrid() {
        int rows = 3;
        int cols = 4;
        cannonCheckboxes = new CheckBox[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CheckBox checkbox = new CheckBox();
                checkbox.setText("(" + (i + 5) + "," + (j + 4) + ")");
                cannonCheckboxes[i][j] = checkbox;

                int finalI = i;
                int finalJ = j;
                checkbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    updateCannonVisual(finalI, finalJ);
                });

                cannonsGrid.add(checkbox, j, i);
            }
        }
    }

    private void setupBatteriesGrid() {
        int rows = 3;
        int cols = 3;
        batteryCheckboxes = new CheckBox[rows][cols];

        ViewShip ship = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());
        boolean isLearner = ship != null && ship.isLearner;
        int offset = isLearner ? 5 : 4;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CheckBox checkbox = new CheckBox();
                checkbox.setText("(" + (i + 5) + "," + (j + offset) + ")");
                batteryCheckboxes[i][j] = checkbox;

                int finalI = i;
                int finalJ = j;
                checkbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    updateBatteryVisual(finalI, finalJ);
                });

                batteriesGrid.add(checkbox, j, i);
            }
        }
    }

    @FXML
    private void handleActivateCannons() {
        List<Pair<Integer, Integer>> selectedCannons = getSelectedCannons();
        List<Pair<Integer, Integer>> selectedBatteries = getSelectedBatteries();

        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().activateCannons(
                    ClientGameModel.getInstance().getUsername(),
                    selectedCannons,
                    selectedBatteries
            );
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            errorLabel.setText("Errore di connessione: " + e.getMessage());
            errorLabel.setVisible(true);
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleSkipActivation() {
        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().activateCannons(
                    ClientGameModel.getInstance().getUsername(),
                    null,
                    null
            );
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            errorLabel.setText("Errore di connessione: " + e.getMessage());
            errorLabel.setVisible(true);
            ClientGameModel.getInstance().setFree();
        }
    }

    private List<Pair<Integer, Integer>> getSelectedCannons() {
        List<Pair<Integer, Integer>> selected = new ArrayList<>();

        for (int i = 0; i < cannonCheckboxes.length; i++) {
            for (int j = 0; j < cannonCheckboxes[i].length; j++) {
                if (cannonCheckboxes[i][j].isSelected()) {
                    selected.add(new Pair<>(i + 5, j + 4));
                }
            }
        }

        return selected;
    }

    private List<Pair<Integer, Integer>> getSelectedBatteries() {
        List<Pair<Integer, Integer>> selected = new ArrayList<>();
        ViewShip ship = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());
        boolean isLearner = ship != null && ship.isLearner;
        int offset = isLearner ? 5 : 4;

        for (int i = 0; i < batteryCheckboxes.length; i++) {
            for (int j = 0; j < batteryCheckboxes[i].length; j++) {
                if (batteryCheckboxes[i][j].isSelected()) {
                    selected.add(new Pair<>(i + 5, j + offset));
                }
            }
        }

        return selected;
    }
}