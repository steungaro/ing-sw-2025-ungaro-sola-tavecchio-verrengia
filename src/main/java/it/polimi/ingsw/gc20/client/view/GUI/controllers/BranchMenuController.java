package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.javatuples.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BranchMenuController {
// TODO -> Make the shipGrid clickable, so that the user can select a branch by clicking on it.
    @FXML
    private Pane shipPane;

    @FXML
    private Button confirmButton;

    @FXML
    private Label errorLabel;

    private String username;
    private ViewShip ship;
    private Pair<Integer, Integer> selectedCoordinates;
    private Map<Pair<Integer, Integer>, Rectangle> branchHighlights = new HashMap<>();

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
        this.ship = ClientGameModel.getInstance().getShip(username);
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

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}