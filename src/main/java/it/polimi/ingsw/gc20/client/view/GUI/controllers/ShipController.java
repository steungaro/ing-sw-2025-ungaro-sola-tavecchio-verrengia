package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;
import java.util.List;

public abstract class ShipController {

    protected int ROWS = 0;
    protected int COLS = 0;

    private ViewShip ship;
    protected CellClickHandler cellClickHandler;
    public enum ShipState {
        Building, Viewing
    }

    @FXML protected Label playerColorLabel;
    @FXML protected Label usernameLabel;
    @FXML protected Label creditsLabel;
    @FXML protected Label inGameLabel;
    @FXML protected StackPane rootPane;

    private final List<double[]> cargoCord3 = List.of(
            new double[]{0.3, 0.45},    // Relative positions (0-1)
            new double[]{0.625, 0.285},
            new double[]{0.625, 0.62}
    );

    private final List<double[]> cargoCord2 = List.of(
            new double[]{0.45, 0.285},
            new double[]{0.45, 0.62}
    );

    @FXML protected GridPane componentsGrid;
    @FXML private Label X_Label;
    @FXML private Label Y_Label;
    @FXML protected ImageView bgImage;

    protected final Map<String, Integer> gridComponents = new HashMap<>();

    public void updateStatisticBoard(ViewPlayer player) {
        if (player != null) {
            playerColorLabel.setText("Color: " + (player.playerColor != null ? player.playerColor.name() : "N/A"));
            usernameLabel.setText("Username: " + player.username);
            creditsLabel.setText("Credits: " + player.credits);
            inGameLabel.setText("In Game: " + (player.inGame ? "Yes" : "No"));
        }
    }

    @FXML
    protected void initialize() {
        ship = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());
        ClientGameModel clientGameModel = ClientGameModel.getInstance();
        if (clientGameModel != null) {
            String currentUsername = clientGameModel.getUsername();
            ViewPlayer[] players = clientGameModel.getPlayers();
            if (players != null && currentUsername != null) {
                Optional<ViewPlayer> currentPlayerOpt = Arrays.stream(players)
                        .filter(p -> currentUsername.equals(p.username))
                        .findFirst();
                currentPlayerOpt.ifPresent(this::updateStatisticBoard);
            }
        }

        bgImage.fitWidthProperty().bind(rootPane.widthProperty());
        bgImage.fitHeightProperty().bind(rootPane.heightProperty());

        bgImage.setPreserveRatio(true);

