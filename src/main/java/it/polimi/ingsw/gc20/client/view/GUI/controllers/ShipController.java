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
    // TODO: Display battery, cabin, cargo hold, aliens (look at buildingPhaese for first implementation)
    // TODO: Clickacle section for ship components, so that the user can click on a component and do thinks (custom for menu)

    protected int ROWS = 0;
    protected int COLS = 0;
    public String playerUsername;;
    private ViewShip ship;

    @FXML protected GridPane compoentsGrid;
    @FXML protected Label playerColorLabel;
    @FXML protected Label usernameLabel;
    @FXML protected Label creditsLabel;
    @FXML protected Label inGameLabel;
    @FXML protected StackPane rootPane;

    private final List<double[]> cargoCord3 = List.of(
            new double[]{0.3, 0.45},
            new double[]{0.625, 0.285},
            new double[]{0.625, 0.62}
    );

    private final List<double[]> cargoCord2 = List.of(
            new double[]{0.45, 0.285},
            new double[]{0.45, 0.62}
    );

    @FXML protected GridPane componentsGrid;

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
        ship = ClientGameModel.getInstance().getShip(playerUsername);
        // TODO: add shipChangeListener to update ship components when ship changes
        buildShipComponents(ship);
        loadObjects();
    }

    public void loadObjects() {
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLS; j++) {
                ImageView imageView = getImageViewAt(i, j);
                ViewComponent comp = ship.getComponent(i, j);
                if (imageView != null && comp != null) {
                    setComponentProp((StackPane) imageView.getParent(), comp);
                }
            }
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

        GridPane parent = (GridPane) targetCell.getParent();
        parent.getChildren().remove(targetCell);
        StackPane layeredPane = new StackPane();

        if (componentId >= 1000 && componentId <= 1003) {
            String username = ClientGameModel.getInstance().getUsername();
            ViewPlayer[] players = ClientGameModel.getInstance().getPlayers();
            ViewPlayer currentPlayer = Arrays.stream(players)
                    .filter(p -> p != null && username.equals(p.username))
                    .findFirst()
                    .orElse(null);
            if (currentPlayer != null && currentPlayer.playerColor != null) {
                switch (currentPlayer.playerColor) {
                    case BLUE -> componentId = 1000;
                    case RED -> componentId = 1001;
                    case GREEN -> componentId = 1002;
                    case YELLOW -> componentId = 1003;
                }
            }
        }

        String imagePath = "/fxml/tiles/" + componentId + ".jpg";
        try {
            Image componentImage = new Image(getClass().getResourceAsStream(imagePath));
            targetCell.setImage(componentImage);

            layeredPane.getChildren().add(targetCell);
            setComponentProp(layeredPane, comp);

            layeredPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            GridPane.setFillWidth(layeredPane, true);
            GridPane.setFillHeight(layeredPane, true);
            GridPane.setHgrow(layeredPane, Priority.ALWAYS);
            GridPane.setVgrow(layeredPane, Priority.ALWAYS);

            parent.add(layeredPane, col, row);

            gridComponents.put(cellId, componentId);
            return true;
        } catch (Exception e) {
            System.err.println("Error uploading the image: " + e.getMessage());
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

    public void enableCellClickHandler(CellClickHandler handler) {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
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

                    clickArea.setOnMouseClicked(event -> {
                        if (handler != null) {
                            handler.onCellClicked(finalRow, finalCol);
                        }
                    });

                    componentsGrid.add(clickArea, col, row);
                    GridPane.setHalignment(clickArea, javafx.geometry.HPos.CENTER);
                    GridPane.setValignment(clickArea, javafx.geometry.VPos.CENTER);
                }
            }
        }
    }

    public void enableMultipleCellClickHandler(MultipleCellClickHandler handler) {
        Set<int[]> selectedCells = new HashSet<>();
        Map<Rectangle, int[]> rectToCoord = new HashMap<>();

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                ImageView cell = getImageViewAt(row, col);
                if (cell != null) {
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

                    int[] coord = new int[]{row, col};
                    rectToCoord.put(clickArea, coord);

                    clickArea.setOnMouseClicked(event -> {
                        if (selectedCells.contains(coord)) {
                            selectedCells.remove(coord);
                            clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        } else {
                            selectedCells.add(coord);
                            clickArea.setFill(javafx.scene.paint.Color.color(0, 1, 0, 0.2));
                        }
                    });

                    clickArea.setOnMouseEntered(_ -> {
                        if (!selectedCells.contains(coord)) {
                            clickArea.setFill(javafx.scene.paint.Color.color(0, 1, 0, 0.1));
                        }
                        clickArea.setCursor(javafx.scene.Cursor.HAND);
                    });

                    clickArea.setOnMouseExited(_ -> {
                        if (!selectedCells.contains(coord)) {
                            clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        }
                        clickArea.setCursor(javafx.scene.Cursor.DEFAULT);
                    });

                    componentsGrid.add(clickArea, col, row);
                    GridPane.setHalignment(clickArea, javafx.geometry.HPos.CENTER);
                    GridPane.setValignment(clickArea, javafx.geometry.VPos.CENTER);
                }
            }
        }

        Runnable finalizeSelection = () -> {
            List<int[]> selected = new ArrayList<>(selectedCells);
            handler.onCellsClicked(selected);
        };
    }

    public interface MultipleCellClickHandler {
        void onCellsClicked(List<int[]> coordinates);
    }

    protected abstract int getRows();
    protected abstract int getCols();

    protected ImageView getImageViewAt(int row, int col) {
        return null;
    }
}