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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoseCrewMenuController implements MenuController.ContextDataReceiver, GameModelListener, BindCleanUp {
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

    /**
     * Initializes the lose crew menu controller.
     * This method is called automatically by JavaFX after loading the FXML file.
     * It retrieves and stores the current username from the game model for use
     * in crew loss operations.
     */
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
    }

    /**
     * Initializes the crew loss interface with the specified number of crew members to lose.
     * This method sets up the UI to allow the player to select which crew members to lose
     * by configuring the ship view and enabling cabin selection interactions.
     * 
     * @param crewToLose the number of crew members that must be lost
     */
    public void initializeWithCrewToLose(int crewToLose) {
        crewToLoseLabel.setText("You need to lose " + crewToLose + " crew members!");
        ship = ClientGameModel.getInstance().getShip(username);
        loadShipView();
        for(int i = 0; i < crewToLose; i++) {
            shipController.enableCellClickHandler(this::selectCabin);
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
            } catch (ClassCastException e) {
                showError("Unable to get the ship controller");
            }
        } catch (IOException e) {
            showError("Error while loading the ship view: " + e.getMessage());
        }
    }

    private void selectCabin(int row, int col) {
        cabins.add(new Pair<>(row, col));
        if (shipController != null) {
            shipController.highlightSelectedCabins(cabins);
        }
    }

    @FXML
    private void handleUndo() {
        if (!cabins.isEmpty()) {
            cabins.clear();
            if (shipController != null) {
                shipController.highlightSelectedCabins(cabins);
            }
        }
    }

    @FXML
    private void handleContinue() {
        try {
            ClientGameModel.getInstance().getClient().loseCrew(username, cabins);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
            ClientGameModel.getInstance().setFree();
        }
    }


    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Sets the context data for the crew loss menu.
     * This method processes the context data map to extract the number of crew members
     * that need to be lost and initializes the interface accordingly. The context data
     * must contain a "crewNum" key with an integer value.
     * 
     * @param contextData a map containing the context data, must include a "crewNum" key
     *                   with an integer value representing the number of crew to lose
     */
    @Override
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("crewNum")) {
            int crewToLose = (int) contextData.get("crewNum");
            initializeWithCrewToLose(crewToLose);
        } else {
            showError("No crew to lose data provided.");
        }
    }

    /**
     * Handles ship updates from the game model.
     * This method is called when the ship state changes in the game model
     * and reloads the ship view to reflect the current ship configuration.
     * 
     * @param ship the updated ship view data
     */
    @Override
    public void onShipUpdated(ViewShip ship) {
        loadShipView();
    }

    /**
     * Handles lobby updates from the game model.
     * This method is called when the lobby state changes but currently
     * performs no action for this controller as lobby updates are not
     * relevant to crew loss operations.
     * 
     * @param lobby the updated lobby view data
     */
    @Override
    public void onLobbyUpdated(ViewLobby lobby) {

    }

    /**
     * Handles error messages from the game model.
     * This method is called when error messages are received from the game model
     * but currently performs no action for this controller.
     * 
     * @param message the error message received
     */
    @Override
    public void onErrorMessageReceived(String message) {

    }

    /**
     * Handles component updates from the game model.
     * This method is called when components in hand are updated but currently
     * performs no action for this controller as component updates are not
     * relevant to crew loss operations.
     * 
     * @param component the updated component view data
     */
    @Override
    public void onComponentInHandUpdated(ViewComponent component) {

    }

    /**
     * Handles current card updates from the game model.
     * This method is called when the current adventure card changes but currently
     * performs no action for this controller as card updates are not
     * relevant to crew loss operations.
     * 
     * @param currentCard the updated current adventure card view data
     */
    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {

    }

    /**
     * Handles board updates from the game model.
     * This method is called when the game board state changes but currently
     * performs no action for this controller as board updates are not
     * relevant to crew loss operations.
     * 
     * @param board the updated board view data
     */
    @Override
    public void onBoardUpdated(ViewBoard board) {

    }

    /**
     * Performs cleanup operations to free resources and reset the controller state.
     * This method clears all UI components, unbinds properties, removes listeners,
     * and resets references to prevent memory leaks when the crew loss menu is
     * no longer needed. It performs comprehensive cleanup including:
     * - Removing game model listeners
     * - Cleaning up the ship controller
     * - Clearing and unbinding ship pane components
     * - Resetting all labels and clearing selections
     * - Setting all references to null
     */
    public void cleanup() {
        System.out.println("LoseCrewMenuController: Starting cleanup...");

        ClientGameModel gameModel = ClientGameModel.getInstance();
        if (gameModel != null) {
            gameModel.removeListener(this);
        }

        if (shipController != null) {
            try {
                shipController.cleanup();
            } catch (Exception e) {
                System.err.println("Error cleaning up ship controller: " + e.getMessage());
            }
            shipController = null;
        }

        if (shipPane != null) {
            shipPane.getChildren().clear();
            if (!shipPane.getChildren().isEmpty()) {
                Parent shipView = (Parent) shipPane.getChildren().getFirst();
                if (shipView != null) {
                    try {
                        Pane shipPaneTyped = (Pane) shipView;
                        shipPaneTyped.prefWidthProperty().unbind();
                        shipPaneTyped.prefHeightProperty().unbind();
                    } catch (Exception e) {
                        System.err.println("Error unbinding ship pane properties: " + e.getMessage());
                    }
                }
            }
        }

        if (crewToLoseLabel != null) {
            crewToLoseLabel.setText("");
        }

        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }

        cabins.clear();

        username = null;
        ship = null;
        crewToLoseLabel = null;
        errorLabel = null;
        shipPane = null;

        System.out.println("LoseCrewMenuController: Cleanup completed");
    }
}