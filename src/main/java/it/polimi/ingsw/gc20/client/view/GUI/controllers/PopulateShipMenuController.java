package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;

public class PopulateShipMenuController {
    @FXML
    private Pane shipPane;

    @FXML
    private Label errorLabel;

    private String username;
    private ViewShip ship;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
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
            if (controller instanceof ShipController shipController) {
                shipController.enableCellClickHandler(this::selectCabinToPopulate);
            } else {
                showError("Unable to get the ship controller");
            }

        } catch (IOException e) {
            showError("Error uploading ship: " + e.getMessage());
        }
    }

    private void selectCabinToPopulate(int row, int col) {
        VBox dialogContent = new VBox(10);
        ComboBox<AlienColor> colorComboBox = new ComboBox<>(
                FXCollections.observableArrayList(AlienColor.PURPLE, AlienColor.BROWN)
        );
        colorComboBox.setPromptText("Select an alien color");
        colorComboBox.setValue(AlienColor.PURPLE);

        dialogContent.getChildren().add(new Label("Color:"));
        dialogContent.getChildren().add(colorComboBox);

        javafx.scene.control.Dialog<AlienColor> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Add alien");
        dialog.setHeaderText("Choose the alien color for the cabin at position (" + (row + 5) + ", " + (col + (ship.isLearner ? 5 : 4)) + ")");
        dialog.getDialogPane().setContent(dialogContent);

        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == javafx.scene.control.ButtonType.OK) {
                return colorComboBox.getValue();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(alienColor -> {
            try {
                ClientGameModel.getInstance().getClient().addAlien(
                        username,
                        alienColor,
                        new Pair<>(row, col)
                );
            } catch (RemoteException e) {
                showError("Error adding alien: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleFinishPopulation() {
        try {
            ClientGameModel.getInstance().setBusy();
            ClientGameModel.getInstance().getClient().endMove(username);
            ClientGameModel.getInstance().setFree();
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