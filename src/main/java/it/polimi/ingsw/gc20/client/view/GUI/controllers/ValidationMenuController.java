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

public class ValidationMenuController implements GameModelListener {

    @FXML
    private Label validationStatusLabel;

    @FXML
    private Pane shipPane;

    @FXML
    private Label errorLabel;

    private String username;
    private ViewShip ship;

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();

        ship = ClientGameModel.getInstance().getShip(username);

        updateValidationStatus();
        loadShipView();
    }

    private void updateValidationStatus() {
        boolean isValid = ClientGameModel.getInstance().getShip(username).isValid();

        if (isValid) {
            validationStatusLabel.setText("Ship is already valid! Wait for other players before going to the next phase.");
            validationStatusLabel.setStyle("-fx-text-fill: #80ffaa;");
        } else {
            validationStatusLabel.setText("Ship is not valid");
            validationStatusLabel.setStyle("-fx-text-fill: #ff6b6b;"); // Red text
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
                ShipController shipController = (ShipController) controller;
                shipController.enableCellClickHandler(this::selectComponentToRemove);
            } catch (ClassCastException e) {
                showError("Unable to get the ship controller");
            }
        } catch (IOException e) {
            showError("Error while loading the ship view: " + e.getMessage());
        }
    }

    @FXML
    private void selectComponentToRemove(int row, int col) {
        try {
            Pair<Integer, Integer> coordinates = new Pair<>(row, col);
            ClientGameModel.getInstance().getClient().removeComponentFromShip(username, coordinates);
            loadShipView();
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for coordinates");
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @Override
    public void onShipUpdated(ViewShip ship) {
        loadShipView();
    }

    @Override
    public void onLobbyUpdated(ViewLobby lobby) {
        //
    }

    @Override
    public void onErrorMessageReceived(String message) {
        //
    }

    @Override
    public void onComponentInHandUpdated(ViewComponent component) {
        //
    }

    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {
        //
    }

    @Override
    public void onBoardUpdated(ViewBoard board) {
        //
    }


    public void cleanup() {
        System.out.println("ValidationMenuController: Starting cleanup...");

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

                            try {
                                StackPane stackPane = (StackPane) shipPaneTyped;
                                stackPane.setAlignment(null);
                            } catch (Exception e) {
                                System.err.println("Error resetting StackPane alignment: " + e.getMessage());
                            }
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

        if (validationStatusLabel != null) {
            validationStatusLabel.setText("");
            validationStatusLabel.setStyle("");
        }

        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }

        username = null;
        ship = null;
        validationStatusLabel = null;
        shipPane = null;
        errorLabel = null;

        System.out.println("ValidationMenuController: Cleanup completed");
    }
}