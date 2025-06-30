package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.javatuples.Pair;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract controller class for managing ship components in the game interface.
 * Handles the display and interaction with ship components on the grid.
 * Implements GameModelListener to respond to game state changes.
 */
public abstract class ShipController implements GameModelListener, BindCleanUp {
    public String playerUsername;
    protected ViewShip ship;

    @FXML protected Label playerColorLabel;
    @FXML protected Label usernameLabel;
    @FXML protected Label creditsLabel;
    @FXML protected Label inGameLabel;
    @FXML protected StackPane rootPane;

    private final Map<String, Rectangle> cellClickAreas = new HashMap<>();


    @FXML protected GridPane componentsGrid;

    protected final Map<String, Integer> gridComponents = new HashMap<>();
    protected boolean shouldLoadComponentStats = true;

    /**
     * Sets whether component statistics should be loaded and displayed.
     * When true, additional component information like battery levels or alien presence is shown.
     *
     * @param shouldLoad True to display component statistics, false to hide them
     */
    public void setShouldLoadComponentStats(boolean shouldLoad) {
        this.shouldLoadComponentStats = shouldLoad;
    }


    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the player information, loads the ship model, and configures the layout.
     */
    @FXML
    protected void initialize() {
        playerUsername = ClientGameModel.getInstance().getUsername();
        ship = ClientGameModel.getInstance().getShip(playerUsername);
        buildShipComponents(ship);

        if (rootPane != null) {
            rootPane.widthProperty().addListener((_, _, _) -> {
                if (rootPane != null) {
                    rootPane.requestLayout();
                }

            });

            rootPane.heightProperty().addListener((_, _, _) -> {
                if (rootPane != null) {
                    rootPane.requestLayout();
                }

            });
        }

    }

    /**
     * Reloads the ship data from the client game model and rebuilds the ship components.
     * Used when the ship model is updated and the view needs to be refreshed.
     */
    public void reloadShip() {
        ship = ClientGameModel.getInstance().getShip(playerUsername);
        buildShipComponents(ship);
    }


    /**
     * Adds a component to the ship grid at the specified position.
     *
     * @param comp The component to add
     * @param row The row position on the grid
     * @param col The column position on the grid
     * @return True if the component was successfully added, false otherwise
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

        GridPane parent = (GridPane) targetCell.getParent();
        parent.getChildren().remove(targetCell);
        StackPane layeredPane = new StackPane();

        String imagePath = "/fxml/tiles/" + componentId + ".jpg";

        Image componentImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        targetCell.setImage(componentImage);
        if(!shouldLoadComponentStats)
            targetCell.setRotate(comp.rotComp * 90);
        if(!loadCompInfo(targetCell, layeredPane, row, col, componentId, comp, parent, cellId))
            return false;

        gridComponents.put(cellId, componentId);
        return true;
    }

    private boolean loadCompInfo(ImageView targetCell, StackPane layeredPane, int row, int col, int ignoredComponentId, ViewComponent comp, GridPane parent, String ignoredCellId) {
        if (!shouldLoadComponentStats) {
            layeredPane.getChildren().add(targetCell);
            layeredPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            GridPane.setFillWidth(layeredPane, true);
            GridPane.setFillHeight(layeredPane, true);
            GridPane.setHgrow(layeredPane, Priority.ALWAYS);
            GridPane.setVgrow(layeredPane, Priority.ALWAYS);
            parent.add(layeredPane, col, row);
            return true;
        }

        targetCell.setRotate(comp.rotComp * 90);


        try {
            layeredPane.getChildren().add(targetCell);
            if (comp.isCabin()) {
                setComponentProp(layeredPane, (ViewCabin) comp);
            } else if (comp.isBattery()) {
                setComponentProp(layeredPane, (ViewBattery) comp);
            } else if (comp.isCargoHold()) {
                setComponentProp(layeredPane, (ViewCargoHold) comp);
            }

            layeredPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            GridPane.setFillWidth(layeredPane, true);
            GridPane.setFillHeight(layeredPane, true);
            GridPane.setHgrow(layeredPane, Priority.ALWAYS);
            GridPane.setVgrow(layeredPane, Priority.ALWAYS);

            parent.add(layeredPane, col, row);
            return true;
        } catch (Exception e) {
            System.err.println("Error uploading the image: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sets the properties of the component in the layered pane.
     * This method is used to display additional information like battery level, alien presence, or cargo hold contents.
     *
     * @param layeredPane The StackPane where the component will be displayed.
     * @param comp        The ViewComponent to set properties for.
     */
    public void setComponentProp(StackPane layeredPane, ViewBattery comp) {
        Label batteryLabel = new Label(Integer.toString(comp.availableEnergy));
        batteryLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.7); -fx-padding: 2px; -fx-background-radius: 3px;");

