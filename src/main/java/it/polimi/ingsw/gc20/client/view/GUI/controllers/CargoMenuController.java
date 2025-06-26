
package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

public class CargoMenuController implements MenuController.ContextDataReceiver, GameModelListener {
    @FXML private Label messageLabel;
    @FXML private Label errorLabel;
    @FXML private Pane shipPane;
    @FXML private VBox cargoBoxPane;
    @FXML private VBox cargoContainer;
    private String username;
    private ViewShip ship;
    private ShipController shipController;
    private int losing; // 0 aren't initialized, 1 losing cargo, 2 gaining cargo
    private Pair<Integer, Integer> from;
    private CargoColor colorFrom;
    private List<CargoColor> cargoToGain;
    private int cargoToLose;
    private CargoColor currentCargo;

    @FXML
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
        ship = ClientGameModel.getInstance().getShip(username);

        loadShipView();
    }

    public void initializeWithParameters(String message) {
        messageLabel.setText(message);

        if(losing==1){
            cargoBoxPane.setVisible(false);
            for (int i = 0; i < cargoToLose; i++) {
                shipController.enableCellClickHandler(this::handleUnloadCargo);
            }
        } else if(losing==2){
            createCargoBoxes();
        } else {
            showError("Invalid operation, please try again.");
        }
    }

    private void createCargoBoxes() {
        cargoContainer.getChildren().clear();
        cargoBoxPane.setVisible(true);

        List<CargoColor> availableCargo = new ArrayList<>(cargoToGain);

        for (CargoColor cargo : availableCargo) {
            Rectangle box = new Rectangle(40, 40);
            box.setFill(getColorFromCargoColor(cargo));
            box.setStroke(Color.WHITE);
            box.setStrokeWidth(2);

            VBox.setMargin(box, new javafx.geometry.Insets(2, 2, 2, 2));

            box.setOnMouseEntered(e -> box.setStroke(Color.YELLOW));
            box.setOnMouseExited(e -> box.setStroke(Color.WHITE));

            box.setOnMouseClicked(e -> {
                currentCargo = cargo;
                shipController.enableCellClickHandler(this::handleLoadCargo);

                cargoContainer.getChildren().forEach(node -> {
                    if (node instanceof Rectangle) {
                        ((Rectangle) node).setStroke(Color.WHITE);
                    }
                });
                box.setStroke(Color.LIME);
            });

            cargoContainer.getChildren().add(box);
        }
    }

    private Color getColorFromCargoColor(CargoColor cargoColor) {
        return switch (cargoColor) {
            case RED -> Color.RED;
            case YELLOW -> Color.YELLOW;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
            default -> Color.GRAY;
        };
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

    private void handleUnloadCargo(int row, int col) {
        Pair<Integer, Integer> coords = new Pair<>(row, col);
        CargoColor color = showColorSelectionDialog();

        if (color == null) {
            return;
        }

        try {
            ClientGameModel.getInstance().getClient().unloadCargo(username, color, coords);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    public static CargoColor showColorSelectionDialog() {
        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setTitle("Cargo Selection");
        dialog.setHeaderText("Select Cargo Color");
        dialog.setContentText("Choose one of the available colors:");

        ButtonType redButton = new ButtonType("Red", ButtonBar.ButtonData.OK_DONE);
        ButtonType yellowButton = new ButtonType("Yellow", ButtonBar.ButtonData.OK_DONE);
        ButtonType blueButton = new ButtonType("Blue", ButtonBar.ButtonData.OK_DONE);
        ButtonType greenButton = new ButtonType("Green", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(redButton, yellowButton, blueButton, greenButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (result.get() == redButton) {
                return CargoColor.RED;
            } else if (result.get() == yellowButton) {
                return CargoColor.YELLOW;
            } else if (result.get() == blueButton) {
                return CargoColor.BLUE;
            } else if (result.get() == greenButton) {
                return CargoColor.GREEN;
            }
        }
        return null;
    }

    private void handleMoveCargoFrom(int row, int col) {
        from = new Pair<>(row, col);
        colorFrom = showColorSelectionDialog();

        shipController.enableCellClickHandler(this::handleMoveCargoTo);
    }

    private void handleMoveCargoTo(int row, int col) {
        Pair<Integer, Integer> to = new Pair<>(row, col);

        try {
            ClientGameModel.getInstance().getClient().moveCargo(username, colorFrom, from, to);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    private void handleLoadCargo(int row, int col) {
        Pair<Integer, Integer> coords = new Pair<>(row, col);
        try {
            ClientGameModel.getInstance().getClient().loadCargo(username, currentCargo, coords);

            removeUsedCargo(currentCargo);

        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    private void removeUsedCargo(CargoColor usedCargo) {
        cargoToGain.remove(usedCargo);

        cargoContainer.getChildren().removeIf(node -> {
            try{
                Rectangle rect = (Rectangle) node;
                Color rectColor = (Color) rect.getFill();
                Color cargoColor = getColorFromCargoColor(usedCargo);
                return rectColor.equals(cargoColor);
            }
             catch (ClassCastException e) {
                // Ignore nodes that are not rectangles
            }
            return false;
        });

        if (cargoToGain.isEmpty()) {
            cargoBoxPane.setVisible(false);
        }

        currentCargo = null;
    }

    @FXML
    private void moveCargo() {
        shipController.enableCellClickHandler(this::handleMoveCargoFrom);
    }

    @FXML
    private void handleEndTurn() {
        try {
            ClientGameModel.getInstance().getClient().endMove(username);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());}
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @SuppressWarnings( "unchecked")
    @Override
    public void setContextData(Map<String, Object> contextData) {
        if(contextData.size()==1) {
            losing = 1;
            cargoToGain = null;
            if (contextData.containsKey("cargoNum")) {
                cargoToLose = (int) contextData.get("cargoNum");
                if (cargoToLose <= 0) {
                    showError("No cargo available to load/unload");
                    return;
                }
                String message = "Select cargo to remove: " + cargoToLose;
                initializeWithParameters(message);
            } else {
                showError("No cargo available to load/unload");
            }
        } else if (contextData.size()==4){
            if(contextData.containsKey("message") && contextData.containsKey("cargoToLose") && contextData.containsKey("cargoToGain") && contextData.containsKey("losing")) {
                losing = (boolean) contextData.get("losing") ? 1 : 2;
                String message = (String) contextData.get("message");
                cargoToGain = new ArrayList<>((List<CargoColor>) contextData.get("cargoToGain"));
                cargoToLose = (int) contextData.get("cargoToLose");

                initializeWithParameters(message);
            }
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
}