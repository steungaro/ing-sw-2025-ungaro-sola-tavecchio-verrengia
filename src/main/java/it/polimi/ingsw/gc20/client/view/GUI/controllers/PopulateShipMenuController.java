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

public class PopulateShipMenuController implements GameModelListener, BindCleanUp {
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

    @Override
    public void onShipUpdated(ViewShip ship) {
        loadShipView();
    }

    @Override
    public void onLobbyUpdated(ViewLobby lobby) {

    }

    @Override
    public void onErrorMessageReceived(String message) {

    }

    @Override
    public void onComponentInHandUpdated(ViewComponent component) {

    }

    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {

    }

    @Override
    public void onBoardUpdated(ViewBoard board) {

    }

    public void cleanup() {
        System.out.println("PopulateShipMenuController: Starting cleanup...");

        ClientGameModel gameModel = ClientGameModel.getInstance();
        if (gameModel != null) {
            gameModel.removeListener(this);
        }

        if (shipPane != null) {
            if (shipPane.getChildren().size() > 0) {
                try {
                    Parent shipView = (Parent) shipPane.getChildren().get(0);
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

        System.out.println("PopulateShipMenuController: Cleanup completed");
    }
}