        try {
            ImageView batteryIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/fxml/icons/battery.png"))));
            batteryIcon.fitWidthProperty().bind(layeredPane.widthProperty().multiply(0.3));
            batteryIcon.fitHeightProperty().bind(layeredPane.heightProperty().multiply(0.3));
            batteryIcon.setPreserveRatio(true);

            StackPane iconBackground = new StackPane();
            iconBackground.getChildren().add(batteryIcon);

            javafx.scene.layout.HBox batteryContainer = new javafx.scene.layout.HBox(3);
            batteryContainer.setAlignment(javafx.geometry.Pos.CENTER);
            batteryContainer.getChildren().addAll(iconBackground, batteryLabel);

            StackPane.setAlignment(batteryContainer, javafx.geometry.Pos.CENTER);
            layeredPane.getChildren().add(batteryContainer);

        } catch (Exception e) {
            System.err.println("Unable to load battery image: " + e.getMessage());
            StackPane.setAlignment(batteryLabel, javafx.geometry.Pos.CENTER);
            layeredPane.getChildren().add(batteryLabel);
        }
    }

    /**
     * Sets the properties for a cabin component.
     * Displays aliens, astronauts, and cabin color information.
     *
     * @param layeredPane The StackPane where the component will be displayed
     * @param comp The cabin component to display
     */
    public void setComponentProp(StackPane layeredPane, ViewCabin comp) {
        if (comp.alien) {
            String alienImagePath = comp.alienColor == AlienColor.PURPLE ?
                    "/fxml/icons/purple_alien.png" : "/fxml/icons/brown_alien.png";

            try {
                ImageView alienIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(alienImagePath))));
                alienIcon.fitWidthProperty().bind(layeredPane.widthProperty().multiply(0.4));
                alienIcon.fitHeightProperty().bind(layeredPane.heightProperty().multiply(0.4));
                alienIcon.setPreserveRatio(true);

                layeredPane.getChildren().add(alienIcon);
                StackPane.setAlignment(alienIcon, Pos.CENTER);
            } catch (Exception e) {
                System.err.println("Unable to load alien image: " + e.getMessage());
            }
        } else if (comp.astronauts > 0) {
            Label astronautsLabel = new Label(Integer.toString(comp.astronauts));
            astronautsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.7); -fx-padding: 2px;");

            try {
                ImageView astronautIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/fxml/icons/astr.png"))));
                astronautIcon.fitWidthProperty().bind(layeredPane.widthProperty().multiply(0.3));
                astronautIcon.fitHeightProperty().bind(layeredPane.heightProperty().multiply(0.3));
                astronautIcon.setPreserveRatio(true);

                javafx.scene.layout.HBox astronautContainer = new javafx.scene.layout.HBox(5);
                astronautContainer.setAlignment(Pos.CENTER);
                astronautContainer.getChildren().addAll(astronautIcon, astronautsLabel);

                StackPane.setAlignment(astronautContainer, Pos.CENTER);
                layeredPane.getChildren().add(astronautContainer);

            } catch (Exception e) {
                System.err.println("Unable to load astronaut image: " + e.getMessage());
                StackPane.setAlignment(astronautsLabel, Pos.CENTER);
                layeredPane.getChildren().add(astronautsLabel);
            }
        }

        if (comp.cabinColor != AlienColor.NONE) {
            String colorStr = switch (comp.cabinColor) {
                case PURPLE -> "purple";
                case BROWN -> "brown";
                case BOTH -> "linear-gradient(45deg, purple 50%, brown 50%)";
                default -> null;
            };

            if (colorStr != null) {
                Label colorIndicator = new Label("");
                colorIndicator.setStyle("-fx-background: " + colorStr +
                        "; -fx-min-width: 15px; -fx-min-height: 15px; -fx-border-color: white; -fx-border-width: 1px;");
                StackPane.setAlignment(colorIndicator, Pos.BOTTOM_RIGHT);
                StackPane.setMargin(colorIndicator, new Insets(0, 5, 5, 0));
                layeredPane.getChildren().add(colorIndicator);
            }
        }
    }

    /**
     * Sets the properties for a cargo hold component.
     * Displays cargo slots with different colors based on their content.
     *
     * @param layeredPane The StackPane where the component will be displayed
     * @param comp The cargo hold component to display
     */
    public void setComponentProp(StackPane layeredPane, ViewCargoHold comp) {
        List<double[]> coordinates = getDoubles(comp);

        int index = 0;
    
    for (int i = 0; i < comp.red && index < coordinates.size(); i++, index++) {
        addCargoBox(layeredPane, coordinates.get(index), "red", comp.rotComp);
    }
    for (int i = 0; i < comp.green && index < coordinates.size(); i++, index++) {
        addCargoBox(layeredPane, coordinates.get(index), "green", comp.rotComp);
    }
    for (int i = 0; i < comp.blue && index < coordinates.size(); i++, index++) {
        addCargoBox(layeredPane, coordinates.get(index), "blue", comp.rotComp);
    }
    for (int i = 0; i < comp.yellow && index < coordinates.size(); i++, index++) {
        addCargoBox(layeredPane, coordinates.get(index), "yellow", comp.rotComp);
    }
    for (int i = 0; i < comp.free && index < coordinates.size(); i++, index++) {
        addCargoBox(layeredPane, coordinates.get(index), "empty", comp.rotComp);
    }

    }

    private static List<double[]> getDoubles(ViewCargoHold comp) {
        List<double[]> coordinates;

        if (comp.getSize() == 1) {
            coordinates = List.of(
                new double[]{0.5, 0.5}
            );
        } else if (comp.getSize() == 2) {
            coordinates = List.of(
                new double[]{0.5, 0.5 - 0.2},
                new double[]{0.5, 0.5 + 0.2}
            );
        } else {
            coordinates = List.of(
                new double[]{0.5 - 0.2, 0.5},
                new double[]{0.5 + 0.2, 0.5 - 0.2},
                new double[]{0.5 + 0.2, 0.5 + 0.2}
            );
        }
        return coordinates;
    }

    private void addCargoBox(StackPane parent, double[] relativePos, String type, int rot) {
    Rectangle box = new Rectangle();

    box.widthProperty().bind(parent.widthProperty().multiply(0.15));
    box.heightProperty().bind(parent.heightProperty().multiply(0.15));

    switch (type) {
        case "red" -> {
            box.setFill(javafx.scene.paint.Color.RED);
            box.setStroke(javafx.scene.paint.Color.DARKRED);
        }
        case "green" -> {
            box.setFill(javafx.scene.paint.Color.LIME);
            box.setStroke(javafx.scene.paint.Color.DARKGREEN);
        }
        case "blue" -> {
            box.setFill(javafx.scene.paint.Color.BLUE);
            box.setStroke(javafx.scene.paint.Color.DARKBLUE);
        }
        case "yellow" -> {
            box.setFill(javafx.scene.paint.Color.YELLOW);
            box.setStroke(javafx.scene.paint.Color.ORANGE);
        }
        case "empty" -> {
            box.setFill(javafx.scene.paint.Color.TRANSPARENT);
            box.setStroke(javafx.scene.paint.Color.WHITE);
            box.setStrokeWidth(2);
            box.getStrokeDashArray().addAll(3.0, 3.0);
        }
    }

    if (!type.equals("empty")) {
        box.setStrokeWidth(1.5);
    }

    double[] rotatedPos = rotatePosition(relativePos[0], relativePos[1], rot);

    box.translateXProperty().bind(
            parent.widthProperty().multiply(rotatedPos[0] - 0.5)
    );
    box.translateYProperty().bind(
            parent.heightProperty().multiply(rotatedPos[1] - 0.5)
    );
    parent.getChildren().add(box);
}

    private double[] rotatePosition(double x, double y, int rot) {
        double centerX = 0.5;
        double centerY = 0.5;

        double relX = x - centerX;
        double relY = y - centerY;

        double angle = Math.toRadians(rot * 90);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double newX = relX * cos - relY * sin + centerX;
        double newY = relX * sin + relY * cos + centerY;

        return new double[]{newX, newY};
    }

    /**
     * Clears all components from the ship grid.
     * Resets the grid to its initial empty state.
     */
    public void clearAllComponents() {
        componentsGrid.getChildren().clear();

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                ImageView cell = getImageViewAt(row, col);
                if (cell != null) {
                    cell.setImage(null);
                    componentsGrid.add(cell, col, row);
                }
            }
        }
        gridComponents.clear();
    }

    /**
     * Builds the ship components on the grid based on the ship model.
     * Adds all components in their correct positions with appropriate visual representations.
     *
     * @param ship The ship model containing component information
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
     * Highlights selected cabin cells on the grid.
     * Different highlight colors are used based on how many times a cabin is selected.
     *
     * @param selectedCabins List of coordinate pairs representing selected cabins
     */
    public void highlightSelectedCabins(List<org.javatuples.Pair<Integer, Integer>> selectedCabins) {
        Map<org.javatuples.Pair<Integer, Integer>, Long> counts = selectedCabins.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

        for (Rectangle rect : cellClickAreas.values()) {
            rect.setFill(javafx.scene.paint.Color.TRANSPARENT);
        }

        for (Map.Entry<org.javatuples.Pair<Integer, Integer>, Long> entry : counts.entrySet()) {
            org.javatuples.Pair<Integer, Integer> cabinCoords = entry.getKey();
            Long count = entry.getValue();

            int gridRow = cabinCoords.getValue0();
            int gridCol = cabinCoords.getValue1();

            String cellId = gridRow + "_" + gridCol;
            Rectangle clickArea = cellClickAreas.get(cellId);

            if (clickArea != null) {
                if (count == 1) {
                    clickArea.setFill(javafx.scene.paint.Color.color(1, 1, 0, 0.3));
                } else if (count == 2) {
                    clickArea.setFill(javafx.scene.paint.Color.color(1, 0.5, 0, 0.3));
                } else if (count >= 3) {
                    clickArea.setFill(javafx.scene.paint.Color.color(1, 0, 0, 0.4));
                } else {
                    clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                }
            }
        }
    }

    /**
     * Highlights specific cells on the grid with custom colors.
     * Used to visually indicate important cells or possible actions.
     *
     * @param highlights Map of coordinate pairs to colors for highlighting
     */
    public void highlightCells(Map<Pair<Integer, Integer>, Color> highlights) {

        for (Map.Entry<Pair<Integer, Integer>, Color> entry : highlights.entrySet()) {
            Pair<Integer, Integer> coords = entry.getKey();
            Color color = entry.getValue();

            String cellId = coords.getValue0() + "_" + coords.getValue1();
            Rectangle clickArea = cellClickAreas.get(cellId);

            if (clickArea != null) {
                clickArea.setStrokeWidth(4);
            }
            else{
                System.err.println("No click area found for cell: " + cellId);
            }
        }
    }

    /**
     * Interface for handling cell click events.
     */
    public interface CellClickHandler {
        /**
         * Called when a cell on the ship grid is clicked.
         *
         * @param row The row coordinate of the clicked cell
         * @param col The column coordinate of the clicked cell
         */
        void onCellClicked(int row, int col);
    }

    /**
     * Enables cell click handling on the ship grid.
     * Creates click areas for each valid cell and registers the provided handler for click events.
     *
     * @param handler The handler to call when a cell is clicked
     */
    public void enableCellClickHandler(CellClickHandler handler) {
        cellClickAreas.clear();
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                if(!checkIsValid(row, col))
                    continue;
                ImageView cell = getImageViewAt(row, col);
                if (cell != null) {
                    int finalRow = row;
                    int finalCol = col;

                    Rectangle clickArea = new Rectangle();
                    clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                    clickArea.setStroke(javafx.scene.paint.Color.LIGHTGREEN);
                    clickArea.setStrokeWidth(2);
                    clickArea.setOpacity(0.7);

                    clickArea.widthProperty().bind(
                            componentsGrid.widthProperty().divide(getCols()).subtract(getCols())
                    );
                    clickArea.heightProperty().bind(
                            componentsGrid.heightProperty().divide(getRows()).subtract(getRows())
                    );

                    clickArea.setOnMouseEntered(_ -> {
                        clickArea.setFill(javafx.scene.paint.Color.color(0, 1, 0, 0.2));
                        clickArea.setCursor(javafx.scene.Cursor.HAND);
                    });

                    clickArea.setOnMouseExited(_ -> {
                        clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        clickArea.setCursor(javafx.scene.Cursor.DEFAULT);
                    });

                    clickArea.setOnMouseClicked(_ -> {
                        if (handler != null) {
                            handler.onCellClicked(finalRow, finalCol);
                        }
                    });

                    componentsGrid.add(clickArea, col, row);
                    GridPane.setHalignment(clickArea, javafx.geometry.HPos.CENTER);
                    GridPane.setValignment(clickArea, javafx.geometry.VPos.CENTER);
                    
                    String cellId = row + "_" + col;
                    cellClickAreas.put(cellId, clickArea);
                }
            }
        }
    }

    protected abstract int getRows();
    protected abstract int getCols();

    protected ImageView getImageViewAt(int row, int col) {
        return null;
    }

    /**
     * Handles ship update events from the game model.
     * Reloads the ship when an update is received.
     *
     * @param ship The updated ship view model
     */
    @Override
    public void onShipUpdated(ViewShip ship) {
        reloadShip();
    }

    /**
     * Handles lobby update events from the game model.
     * Currently not implemented.
     *
     * @param lobby The updated lobby view model
     */
    @Override
    public void onLobbyUpdated(ViewLobby lobby) {
        // ignore
    }

    /**
     * Handles error message events from the game model.
     * Currently not implemented.
     *
     * @param message The error message received
     */
    @Override
    public void onErrorMessageReceived(String message) {
        // ignore
    }

    /**
     * Handles component in hand update events from the game model.
     * Currently not implemented.
     *
     * @param component The updated component view model
     */
    @Override
    public void onComponentInHandUpdated(ViewComponent component) {
        // ignore
    }

    /**
     * Handles board update events from the game model.
     * Currently not implemented.
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

        if (componentsGrid != null) {
            for (javafx.scene.Node node : componentsGrid.getChildren()) {
                try {
                    StackPane stackPane = (StackPane) node;
                    for (javafx.scene.Node child : stackPane.getChildren()) {
                        if (child.getClass().equals(ImageView.class)) {
                            try {
                                ImageView imageView = (ImageView) child;
                                imageView.setImage(null);
                                imageView.setRotate(0);
                            } catch (Exception e) {
                                System.err.println("Error cleaning up ImageView: " + e.getMessage());
                            }
                        } else if (child.getClass().equals(javafx.scene.layout.HBox.class)) {
                            try {
                                javafx.scene.layout.HBox hbox = (javafx.scene.layout.HBox) child;
                                hbox.getChildren().clear();
                            } catch (Exception e) {
                                System.err.println("Error cleaning up HBox: " + e.getMessage());
                            }
                        } else if (child.getClass().equals(Rectangle.class)) {
                            try {
                                Rectangle rect = (Rectangle) child;
                                rect.setFill(null);
                                rect.setStroke(null);
                                rect.widthProperty().unbind();
                                rect.heightProperty().unbind();
                                rect.translateXProperty().unbind();
                                rect.translateYProperty().unbind();
                                rect.setOnMouseEntered(null);
                                rect.setOnMouseExited(null);
                                rect.setOnMouseClicked(null);
                            } catch (Exception e) {
                                System.err.println("Error cleaning up Rectangle: " + e.getMessage());
                            }
                        }
                    }
                    stackPane.getChildren().clear();
                } catch (Exception e) {
                    if (node.getClass().equals(Rectangle.class)) {
                        try {
                            Rectangle rect = (Rectangle) node;
                            rect.setFill(null);
                            rect.setStroke(null);
                            rect.widthProperty().unbind();
                            rect.heightProperty().unbind();
                            rect.setOnMouseEntered(null);
                            rect.setOnMouseExited(null);
                            rect.setOnMouseClicked(null);
                        } catch (Exception rectEx) {
                            System.err.println("Error cleaning up click area Rectangle: " + rectEx.getMessage());
                        }
                    }
                }
            }
            componentsGrid.getChildren().clear();
        }

        if (rootPane != null) {
            rootPane.widthProperty().removeListener((_, _, _) -> Platform.runLater(() -> rootPane.requestLayout()));
            rootPane.heightProperty().removeListener((_, _, _) -> Platform.runLater(() -> rootPane.requestLayout()));
        }

        for (Rectangle rect : cellClickAreas.values()) {
            try {
                rect.setFill(null);
                rect.setStroke(null);
                rect.widthProperty().unbind();
                rect.heightProperty().unbind();
                rect.setOnMouseEntered(null);
                rect.setOnMouseExited(null);
                rect.setOnMouseClicked(null);
            } catch (Exception e) {
                System.err.println("Error cleaning up cell click area: " + e.getMessage());
            }
        }
        cellClickAreas.clear();

        gridComponents.clear();

        playerUsername = null;
        ship = null;
        playerColorLabel = null;
        usernameLabel = null;
        creditsLabel = null;
        inGameLabel = null;
        rootPane = null;
        componentsGrid = null;
    }

    protected abstract boolean checkIsValid(int row,int col);

}