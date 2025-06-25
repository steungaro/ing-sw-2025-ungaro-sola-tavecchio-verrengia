package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

import static java.lang.Math.min;

public class ActivationMenuController implements MenuController.ContextDataReceiver {

    @FXML public Button skipButton;

    public enum ActivationType {
        CANNONS {
            @Override
            public boolean matches(ViewComponent component) {
                return component.isCannon();
            }
            @Override
            public void activate(String username, List<Pair<Integer, Integer>> primary, List<Pair<Integer, Integer>> batteries) throws RemoteException {
                ClientGameModel.getInstance().getClient().activateCannons(username, primary, batteries);
            }
            @Override
            public void skip(String username) throws RemoteException {
                ClientGameModel.getInstance().getClient().activateCannons(username, new ArrayList<>(), new ArrayList<>());
            }
        },
        ENGINES {
            @Override
            public boolean matches(ViewComponent component) {
                return component.isEngine();
            }
            @Override
            public void activate(String username, List<Pair<Integer, Integer>> primary, List<Pair<Integer, Integer>> batteries) throws RemoteException {
                ClientGameModel.getInstance().getClient().activateEngines(username, primary, batteries);
            }
            @Override
            public void skip(String username) throws RemoteException {
                ClientGameModel.getInstance().getClient().activateEngines(username, new ArrayList<>(), new ArrayList<>());
            }
        },
        SHIELDS {
            @Override
            public boolean matches(ViewComponent component) {
                return component.isShield();
            }
            @Override
            public void activate(String username, List<Pair<Integer, Integer>> primary, List<Pair<Integer, Integer>> batteries) throws RemoteException {
                for(int i = 0; i < min(primary.size(), batteries.size()); i++){
                    Pair<Integer, Integer> shieldCoordinates = primary.get(i);
                    Pair<Integer, Integer> batteryCoordinates = batteries.get(i);
                    ClientGameModel.getInstance().getClient().activateShield(username, shieldCoordinates, batteryCoordinates);
                }
            }
            @Override
            public void skip(String username) throws RemoteException {
                ClientGameModel.getInstance().getClient().activateShield(username, null, null);
            }
        },
        BATTERY {
            @Override
            public boolean matches(ViewComponent component) {
                return component.isBattery();
            }
            @Override
            public void activate(String username, List<Pair<Integer, Integer>> primary, List<Pair<Integer, Integer>> batteries) throws RemoteException {
                for (Pair<Integer, Integer> batteryCoordinates : primary) {
                    ClientGameModel.getInstance().getClient().loseEnergy(username, batteryCoordinates);
                }
            }
            @Override
            public void skip(String username) throws RemoteException {
                ClientGameModel.getInstance().getClient().endMove(username);
            }
        },
        BRANCH{
            @Override
            public boolean matches(ViewComponent component) {
                return true;
            }
            @Override
            public void activate(String username, List<Pair<Integer, Integer>> primary, List<Pair<Integer, Integer>> batteries) throws RemoteException {
                ClientGameModel.getInstance().getClient().chooseBranch(username, primary.getLast());
            }
            @Override
            public void skip(String username) throws RemoteException {
                ClientGameModel.getInstance().getClient().chooseBranch(username, null);
            }

        };

        public abstract boolean matches(ViewComponent component);
        public abstract void activate(String username, List<Pair<Integer, Integer>> primary, List<Pair<Integer, Integer>> batteries) throws RemoteException;
        public abstract void skip(String username) throws RemoteException;
    }

    @FXML
    private Label titleLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Pane shipPane;

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
        shipController.enableCellClickHandler(this::selectComponent);
    }

    public void initializeData(ActivationType type, String message, int batteryNum) {
        this.activationType = type;
        titleLabel.setText(type.toString() + " Activation");
        messageLabel.setText(message);

        if(activationType == ActivationType.BRANCH)
            skipButton.setVisible(false);
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

    private void selectComponent(int row, int col) {
        selectedComponents.add(new Pair<>(row, col));
        updateHighlights();
    }

    @FXML
    private void handleUndo() {
        if (!selectedComponents.isEmpty()) {
            selectedComponents.clear();
            updateHighlights();
        }
    }

    private void updateHighlights() {
        // TODO: fix
        if (shipController == null) {
            return;
        }
        Map<Pair<Integer, Integer>, Color> highlights = new HashMap<>();
        for (Pair<Integer, Integer> coords : selectedComponents) {
            int gridRow = coords.getValue0();
            int gridCol = coords.getValue1();
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
        try {
            List<Pair<Integer, Integer>> primary = new ArrayList<>();
            List<Pair<Integer, Integer>> batteries = new ArrayList<>();

            separateComponentsByType(primary, batteries, activationType::matches);

            activationType.activate(username, primary, batteries);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    private void separateComponentsByType(List<Pair<Integer, Integer>> primary,
                                        List<Pair<Integer, Integer>> batteries,
                                        java.util.function.Predicate<ViewComponent> componentMatcher) {
        for (Pair<Integer, Integer> coords : selectedComponents) {
            ViewComponent comp = ship.getComponent(coords.getValue0(), coords.getValue1());
            if (comp == null) continue;
            if (componentMatcher.test(comp)) {
                primary.add(coords);
            } else if (comp.isBattery()) {
                batteries.add(coords);
            }
        }
    }

    @FXML
    private void handleSkip() {
        try {
            activationType.skip(username);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }


    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @Override
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("activationType") && contextData.containsKey("message")) {
            activationType = (ActivationType) contextData.get("activationType");
            String message = (String) contextData.get("message");
            initializeData(activationType, message, 0);
        } else if(contextData.containsKey("activationTyper") &&  contextData.containsKey("batteryNum")) {
            activationType = (ActivationType) contextData.get("activationTyper");
            int batteryNum = (int) contextData.get("batteryNum");
            String message = "You have to lose " + batteryNum + " energy because you are short on cargo!";
            initializeData(activationType, message, batteryNum);
        }


        else {
            throw new IllegalArgumentException("Context data must contain activationType and message");
        }

    }
}