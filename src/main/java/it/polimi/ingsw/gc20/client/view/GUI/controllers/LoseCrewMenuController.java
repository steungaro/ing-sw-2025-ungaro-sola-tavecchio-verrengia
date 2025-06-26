package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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

    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
    }

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

            shipPaneTyped.setMaxWidth(Region.USE_COMPUTED_SIZE);
            shipPaneTyped.setMaxHeight(Region.USE_COMPUTED_SIZE);

            shipPaneTyped.prefWidthProperty().bind(shipPane.widthProperty());
            shipPaneTyped.prefHeightProperty().bind(shipPane.heightProperty());

            shipController = (ShipController) loader.getController();
        } catch (IOException e) {
            showError("Error uploading ship: " + e.getMessage());
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

    @Override
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("crewNum")) {
            int crewToLose = (int) contextData.get("crewNum");
            initializeWithCrewToLose(crewToLose);
        } else {
            showError("No crew to lose data provided.");
        }
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
            if (shipPane.getChildren().size() > 0) {
                Parent shipView = (Parent) shipPane.getChildren().get(0);
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

        if (cabins != null) {
            cabins.clear();
        }

        username = null;
        ship = null;
        crewToLoseLabel = null;
        errorLabel = null;
        shipPane = null;

        System.out.println("LoseCrewMenuController: Cleanup completed");
    }
}