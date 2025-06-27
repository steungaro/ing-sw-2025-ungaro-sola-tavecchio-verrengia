package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
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

public class CargoMenuController implements MenuController.ContextDataReceiver, GameModelListener, BindCleanUp {
    @FXML public Button unloadButton;
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

    /**
     * Initializes the cargo menu controller and sets up the basic components.
     * This method is called automatically by JavaFX after loading the FXML file.
     * It performs the following operations:
     * - Retrieves the current username from the game model
     * - Gets the ship associated with the current player
     * - Loads and displays the ship view in the interface
     */
    @FXML
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
        ship = ClientGameModel.getInstance().getShip(username);

        loadShipView();
    }

    /**
     * Initializes the cargo menu with specific parameters and sets up the appropriate mode.
     * This method configures the interface based on the current operation mode:
     * - For cargo losing mode (losing=1): hides cargo selection UI and enables unload functionality
     * - For cargo gaining mode (losing=2): shows cargo selection boxes for loading
     * - For invalid states: displays an error message
     * 
     * @param message the message to display to the user explaining the current operation
     */
    public void initializeWithParameters(String message) {
        messageLabel.setText(message);

        if(losing==1){
            cargoBoxPane.setVisible(false);
            unloadButton.setVisible(false);
            shipController.enableCellClickHandler(this::handleUnloadCargo);

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

            box.setOnMouseEntered(_ -> box.setStroke(Color.YELLOW));
            box.setOnMouseExited(_ -> box.setStroke(Color.WHITE));

            box.setOnMouseClicked(_ -> {
                currentCargo = cargo;
                unloadButton.setVisible(true);
                shipController.enableCellClickHandler(this::handleLoadCargo);

                cargoContainer.getChildren().forEach(node -> {
                    try{
                        ((Rectangle) node).setStroke(Color.WHITE);
                    } catch (ClassCastException e) {
                        // Ignore if the node is not a Rectangle
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

    private void handleUnloadCargo(int row, int col) {
        Pair<Integer, Integer> coords = new Pair<>(row, col);
        CargoColor color = showColorSelectionDialog();

        if (color == null) {
            return;
        }

        try {
            ClientGameModel.getInstance().getClient().unloadCargo(username, color, coords);
            unloadButton.setVisible(false);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
        shipController.enableCellClickHandler(this::handleUnloadCargo);
    }

    /**
     * Displays a color selection dialog for cargo operations.
     * This static method creates a modal dialog with buttons for each cargo color
     * (Red, Yellow, Blue, Green) and a Cancel option. The user can select one
     * color or cancel the operation.
     * 
     * @return the selected CargoColor, or null if the operation was cancelled
     */
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
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
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

    /**
     * Sets the context data for the cargo menu operations.
     * This method processes different types of cargo operations based on the provided context:
     * - Single parameter (cargoNum): Sets up cargo removal mode
     * - Four parameters (message, cargoToLose, cargoToGain, losing): Sets up complex cargo operations
     * 
     * The method determines the operation mode and initializes the interface accordingly.
     * 
     * @param contextData a map containing operation parameters such as cargo numbers, 
     *                   messages, and operation type flags
     */
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

    /**
     * Handles ship update events from the game model.
     * This method reloads the ship view when the ship state is updated,
     * ensuring the display reflects the current ship configuration.
     * 
     * @param ship the updated ship view object
     */
    @Override
    public void onShipUpdated(ViewShip ship) {
        loadShipView();
    }

    /**
     * Handles lobby update events from the game model.
     * This implementation provides no functionality as lobby updates are not relevant for cargo operations.
     * 
     * @param lobby the updated lobby view object
     */
    @Override
    public void onLobbyUpdated(ViewLobby lobby) {

    }

    /**
     * Handles error message events from the game model.
     * This implementation provides no functionality as error messages are handled locally.
     * 
     * @param message the error message received from the game model
     */
    @Override
    public void onErrorMessageReceived(String message) {

    }

    /**
     * Handles component in hand update events from the game model.
     * This implementation provides no functionality as component updates are not relevant for cargo operations.
     * 
     * @param component the updated component in the player's hand
     */
    @Override
    public void onComponentInHandUpdated(ViewComponent component) {

    }

    /**
     * Handles current adventure card update events from the game model.
     * This implementation provides no functionality as adventure card updates are not relevant for cargo operations.
     * 
     * @param currentCard the updated current adventure card
     */
    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {

    }

    /**
     * Handles board update events from the game model.
     * This implementation provides no functionality as board updates are not relevant for cargo operations.
     * 
     * @param board the updated board view object
     */
    @Override
    public void onBoardUpdated(ViewBoard board) {

    }

    /**
     * Handles the unload cargo button click event.
     * This FXML event handler enables the cell click handler for cargo unloading operations,
     * allowing the user to select a ship cell to unload cargo from.
     */
    @FXML
    public void handleUnload() {
        shipController.enableCellClickHandler(this::handleUnloadCargo);
    }

    /**
     * Performs comprehensive cleanup operations for the cargo menu controller.
     * This method handles the following cleanup tasks:
     * - Removes the controller as a listener from the game model
     * - Cleans up and nullifies the ship controller
     * - Clears ship pane contents and unbinds properties
     * - Removes event handlers from cargo container rectangles
     * - Resets all UI components to their default states
     * - Nullifies all object references and resets primitive values
     * - Provides error handling for cleanup operations
     */
    public void cleanup() {
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

        if (cargoContainer != null) {
            for (javafx.scene.Node node : cargoContainer.getChildren()) {
                try {
                    Rectangle rect = (Rectangle) node;
                    rect.setOnMouseEntered(null);
                    rect.setOnMouseExited(null);
                    rect.setOnMouseClicked(null);
                } catch (Exception e) {
                    System.err.println("Error cleaning up cargo rectangle: " + e.getMessage());
                }
            }
            cargoContainer.getChildren().clear();
        }

        if (messageLabel != null) {
            messageLabel.setText("");
        }

        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }

        if (cargoBoxPane != null) {
            cargoBoxPane.setVisible(false);
        }

        username = null;
        ship = null;
        from = null;
        colorFrom = null;
        currentCargo = null;

        if (cargoToGain != null) {
            cargoToGain.clear();
            cargoToGain = null;
        }

        losing = 0;
        cargoToLose = 0;

        messageLabel = null;
        errorLabel = null;
        shipPane = null;
        cargoBoxPane = null;
        cargoContainer = null;
    }
}