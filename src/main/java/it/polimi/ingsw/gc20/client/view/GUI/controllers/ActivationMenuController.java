package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

import static java.lang.Math.min;

public class ActivationMenuController implements MenuController.ContextDataReceiver, GameModelListener, BindCleanUp {

    @FXML public Button skipButton;

    /**
     * Performs cleanup operations for this controller.
     * This method removes listeners from the game model, unbinds node properties,
     * clears selected components, and nullifies references to prevent memory leaks.
     */
    @Override
    public void cleanup() {

        ClientGameModel gameModel = ClientGameModel.getInstance();
        if (gameModel != null) {
            gameModel.removeListener(this);
        }

        if (shipPane != null && !shipPane.getChildren().isEmpty()) {
            for (javafx.scene.Node child : shipPane.getChildren()) {
                unbindNodeProperties(child);
            }

            shipPane.getChildren().clear();
        }

        selectedComponents.clear();

        ship = null;
        username = null;
        activationType = null;

        if (titleLabel != null) {
            titleLabel.setText("");
        }
        if (messageLabel != null) {
            messageLabel.setText("");
        }
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }
    }

    private void unbindNodeProperties(javafx.scene.Node node) {
        if (node == null) return;

        try {
            try{
                Region region = (Region) node;
                region.prefWidthProperty().unbind();
                region.prefHeightProperty().unbind();
                region.minWidthProperty().unbind();
                region.minHeightProperty().unbind();
                region.maxWidthProperty().unbind();
                region.maxHeightProperty().unbind();
            } catch (ClassCastException e) {
                // If the node is not a Region, we skip this part
            }

            try{
                Pane pane = (Pane) node;
                pane.prefWidthProperty().unbind();
                pane.prefHeightProperty().unbind();

                for (javafx.scene.Node child : pane.getChildren()) {
                    unbindNodeProperties(child);
                }
            } catch (ClassCastException e) {
                // If the node is not a Pane, we skip this part
            }

            try{
                ImageView imageView = (ImageView) node;
                imageView.fitWidthProperty().unbind();
                imageView.fitHeightProperty().unbind();
            }  catch (ClassCastException e) {
                // If the node is not an ImageView, we skip this part
            }

            node.setOnMouseClicked(null);
            node.setOnMouseEntered(null);
            node.setOnMouseExited(null);

        } catch (Exception e) {
            System.err.println("Error unbinding node properties: " + e.getMessage());
        }
    }


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

    /**
     * Initializes the JavaFX components and sets up the initial state of the controller.
     * This method is automatically called by JavaFX after loading the FXML file.
     * It retrieves the current username and ship from the game model and loads the ship view.
     */
    @FXML
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
        ship = ClientGameModel.getInstance().getShip(username);
        loadShipView();
    }

    /**
     * Initializes the controller with specific activation data.
     * Sets the activation type, updates UI labels based on the activation type,
     * and configures the skip button visibility for branch selection.
     * 
     * @param type the type of activation to perform (CANNONS, ENGINES, SHIELDS, BATTERY, or BRANCH)
     * @param message the message to display to the user
     * @param batteryNum the number of batteries (currently unused in implementation)
     */
    public void initializeData(ActivationType type, String message, int batteryNum) {
        this.activationType = type;
        if(activationType == ActivationType.BRANCH)
            titleLabel.setText("Choose a branch");
        else
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

            shipPane.applyCss();
            shipPane.layout();

            Pane shipPaneTyped = (Pane) shipView;

            shipPaneTyped.setMinWidth(150);
            shipPaneTyped.setMinHeight(120);

            shipPaneTyped.setMaxWidth(Region.USE_COMPUTED_SIZE);
            shipPaneTyped.setMaxHeight(Region.USE_COMPUTED_SIZE);

            shipPaneTyped.prefWidthProperty().bind(shipPane.widthProperty());
            shipPaneTyped.prefHeightProperty().bind(shipPane.heightProperty());

            try{
                StackPane stackPane = (StackPane)shipPaneTyped;
                stackPane.setAlignment(javafx.geometry.Pos.CENTER);

            } catch (ClassCastException e) {
                showError("Error casting shipPaneTyped to StackPane: " + e.getMessage());
            }

            Platform.runLater(() -> {
                shipPane.requestLayout();
                shipPaneTyped.requestLayout();
            });

            Object controller = loader.getController();

            try{
                shipController = (ShipController) controller;
                shipController.enableCellClickHandler(this::selectComponent);
            } catch (ClassCastException e) {
                showError("Unable to get the ship controller");
            }
        } catch (IOException e) {
            showError("Error while loading the ship view: " + e.getMessage());
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

    /**
     * Sets the context data for this controller from the menu system.
     * Extracts activation type and message from context data and initializes the controller.
     * Supports two different context data formats:
     * 1. Contains "activationType" and "message" keys
     * 2. Contains "activationTyper" and "batteryNum" keys (for energy loss scenarios)
     * 
     * @param contextData a map containing context data with activation parameters
     * @throws IllegalArgumentException if context data doesn't contain the required keys
     */
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
        } else {
            throw new IllegalArgumentException("Context data must contain activationType and message");
        }
    }

    /**
     * Handles ship update events from the game model.
     * Reloads the ship view when the ship state is updated to reflect current changes.
     * 
     * @param ship the updated ship view object
     */
    @Override
    public void onShipUpdated(ViewShip ship) {
        loadShipView();
    }

    /**
     * Handles lobby update events from the game model.
     * Currently provides no implementation as lobby updates are not relevant for activation menus.
     * 
     * @param lobby the updated lobby view object
     */
    @Override
    public void onLobbyUpdated(ViewLobby lobby) {
        // Empty implementation - not relevant for activation menu
    }

    /**
     * Handles error message events from the game model.
     * Currently provides no implementation as error messages are handled separately.
     * 
     * @param message the error message received from the game model
     */
    @Override
    public void onErrorMessageReceived(String message) {
        // Empty implementation - errors handled through other mechanisms
    }

    /**
     * Handles component in hand update events from the game model.
     * Currently provides no implementation as hand components are not relevant for activation menus.
     * 
     * @param component the updated component in the player's hand
     */
    @Override
    public void onComponentInHandUpdated(ViewComponent component) {
        // Empty implementation - not relevant for activation menu
    }

    /**
     * Handles current adventure card update events from the game model.
     * Currently provides no implementation as adventure cards are not directly relevant for activation menus.
     * 
     * @param currentCard the updated current adventure card
     */
    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {
        // Empty implementation - not relevant for activation menu
    }

    /**
     * Handles board update events from the game model.
     * Currently provides no implementation as board updates are not directly relevant for activation menus.
     * 
     * @param board the updated game board view
     */
    @Override
    public void onBoardUpdated(ViewBoard board) {
        // Empty implementation - not relevant for activation menu
    }
}