        buildShipComponents(ship);
    }

    private void updateCoordinateLabels(double width, double height) {
        if (X_Label != null) {
            X_Label.setText(String.format("W: %.0f", width));
        }
        if (Y_Label != null) {
            Y_Label.setText(String.format("H: %.0f", height));
        }
    }

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
            Image componentImage = new Image(getClass().getResourceAsStream(imagePath));
            targetCell.setImage(componentImage);

            // Bind size to parent cell for perfect fit
            targetCell.fitWidthProperty().bind(layeredPane.widthProperty());
            targetCell.fitHeightProperty().bind(layeredPane.heightProperty());
            targetCell.setPreserveRatio(false);

            layeredPane.getChildren().add(targetCell);
            setComponentProp(layeredPane, comp);

            // Make sure the layered pane fills the cell completely
            layeredPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            GridPane.setFillWidth(layeredPane, true);
            GridPane.setFillHeight(layeredPane, true);
            GridPane.setHgrow(layeredPane, Priority.ALWAYS);
            GridPane.setVgrow(layeredPane, Priority.ALWAYS);

            parent.add(layeredPane, col, row);

            gridComponents.put(cellId, componentId);
            return true;
        } catch (Exception e) {
            System.err.println("Impossibile caricare l'immagine del componente: " + e.getMessage());
            return false;
        }
    }

    public void setComponentProp(StackPane layeredPane, ViewComponent comp) {
        return;
    }

    public void setComponentProp(StackPane layeredPane, ViewBattery comp) {
        Label batteryLabel = new Label(Integer.toString(comp.availableEnergy));
        batteryLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.7); -fx-padding: 2px;");

        try {
            ImageView batteryIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/icons/battery.png")));
            batteryIcon.fitWidthProperty().bind(layeredPane.widthProperty().multiply(0.3));
            batteryIcon.fitHeightProperty().bind(layeredPane.heightProperty().multiply(0.3));
            batteryIcon.setPreserveRatio(true);

            StackPane.setAlignment(batteryLabel, javafx.geometry.Pos.TOP_LEFT);
            StackPane.setAlignment(batteryIcon, javafx.geometry.Pos.BOTTOM_LEFT);

            layeredPane.getChildren().addAll(batteryLabel, batteryIcon);
        } catch (Exception e) {
            System.err.println("Impossibile caricare l'immagine della batteria: " + e.getMessage());
            layeredPane.getChildren().add(batteryLabel);
        }
    }

    public void setComponentProp(StackPane layeredPane, ViewCabin comp) {
        if (comp.alien) {
            String alienImagePath = comp.alienColor == AlienColor.PURPLE ?
                    "/images/icons/purple_alien.png" : "/images/icons/brown_alien.png";

            try {
                ImageView alienIcon = new ImageView(new Image(getClass().getResourceAsStream(alienImagePath)));
                alienIcon.fitWidthProperty().bind(layeredPane.widthProperty().multiply(0.4));
                alienIcon.fitHeightProperty().bind(layeredPane.heightProperty().multiply(0.4));
                alienIcon.setPreserveRatio(true);

                StackPane.setAlignment(alienIcon, javafx.geometry.Pos.TOP_LEFT);
                layeredPane.getChildren().add(alienIcon);
            } catch (Exception e) {
                System.err.println("Impossibile caricare l'immagine dell'alieno: " + e.getMessage());
            }
        } else if (comp.astronauts > 0) {
            Label astronautsLabel = new Label(Integer.toString(comp.astronauts));
            astronautsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.7); -fx-padding: 2px;");

            try {
                ImageView astronautIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/icons/astr.png")));
                astronautIcon.fitWidthProperty().bind(layeredPane.widthProperty().multiply(0.3));
                astronautIcon.fitHeightProperty().bind(layeredPane.heightProperty().multiply(0.3));
                astronautIcon.setPreserveRatio(true);

                StackPane.setAlignment(astronautsLabel, javafx.geometry.Pos.TOP_LEFT);
                StackPane.setAlignment(astronautIcon, javafx.geometry.Pos.BOTTOM_LEFT);

                layeredPane.getChildren().addAll(astronautsLabel, astronautIcon);
            } catch (Exception e) {
                System.err.println("Impossibile caricare l'immagine dell'astronauta: " + e.getMessage());
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
                StackPane.setAlignment(colorIndicator, javafx.geometry.Pos.BOTTOM_RIGHT);
                StackPane.setMargin(colorIndicator, new Insets(0, 5, 5, 0));
                layeredPane.getChildren().add(colorIndicator);
            }
        }
    }

    public void setComponentProp(StackPane layeredPane, ViewCargoHold comp) {
        List<double[]> coordinates = comp.getSize() == 2 ? cargoCord2 : cargoCord3;
        int index = 0;

        // Create cargo boxes with relative positioning
        for (int i = 0; i < comp.red && index < coordinates.size(); i++, index++) {
            addCargoBox(layeredPane, coordinates.get(index), "red");
        }
        for (int i = 0; i < comp.green && index < coordinates.size(); i++, index++) {
            addCargoBox(layeredPane, coordinates.get(index), "green");
        }
        for (int i = 0; i < comp.blue && index < coordinates.size(); i++, index++) {
            addCargoBox(layeredPane, coordinates.get(index), "blue");
        }
        for (int i = 0; i < comp.yellow && index < coordinates.size(); i++, index++) {
            addCargoBox(layeredPane, coordinates.get(index), "yellow");
        }
        for (int i = 0; i < comp.free && index < coordinates.size(); i++, index++) {
            addCargoBox(layeredPane, coordinates.get(index), "empty");
        }
    }

    private void addCargoBox(StackPane parent, double[] relativePos, String type) {
        Rectangle box = new Rectangle();

        // Bind size to parent for scaling
        box.widthProperty().bind(parent.widthProperty().multiply(0.15));
        box.heightProperty().bind(parent.heightProperty().multiply(0.15));

        // Set color based on type
        switch (type) {
            case "red" -> box.setFill(javafx.scene.paint.Color.RED);
            case "green" -> box.setFill(javafx.scene.paint.Color.GREEN);
            case "blue" -> box.setFill(javafx.scene.paint.Color.BLUE);
            case "yellow" -> box.setFill(javafx.scene.paint.Color.YELLOW);
            case "empty" -> {
                box.setFill(javafx.scene.paint.Color.TRANSPARENT);
                box.setStroke(javafx.scene.paint.Color.WHITE);
                box.setStrokeWidth(1);
                box.getStrokeDashArray().addAll(5.0, 5.0);
            }
        }

        box.setStroke(javafx.scene.paint.Color.BLACK);
        box.setStrokeWidth(1);

        // Position using binding for responsive layout
        box.translateXProperty().bind(
                parent.widthProperty().multiply(relativePos[0] - 0.5)
        );
        box.translateYProperty().bind(
                parent.heightProperty().multiply(relativePos[1] - 0.5)
        );

        parent.getChildren().add(box);
    }

    public boolean removeComponent(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return false;
        }

        String cellId = row + "_" + col;

        // Find the node in the grid at this position
        Node nodeToRemove = null;
        for (Node node : componentsGrid.getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);
            if (nodeRow != null && nodeCol != null && nodeRow == row && nodeCol == col) {
                nodeToRemove = node;
                break;
            }
        }

        if (nodeToRemove != null) {
            componentsGrid.getChildren().remove(nodeToRemove);

            // Re-add empty ImageView
            ImageView emptyCell = getImageViewAt(row, col);
            if (emptyCell != null) {
                emptyCell.setImage(null);
                GridPane.setFillWidth(emptyCell, true);
                GridPane.setFillHeight(emptyCell, true);
                componentsGrid.add(emptyCell, col, row);
            }
        }

        gridComponents.remove(cellId);
        return true;
    }

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

    public interface CellClickHandler {
        void onCellClicked(int row, int col);
    }

    public void enableGridInteraction(CellClickHandler handler) {
        this.cellClickHandler = handler;
        System.out.println("Placing mode activated");

        // Add click overlays to all cells
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                final int finalRow = row;
                final int finalCol = col;

                Rectangle clickArea = new Rectangle();
                clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                clickArea.setStroke(javafx.scene.paint.Color.LIGHTGREEN);
                clickArea.setStrokeWidth(2);
                clickArea.setOpacity(0.7);

                // Bind size to grid cell
                clickArea.widthProperty().bind(
                        componentsGrid.widthProperty().divide(getCols()).subtract(4)
                );
                clickArea.heightProperty().bind(
                        componentsGrid.heightProperty().divide(getRows()).subtract(4)
                );

                clickArea.setOnMouseClicked(event -> {
                    if (cellClickHandler != null) {
                        cellClickHandler.onCellClicked(finalRow, finalCol);
                    }
                });

                clickArea.setOnMouseEntered(e -> {
                    clickArea.setFill(javafx.scene.paint.Color.color(0, 1, 0, 0.2));
                    clickArea.setCursor(javafx.scene.Cursor.HAND);
                });

                clickArea.setOnMouseExited(e -> {
                    clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                    clickArea.setCursor(javafx.scene.Cursor.DEFAULT);
                });

                componentsGrid.add(clickArea, col, row);
                GridPane.setHalignment(clickArea, javafx.geometry.HPos.CENTER);
                GridPane.setValignment(clickArea, javafx.geometry.VPos.CENTER);
            }
        }
    }

    public void disableGridInteraction() {
        this.cellClickHandler = null;
        System.out.println("Placing mode deactivated");

        componentsGrid.getChildren().removeIf(node -> node instanceof Rectangle);
    }

    protected abstract int getRows();
    protected abstract int getCols();

    protected ImageView getImageViewAt(int row, int col) {
        return null;
    }
}