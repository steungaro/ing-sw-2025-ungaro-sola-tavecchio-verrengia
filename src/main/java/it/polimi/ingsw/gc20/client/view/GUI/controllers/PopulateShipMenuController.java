package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Controller class for the ship population menu interface.
 * Handles user interactions for adding aliens to cabins and manages the ship view.
 * Implements GameModelListener to respond to game state changes and BindCleanUp for resource management.
 */
public class PopulateShipMenuController implements GameModelListener, BindCleanUp {
    @FXML
    private Pane shipPane;

    @FXML
    private Label errorLabel;

    private String username;
    private ViewShip ship;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Retrieves the current username and ship from the client game model and loads the ship view.
     */
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
                ShipController shipController = (ShipController) controller;
                shipController.enableCellClickHandler(this::selectCabinToPopulate);
            } catch (ClassCastException e) {
                showError("Unable to get the ship controller");
            }
        } catch (IOException e) {
            showError("Error while loading the ship view: " + e.getMessage());
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

    /**
     * Handles the finish population action triggered by the user.
     * Sends a request to the server to end the current move after the ship has been populated with aliens.
     * Sets the game model to busy during the operation and free afterwards.
     */
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

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Handles ship update events from the game model.
     * Reloads the ship view when an update is received.
     *
     * @param ship The updated ship view model
     */
    @Override
    public void onShipUpdated(ViewShip ship) {
        loadShipView();
    }

    /**
     * Handles lobby update events from the game model.
     * This implementation is empty as it's not relevant for this controller.
     *
     * @param lobby The updated lobby view model
     */
    @Override
    public void onLobbyUpdated(ViewLobby lobby) {

    }

    /**
     * Handles error message events from the game model.
     * This implementation is empty as errors are handled differently in this controller.
     *
     * @param message The error message received
     */
    @Override
    public void onErrorMessageReceived(String message) {

    }

    /**
     * Handles component in hand update events from the game model.
     * This implementation is empty as it's not relevant for this controller.
     *
     * @param component The updated component view model
     */
    @Override
    public void onComponentInHandUpdated(ViewComponent component) {

    }

    /**
     * Handles current card update events from the game model.
     * This implementation is empty as it's not relevant for this controller.
     *
     * @param currentCard The updated adventure card view model
     */
    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {

    }

    /**
     * Handles board update events from the game model.
     * This implementation is empty as it's not relevant for this controller.
     *
     * @param board The updated board view model
     */
    @Override
    public void onBoardUpdated(ViewBoard board) {

    }

    /**
     * Cleans up resources used by this controller.
     * Removes listeners, unbinds properties, and clears references to avoid memory leaks.
     * Should be called when the view is no longer needed.
     */
    public void cleanup() {

        ClientGameModel gameModel = ClientGameModel.getInstance();
        if (gameModel != null) {
            gameModel.removeListener(this);
        }

        if (shipPane != null) {
            if (!shipPane.getChildren().isEmpty()) {
                try {
                    Parent shipView = (Parent) shipPane.getChildren().getFirst();
                    if (shipView != null) {
                        try {
                            Pane shipPaneTyped = (Pane) shipView;
                            shipPaneTyped.prefWidthProperty().unbind();
                            shipPaneTyped.prefHeightProperty().unbind();
                            shipPaneTyped.setMaxWidth(Region.USE_PREF_SIZE);
                            shipPaneTyped.setMaxHeight(Region.USE_PREF_SIZE);
                        } catch (Exception e) {
                            System.err.println("Error unbinding ship pane properties: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error accessing ship view: " + e.getMessage());
                }
            }
            shipPane.getChildren().clear();
        }

        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }

        username = null;
        ship = null;
        shipPane = null;
        errorLabel = null;
    }
}