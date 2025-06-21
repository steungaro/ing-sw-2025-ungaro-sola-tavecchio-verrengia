package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class LoseCrewMenuController {
    @FXML
    private Label crewToLoseLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Pane shipPane;

    private String username;
    private ViewShip ship;
    private final List<Pair<Integer, Integer>> cabins = new ArrayList<>();
    private ShipController shipController;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
    }

    public void initializeWithCrewToLose(int crewToLose) {
        crewToLoseLabel.setText("You need to lose " + crewToLose + " crew members!");
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

            Object controller = loader.getController();
            shipController.enableCellClickHandler(this::selectCabin);


        } catch (IOException e) {
            showError("Error uploading ship: " + e.getMessage());
        }
    }

    private void selectCabin(int row, int col) {
        int cabinRow = row - 5;
        int cabinCol = col - (ship.isLearner ? 5 : 4);
        cabins.add(new Pair<>(cabinRow, cabinCol));
        if (shipController != null) {
            shipController.highlightSelectedCabins(cabins);
        }
    }

    @FXML
    private void handleUndo() {
        if (!cabins.isEmpty()) {
            cabins.removeLast();
            if (shipController != null) {
                shipController.highlightSelectedCabins(cabins);
            }
        }
    }

    @FXML
    private void handleContinue() {
        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().loseCrew(username, cabins);
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleViewOptions() {
        ClientGameModel.getInstance().setBusy();
        // TODO Call the options menu
    }


    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}