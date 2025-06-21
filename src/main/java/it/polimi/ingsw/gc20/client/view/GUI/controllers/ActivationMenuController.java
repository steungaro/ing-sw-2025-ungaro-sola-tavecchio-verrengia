package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivationMenuController {

    public enum ActivationType {
        CANNONS,
        ENGINES,
        SHIELDS
    }

    @FXML
    private Label titleLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Pane shipPane;
    @FXML
    private ImageView cardImageView;

    private ShipController shipController;
    private ViewShip ship;
    private String username;
    private ActivationType activationType;

    private final List<Pair<Integer, Integer>> selectedComponents = new ArrayList<>();

    @FXML
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
        ship = ClientGameModel.getInstance().getShip(username);
        loadShipView();
    }

    public void initializeData(ActivationType type, String message, Integer cardId) {
        this.activationType = type;
        titleLabel.setText(type.toString() + " Activation");
        messageLabel.setText(message);

        if (cardId != null) {
            try {
                String imagePath = "/fxml/cards/GT-cards_I_IT_" + String.format("%03d", cardId) + ".jpg";
                Image cardImage = new Image(getClass().getResourceAsStream(imagePath));
                cardImageView.setImage(cardImage);
            } catch (Exception e) {
                showError("Could not load card image.");
            }
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
            if (controller instanceof ShipController) {
                this.shipController = (ShipController) controller;
                shipController.enableCellClickHandler(this::selectComponent);
            } else {
                showError("Unable to get the ship controller");
            }

        } catch (IOException e) {
            showError("Error uploading ship: " + e.getMessage());
        }
    }

    private void selectComponent(int row, int col) {
        int componentRow = row - 5;
        int componentCol = col - (ship.isLearner ? 5 : 4);
        selectedComponents.add(new Pair<>(componentRow, componentCol));
        updateHighlights();
    }

    @FXML
    private void handleUndo() {
        if (!selectedComponents.isEmpty()) {
            selectedComponents.remove(selectedComponents.size() - 1);
            updateHighlights();
        }
    }

    private void updateHighlights() {
        if (shipController == null) {
            return;
        }
        Map<Pair<Integer, Integer>, Color> highlights = new HashMap<>();
        for (Pair<Integer, Integer> coords : selectedComponents) {
            int gridRow = coords.getValue0() + 5;
            int gridCol = coords.getValue1() + (ship.isLearner ? 5 : 4);
            ViewComponent comp = ship.getComponent(gridRow, gridCol);
            if (comp != null) {
                Color color = getColorForComponent(comp);
                highlights.put(new Pair<>(gridRow, gridCol), color);
            }
        }
        shipController.highlightCells(highlights);
    }

    private Color getColorForComponent(ViewComponent component) {
        if (component.isCannon()) {
            return Color.RED;
        } else if (component.isEngine()) {
            return Color.BLUE;
        } else if (component.isShield()) {
            return Color.GREEN;
        } else if (component.isBattery()) {
            return Color.YELLOW;
        }
        return Color.TRANSPARENT;
    }


    @FXML
    private void handleActivate() {
        List<Pair<Integer, Integer>> primary = new ArrayList<>();
        List<Pair<Integer, Integer>> batteries = new ArrayList<>();

        for (Pair<Integer, Integer> coords : selectedComponents) {
            ViewComponent comp = ship.getComponent(coords.getValue0() + 5, coords.getValue1() + (ship.isLearner ? 5 : 4));
            if (comp == null) continue;

            if (comp.isBattery()) {
                batteries.add(coords);
                continue;
            }

            switch (activationType) {
                case CANNONS:
                    if (comp.isCannon()) {
                        primary.add(coords);
                    }
                    break;
                case ENGINES:
                    if (comp.isEngine()) {
                        primary.add(coords);
                    }
                    break;
                case SHIELDS:
                    if (comp.isShield()) {
                        primary.add(coords);
                    }
                    break;
            }
        }

        try {
            ClientGameModel.getInstance().setBusy();
            switch (activationType) {
                case CANNONS:
                    ClientGameModel.getInstance().getClient().activateCannons(username, primary, batteries);
                    break;
                case ENGINES:
                    ClientGameModel.getInstance().getClient().activateEngines(username, primary, batteries);
                    break;
                case SHIELDS:
                    //ClientGameModel.getInstance().getClient().activateShields(username, primary, batteries);
                    break;
            }
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    @FXML
    private void handleSkip() {
        try {
            ClientGameModel.getInstance().setBusy();
            switch (activationType) {
                case CANNONS:
                    ClientGameModel.getInstance().getClient().activateCannons(username, null, null);
                    break;
                case ENGINES:
                    ClientGameModel.getInstance().getClient().activateEngines(username, null, null);
                    break;
                case SHIELDS:
                    // ClientGameModel.getInstance().getClient().skipShields(username);
                    break;
            }
            ClientGameModel.getInstance().setFree();
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
