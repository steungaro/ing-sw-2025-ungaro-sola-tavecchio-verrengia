package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.rmi.RemoteException;
import java.util.*;

public abstract class BuildingPhaseController implements GameModelListener, BindCleanUp {

    @FXML
    protected StackPane shipContainer;

    @FXML
    protected Pane componentInHandPane;

    @FXML
    private Pane coveredDeckPane;

    @FXML
    protected VBox uncoveredComponentsPane;

    @FXML
    protected ImageView bgImage;

    @FXML
    protected StackPane rootPane;

    @FXML
    protected GridPane componentsGrid;

    @FXML
    protected GridPane bookedGrid;

    @FXML
    public HBox rotateButtonsContainer;

    private ViewComponent selectedComponent;
    protected boolean placementModeActive = false;
    protected ViewShip ship;
    protected final Map<String, Integer> gridComponents = new HashMap<>();
    protected ShipController.CellClickHandler cellClickHandler;
    private ViewPlayer currentPlayerBeingViewed;

    /**
     * Initializes the building phase controller and sets up the user interface.
     * This method is called automatically by JavaFX after loading the FXML file.
     * It performs the following operations:
     * - Sets up the current player view based on the username
     * - Initializes the covered deck background
     * - Loads the ship view and uncovered components
     * - Registers event listeners for user interactions
     * - Updates the component in hand and booked components display
     */
    public void initialize() {
        String myUsername = ClientGameModel.getInstance().getUsername();
        this.currentPlayerBeingViewed = getPlayerFromModel(myUsername);

        covered();
        loadShip();
        loadUncoveredComponents();

        ClientGameModel.getInstance().addListener(this);

        coveredDeckPane.setOnMouseClicked(_ -> {
            if (isViewingOwnShip()) {
                takeCoveredComponent();
            } else {
                showError("You can only take covered components for your own ship.");
            }
        });

        componentInHandPane.setOnMouseClicked(_ -> {
            if (isViewingOwnShip()) {
                activatePlacementMode();
            } else {
                showError("Placement mode is only available for your own ship.");
            }
        });
        updateComponentInHand();
        updateBookedComponents();
    }

    private void covered(){
        if (coveredDeckPane != null) {
            try {
                Image deckBackgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/fxml/tiles/component.png")));
                if (deckBackgroundImage.isError()) {
                    coveredDeckPane.setStyle(coveredDeckPane.getStyle() + "; -fx-background-color: lightgrey;");
                } else {
                    BackgroundImage bgImage = new BackgroundImage(
                            deckBackgroundImage,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(1.0, 1.0, true, true, true, false)
                    );
                    coveredDeckPane.setBackground(new Background(bgImage));
                }
            } catch (Exception e) {
                showError("Could not load deck background image: " + e.getMessage());
                coveredDeckPane.setStyle(coveredDeckPane.getStyle() + "; -fx-background-color: lightgrey;");
            }
        }
    }

    private ViewPlayer getPlayerFromModel(String username) {
        ViewPlayer[] players = ClientGameModel.getInstance().getPlayers();
        if (players == null || username == null) return null;
        return Arrays.stream(players)
                .filter(p -> p != null && username.equals(p.username))
                .findFirst()
                .orElse(null);
    }

    private boolean isViewingOwnShip() {
        return currentPlayerBeingViewed != null &&
                currentPlayerBeingViewed.username.equals(ClientGameModel.getInstance().getUsername());
    }

    private void loadShip() {
        ship = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());
        buildShipComponents(ship);
    }

    /**
     * Builds and displays the ship components on the grid.
     * This method clears the current grid and rebuilds it with all components
     * from the provided ship at their correct positions.
     * 
     * @param ship the ship view containing the components to display
     */
    public void buildShipComponents(ViewShip ship) {
        if (ship == null || componentsGrid == null) return;

        clearAllComponents();

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                ViewComponent comp = ship.getComponent(row, col);
                if (comp != null) {
                    addComponent(comp, row, col);
                }
            }
        }
    }

    /**
     * Clears all components from the ship grid and resets it to empty state.
     * This method removes all component images and re-adds empty ImageViews
     * to maintain the grid structure.
     */
    public void clearAllComponents() {
        componentsGrid.getChildren().clear();

        // Re-add all empty ImageViews
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                ImageView cell = getImageViewAt(row, col);
                if (cell != null) {
                    cell.setImage(null);
                    GridPane.setFillWidth(cell, true);
                    GridPane.setFillHeight(cell, true);
                    componentsGrid.add(cell, col, row);
                }
            }
        }
        gridComponents.clear();
    }

    protected ImageView getImageViewAt(int row, int col) {
        return null;
    }

    /**
     * Adds a component to the ship grid at the specified position.
     * This method loads the component image, applies rotation if needed,
     * and places it in the grid with proper layout properties.
     * 
     * @param comp the component to add
     * @param row the row position (0-based)
     * @param col the column position (0-based)
     * @return true if the component was successfully added, false otherwise
     */
    public boolean addComponent(ViewComponent comp, int row, int col) {
        int componentId = comp.id;
        if (row < 0 || row >= getRows() || col < 0 || col >= getCols()) {
            return false;
        }

        String cellId = row + "_" + col;
        ImageView targetCell = getImageViewAt(row, col);

        if (targetCell == null) {
            return false;
        }

        // Remove the old cell and create a new layered pane
        GridPane parent = (GridPane) targetCell.getParent();
        parent.getChildren().remove(targetCell);
        StackPane layeredPane = new StackPane();

        String imagePath = "/fxml/tiles/" + componentId + ".jpg";
        try {
            Image componentImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            targetCell.setImage(componentImage);
            if (comp.rotComp >= 0 && comp.rotComp <= 3) {
                targetCell.setRotate(comp.rotComp * 90);
            }

            layeredPane.getChildren().add(targetCell);
            // Make sure the layered pane fills the cell
            layeredPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            GridPane.setFillWidth(layeredPane, true);
            GridPane.setFillHeight(layeredPane, true);
            GridPane.setHgrow(layeredPane, Priority.ALWAYS);
            GridPane.setVgrow(layeredPane, Priority.ALWAYS);

            parent.add(layeredPane, col, row);

            gridComponents.put(cellId, componentId);
            return true;
        } catch (Exception e) {
            System.err.println("Unable to load image: " + e.getMessage());
            return false;
        }
    }

    private void loadUncoveredComponents() {
        uncoveredComponentsPane.getChildren().clear();
        ClientGameModel client = ClientGameModel.getInstance();
        List<ViewComponent> uncoveredComponents;
        if (client.getBoard() != null && client.getBoard().viewedPile != null) {
            uncoveredComponents = client.getBoard().viewedPile;
            for (ViewComponent component : uncoveredComponents) {
                Pane componentPane = createComponentPane(component);
                // Take the index of the component in the list to use as a unique identifier
                int index = uncoveredComponents.indexOf(component);
                componentPane.setOnMouseClicked(_ -> selectComponent(component, componentPane, index));
                uncoveredComponentsPane.getChildren().add(componentPane);
            }
        }
    }

    private void takeCoveredComponent() {
        if (ClientGameModel.getInstance().getComponentInHand() != null) {
            showError("You already have a component in hand!");
            return;
        }
        ClientGameModel model = ClientGameModel.getInstance();
        String username = model.getUsername();
        try {
            model.getClient().takeComponentFromUnviewed(username, 0);
        } catch (RemoteException e) {
            showError("Error taking component from covered deck: " + e.getMessage());
            return;
        }
        updateComponentInHand();
    }

    protected void updateComponentInHand() {
        ViewComponent componentInHand = ClientGameModel.getInstance().getComponentInHand();
        componentInHandPane.getChildren().clear();

        if (componentInHand != null) {
            Pane componentPane = createComponentPane(componentInHand);
            componentPane.setTranslateX((componentInHandPane.getWidth() - componentPane.getPrefWidth()) / 2);
            componentPane.setTranslateY((componentInHandPane.getHeight() - componentPane.getPrefHeight()) / 2);

            componentPane.translateXProperty().bind(
                    componentInHandPane.widthProperty().subtract(componentPane.prefWidthProperty()).divide(2)
            );
            componentPane.translateYProperty().bind(
                    componentInHandPane.heightProperty().subtract(componentPane.prefHeightProperty()).divide(2)
            );

            componentInHandPane.getChildren().add(componentPane);
            selectedComponent = componentInHand;

            rotateButtonsContainer.setVisible(true);
            rotateButtonsContainer.setManaged(true);
        } else {
            if (rotateButtonsContainer != null) {
                rotateButtonsContainer.setVisible(false);
                rotateButtonsContainer.setManaged(false);
            }
            selectedComponent = null;
        }
    }

    private Pane createComponentPane(ViewComponent component) {
        Pane pane = new Pane();
        pane.setPrefSize(80, 80);

        if (component == null) {
            return pane;
        }

        String imageUrl = getComponentImageUrl(component);
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageUrl)));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(70);
            imageView.setFitHeight(70);
            imageView.setPreserveRatio(true);
            imageView.setX(5);
            imageView.setY(5);
            if (component.rotComp >= 0 && component.rotComp <= 3) {
                imageView.setRotate(component.rotComp * 90);
            }
            pane.getChildren().add(imageView);
        } catch (Exception e) {
            Rectangle fallback = new Rectangle(5, 5, 70, 70);
            pane.getChildren().add(fallback);
        }

        return pane;
    }

    private String getComponentImageUrl(ViewComponent component) {
        if (component == null) return null;

        return "/fxml/tiles/" + component.id + ".jpg";
    }

    /**
     * Shows an error message in a dialog window
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Stops ship assembly and finishes the building phase
     */
    @FXML
    private void stopAssembling() {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Stop Assembling Ship");
        dialog.setHeaderText("Choose your starting board position");
        dialog.setContentText("Enter board index (1-4):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(boardIndex -> {
            try {
                int index = Integer.parseInt(boardIndex);
                if (index >= 1 && index <= 4) {
                    ClientGameModel.getInstance().getClient().stopAssembling(
                            ClientGameModel.getInstance().getUsername(),
                            index
                    );
                } else {
                    showError("Invalid board index. Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        });
    }

    /**
     * Turns the hourglass
     */
    @FXML
    private void turnHourglass() {
        try {
            String username = ClientGameModel.getInstance().getUsername();
            ClientGameModel.getInstance().getClient().turnHourglass(username);
        } catch (Exception e) {
            showError("Error turning hourglass: " + e.getMessage());
        }
    }

    /**
     * Peeks at a deck of cards
     */
    @FXML
    private void peekDeck() {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Peek Deck");
        dialog.setHeaderText("Select a deck to peek");
        dialog.setContentText("Enter deck index (1, 2, 3):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(indexStr -> {
            try {
                int index = Integer.parseInt(indexStr);
                if (index > 0 && index <= 3) {
                    String username = ClientGameModel.getInstance().getUsername();
                    ClientGameModel.getInstance().getClient().peekDeck(username, index);
                } else {
                    showError("Invalid index. Please enter a number between 1 and 3.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        });
    }

    /**
     * Books the component currently in hand
     */
    @FXML
    private void bookComponent() {
        if (selectedComponent == null) {
            showError("No component selected!");
            return;
        }

        try {
            String username = ClientGameModel.getInstance().getUsername();
            ClientGameModel.getInstance().getClient().addComponentToBooked(username);
            selectedComponent = null;
            componentInHandPane.getChildren().clear();
        } catch (Exception e) {
            showError("Error booking component: " + e.getMessage());
        }
    }

    private void selectComponent(ViewComponent ignoredComponent, Pane sourcePane, int index) {
        if (ClientGameModel.getInstance().getComponentInHand() != null) {
            return;
        }
        try{
            ClientGameModel.getInstance().getClient().takeComponentFromViewed(ClientGameModel.getInstance().getUsername(), index);
        } catch (RemoteException e) {
            showError("Error selecting component: " + e.getMessage());
            return;
        }
        uncoveredComponentsPane.getChildren().forEach(node -> node.setStyle(node.getStyle().replace("-fx-border-width: 3;", "")));
        sourcePane.setStyle(sourcePane.getStyle() + "-fx-border-width: 3;");
    }

    protected void activatePlacementMode() {
        if (ClientGameModel.getInstance().getComponentInHand() != null) {
            placementModeActive = true;
            componentInHandPane.setStyle("-fx-border-color: green; -fx-border-width: 2;");
            uncoveredComponentsPane.setStyle("-fx-border-color: green; -fx-border-width: 2;");

            enableGridInteraction(this::handleCellClick);
            enableUncoveredComponentsInteraction(this::handleUncoveredClick);
        } else {
            System.err.println("No component in hand to place");
        }
    }

    protected void deactivatePlacementMode(){
        disableGridInteraction();
        disableUncoveredComponentsInteraction();
    }

    /**
     * Enables interaction with uncovered components pile.
     * This method sets up mouse click handlers and visual feedback
     * for the uncovered components pane.
     * 
     * @param handler the runnable to execute when the uncovered components are clicked
     */
    public void enableUncoveredComponentsInteraction(Runnable handler) {
        if (uncoveredComponentsPane != null) {
            uncoveredComponentsPane.setOnMouseClicked(_ -> {
                if (handler != null) {
                    handler.run();
                }
            });
            uncoveredComponentsPane.setCursor(Cursor.HAND);
        } else {
            System.err.println("uncoveredComponentsPane is null. Cannot enable interaction.");
        }
    }

    /**
     * Enables interaction with the ship grid for component placement.
     * This method adds clickable areas to valid grid positions and sets up
     * visual feedback for component placement operations.
     * 
     * @param handler the cell click handler to process grid interactions
     */
    public void enableGridInteraction(ShipController.CellClickHandler handler) {
        this.cellClickHandler = handler;

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                if(!checkIsValid(row,col))
                    continue;

                final int finalRow = row;
                final int finalCol = col;

                Rectangle clickArea = new Rectangle();
                clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                clickArea.setStroke(javafx.scene.paint.Color.LIGHTGREEN);
                clickArea.setStrokeWidth(2);
                clickArea.setOpacity(0.7);

                // Bind size to grid cell
                clickArea.widthProperty().bind(
                        componentsGrid.widthProperty().divide(getCols()).subtract(getCols())
                );
                clickArea.heightProperty().bind(
                        componentsGrid.heightProperty().divide(getRows()).subtract(getRows())
                );

                clickArea.setOnMouseClicked(_ -> {
                    if (cellClickHandler != null) {
                        cellClickHandler.onCellClicked(finalRow, finalCol);
                    }
                });

                clickArea.setOnMouseEntered(_ -> {
                    clickArea.setFill(javafx.scene.paint.Color.color(0, 1, 0, 0.2));
                    clickArea.setCursor(javafx.scene.Cursor.HAND);
                });

                clickArea.setOnMouseExited(_ -> {
                    clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                    clickArea.setCursor(javafx.scene.Cursor.DEFAULT);
                });

                componentsGrid.add(clickArea, col, row);
                GridPane.setHalignment(clickArea, javafx.geometry.HPos.CENTER);
                GridPane.setValignment(clickArea, javafx.geometry.VPos.CENTER);
            }
        }
    }

    protected abstract boolean checkIsValid(int row,int col);

    protected void handleCellClick(int row, int col) {
        if (placementModeActive && ClientGameModel.getInstance().getComponentInHand() != null) {
            try {
                String username = ClientGameModel.getInstance().getUsername();
                ClientGameModel.getInstance().getClient().placeComponent(username, new org.javatuples.Pair<>(row, col));

                placementModeActive = false;
                componentInHandPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");

                deactivatePlacementMode();

                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                        javafx.application.Platform.runLater(() -> {
                            updateComponentInHand();
                                buildShipComponents(ClientGameModel.getInstance().getShip(username));
                        });
                    } catch (InterruptedException e) {
                        showError("Error placing component: " + e.getMessage());
                    }
                }).start();
            } catch (Exception e) {
                placementModeActive = false;
                componentInHandPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");
                showError("Error during placement: " + e.getMessage());
            }
        }
    }

    protected void handleUncoveredClick() {
        if (placementModeActive && ClientGameModel.getInstance().getComponentInHand() != null) {
            try {
                String username = ClientGameModel.getInstance().getUsername();
                if (username == null) {
                    showError("User not identified. Cannot add component to viewed pile.");
                    return;
                }
                ClientGameModel.getInstance().getClient().addComponentToViewed(username);

                placementModeActive = false;
                componentInHandPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");

                deactivatePlacementMode();

                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        Platform.runLater(() -> {
                            updateComponentInHand();
                            loadUncoveredComponents();
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        Platform.runLater(() -> showError("UI update interrupted after adding to viewed pile."));
                    }
                }).start();

            } catch (Exception e) {
                placementModeActive = false;
                componentInHandPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");
                deactivatePlacementMode();
                showError("Error adding component to viewed pile: " + e.getMessage());
            }
        } else {
            if (placementModeActive && ClientGameModel.getInstance().getComponentInHand() == null) {
                Platform.runLater(() -> showError("No component in hand to add to viewed pile."));
            }
        }
    }

    /**
     * Disables interaction with the uncovered components pile.
     * This method removes mouse click handlers and resets the cursor
     * and visual styling to default state.
     */
    public void disableUncoveredComponentsInteraction() {
        if (uncoveredComponentsPane != null) {
            uncoveredComponentsPane.setOnMouseClicked(null);
            uncoveredComponentsPane.setCursor(Cursor.DEFAULT);
            uncoveredComponentsPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");
        }
    }

    /**
     * Disables interaction with the ship grid.
     * This method removes all clickable areas from the grid and
     * clears the cell click handler.
     */
    public void disableGridInteraction() {
        this.cellClickHandler = null;

        componentsGrid.getChildren().removeIf(node ->
                node.getClass().equals(javafx.scene.shape.Rectangle.class));
    }

    protected abstract int getRows();
    protected abstract int getCols();

    @FXML
    private void rotateComponentClockwise() {
        try {
            String username = ClientGameModel.getInstance().getUsername();
            ClientGameModel.getInstance().getClient().rotateComponentClockwise(username);
        } catch (Exception e) {
            showError("Error rotating component clockwise: " + e.getMessage());
        }
    }

    @FXML
    private void rotateComponentCounterclockwise() {
        try {
            String username = ClientGameModel.getInstance().getUsername();
            ClientGameModel.getInstance().getClient().rotateComponentCounterclockwise(username);
        } catch (Exception e) {
            showError("Error rotating component counterclockwise: " + e.getMessage());
        }
    }

    protected void handleBookedClick(int index) {
        String username = ClientGameModel.getInstance().getUsername();
        try {
            ClientGameModel.getInstance().getClient().addComponentToBooked(username);
        } catch (RemoteException e) {
            System.err.println("Error taking component from booked: " + index + e.getMessage());
        }
    }

    protected void disableBookedComponentsInteraction(){
        if (bookedGrid != null) {
            java.util.List<javafx.scene.Node> nodesToRemove = new java.util.ArrayList<>();

            for (javafx.scene.Node node : bookedGrid.getChildren()) {
                try{
                    Rectangle rect = (Rectangle) node;
                    nodesToRemove.add(rect);
                } catch (ClassCastException e) {
                    // Ignore nodes that are not rectangles
                }
            }
            bookedGrid.getChildren().removeAll(nodesToRemove);
            bookedGrid.setOnMouseClicked(null);
        }
    }

    protected abstract void updateBookedComponents();

    /**
     * Handles ship update events from the game model.
     * This method rebuilds the ship components display when the ship is updated.
     * 
     * @param ship the updated ship view object
     */
    @Override
    public void onShipUpdated(ViewShip ship) {
        if (this.ship.equals(ship)) {
            buildShipComponents(this.ship);
        }
    }

    /**
     * Handles component in hand update events from the game model.
     * This method updates the component in hand display, uncovered components,
     * and booked components when the component in hand changes.
     * 
     * @param component the updated component in hand
     */
    @Override
    public void onComponentInHandUpdated(ViewComponent component) {
        updateComponentInHand();
        loadUncoveredComponents();
        updateBookedComponents();
    }

    /**
     * Handles lobby update events from the game model.
     * This implementation ignores lobby updates as they are not relevant for the building phase.
     * 
     * @param lobby the updated lobby view object
     */
    @Override
    public void onLobbyUpdated(ViewLobby lobby) {
        // Ignore - not relevant for building phase
    }

    /**
     * Handles error message events from the game model.
     * This implementation ignores error messages as they are handled elsewhere in the UI.
     * 
     * @param message the error message received from the game model
     */
    @Override
    public void onErrorMessageReceived(String message) {
        // Ignore - error messages handled elsewhere
    }

    /**
     * Handles current adventure card update events from the game model.
     * This implementation ignores adventure card updates as they are not relevant for the building phase.
     * 
     * @param currentCard the updated current adventure card
     */
    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {
        // ignore - not relevant for building phase
    }

    /**
     * Handles board update events from the game model.
     * This implementation ignores board updates as they are not relevant for the building phase.
     * 
     * @param board the updated board view object
     */
    @Override
    public void onBoardUpdated(ViewBoard board) {
        // ignore - not relevant for building phase
    }
}