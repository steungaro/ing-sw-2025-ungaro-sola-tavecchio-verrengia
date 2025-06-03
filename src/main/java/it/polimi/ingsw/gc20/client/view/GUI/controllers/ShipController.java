package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.*;

public abstract class ShipController {

    protected int ROWS = 0;
    protected int COLS = 0;

    private ViewShip ship;

    public enum ShipState {
        Building, Viewing
    }

    @FXML private ImageView imageBooked_0_0;
    @FXML private ImageView imageBooked_0_1;
    @FXML protected Label playerColorLabel;
    @FXML protected Label usernameLabel;
    @FXML protected Label creditsLabel;
    @FXML protected Label inGameLabel;

    // 3 Slot -> 60x90, 125x57, 125x124
    // 2 Slot -> 90x57, 90x124
    private final List<int[]> cargoCord3 = List.of(
            new int[]{60, 90},
            new int[]{125, 57},
            new int[]{125, 124}
    );

    private final List<int[]> cargoCord2 = List.of(
            new int[]{90, 57},
            new int[]{90, 124}
    );

    @FXML private ImageView boardImageView;
    @FXML private GridPane componentsGrid; // Allineato con il nome nel FXML
    @FXML private Pane gridWrapper;
    @FXML private Label X_Label;
    @FXML private Label Y_Label;

    private final Map<String, Integer> gridComponents = new HashMap<>();
    private ShipState shipState = ShipState.Viewing;

    public void updateStatisticBoard(ViewPlayer player) {
        if (player != null) {
            playerColorLabel.setText("Color: " + (player.playerColor != null ? player.playerColor.name() : "N/A"));
            usernameLabel.setText("Username: " + player.username);
            creditsLabel.setText("Credits: " + player.credits);
            inGameLabel.setText("In Game: " + (player.inGame ? "Yes" : "No"));
        }
    }

    @FXML
    private void initialize() {
        ship = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());

        if (boardImageView != null && boardImageView.getImage() != null) {
            setupGridBounds();
        } else if (boardImageView != null) {
            boardImageView.imageProperty().addListener((obs, oldImg, newImg) -> {
                if (newImg != null) {
                    setupGridBounds();
                }
            });
        }

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

        buildShipComponents(ship);
    }

    private void setupGridBounds() {
        Platform.runLater(() -> {
            if (boardImageView == null || componentsGrid == null) return;

            double boardWidth = boardImageView.getBoundsInParent().getWidth();
            double boardHeight = boardImageView.getBoundsInParent().getHeight();

            double gridX = boardWidth * 0.25;
            double gridY = boardHeight * 0.18;
            double gridWidth = boardWidth * 0.5;
            double gridHeight = boardHeight * 0.64;

            componentsGrid.setLayoutX(gridX);
            componentsGrid.setLayoutY(gridY);
            componentsGrid.setPrefSize(gridWidth, gridHeight);
            componentsGrid.setMaxSize(gridWidth, gridHeight);

            updateCoordinateLabels(gridX, gridY);
        });
    }

    private void updateCoordinateLabels(double x, double y) {
        if (X_Label != null) {
            X_Label.setText(String.format("X: %.2f", x));
        }
        if (Y_Label != null) {
            Y_Label.setText(String.format("Y: %.2f", y));
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

        String imagePath = "/fxml/tiles/" + componentId + ".jpg";
        try {
            Image componentImage = new Image(getClass().getResourceAsStream(imagePath));
            targetCell.setImage(componentImage);

            // Imposta ImageView per adattarsi completamente alla cella
            targetCell.setFitWidth(parent.getWidth() / getCols());
            targetCell.setFitHeight(parent.getHeight() / getRows());
            targetCell.setPreserveRatio(false);

            // Imposta proprietà per far sì che l'immagine si adatti quando la cella cambia dimensione
            targetCell.fitWidthProperty().bind(parent.widthProperty().divide(getCols()));
            targetCell.fitHeightProperty().bind(parent.heightProperty().divide(getRows()));

            layeredPane.getChildren().add(targetCell);

            setComponentProp(layeredPane, comp);

            // Imposta il StackPane per occupare tutto lo spazio disponibile
            layeredPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            parent.add(layeredPane, col-1, row);

            // TODO Rotate the IMG
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
        // Implementazione specifica per ViewBattery, se necessario
        Label batteryLabel = new Label(Integer.toString(comp.availableEnergy));

        try {
            ImageView batteryIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/icons/battery.png")));
            batteryIcon.setFitHeight(25);
            batteryIcon.setFitWidth(25);
            batteryIcon.setPreserveRatio(true);

            StackPane.setAlignment(batteryIcon, javafx.geometry.Pos.TOP_LEFT);
            StackPane.setAlignment(batteryIcon, javafx.geometry.Pos.BOTTOM_LEFT);

            layeredPane.getChildren().addAll(batteryLabel, batteryIcon);
        } catch (Exception e) {
            System.err.println("Impossibile caricare l'immagine della batterua: " + e.getMessage());
            layeredPane.getChildren().add(batteryLabel);
        }
        return;
    }

    public void setComponentProp(StackPane layeredPane, ViewCabin comp) {
        // Gestione degli alieni nella cabina
        if (comp.alien) {
            String alienImagePath;
            if (comp.alienColor == AlienColor.PURPLE) {
                alienImagePath = "/images/icons/purple_alien.png";
            } else {
                alienImagePath = "/images/icons/brown_alien.png";
            }

            try {
                ImageView alienIcon = new ImageView(new Image(getClass().getResourceAsStream(alienImagePath)));
                alienIcon.setFitHeight(30);
                alienIcon.setFitWidth(30);
                alienIcon.setPreserveRatio(true);

                StackPane.setAlignment(alienIcon, javafx.geometry.Pos.TOP_LEFT);
                layeredPane.getChildren().add(alienIcon);
            } catch (Exception e) {
                System.err.println("Impossibile caricare l'immagine dell'alieno: " + e.getMessage());
            }
        } else if (comp.astronauts > 0) {
            // Aggiungi il numero di astronauti
            Label astronautsLabel = new Label(Integer.toString(comp.astronauts));

            try {
                ImageView astronautIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/icons/astr.png")));
                astronautIcon.setFitHeight(25);
                astronautIcon.setFitWidth(25);
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
            String colorStr;
            switch (comp.cabinColor) {
                case PURPLE -> colorStr = "purple";
                case BROWN -> colorStr = "brown";
                case BOTH -> colorStr = "both";
                default -> colorStr = null;
            }

            if (colorStr != null) {
                Label colorIndicator = new Label("");
                colorIndicator.setStyle("-fx-background-color: " + colorStr + "; -fx-min-width: 10px; -fx-min-height: 10px; -fx-border-color: white;");
                StackPane.setAlignment(colorIndicator, javafx.geometry.Pos.BOTTOM_RIGHT);
                layeredPane.getChildren().add(colorIndicator);
            }
        }
    }

    // 3 Slot -> 60x90, 125x57, 125x124
    // 2 Slot -> 90x57, 90x124
    // Cargo 30px x 30px
    public void setComponentProp(StackPane layeredPane, ViewCargoHold comp) {
        List<int[]> coordinates;
        if (comp.getSize() == 2) {
            coordinates = cargoCord2;
        } else {
            coordinates = cargoCord3;
        }

        int index = 0;

        for (int i = 0; i < comp.red; i++) {
            if (index < coordinates.size()) {
                Pane cargoBox = createCargoBox("red");
                cargoBox.setLayoutX(coordinates.get(index)[0]);
                cargoBox.setLayoutY(coordinates.get(index)[1]);
                layeredPane.getChildren().add(cargoBox);
                index++;
            }
        }

        for (int i = 0; i < comp.green; i++) {
            if (index < coordinates.size()) {
                Pane cargoBox = createCargoBox("green");
                cargoBox.setLayoutX(coordinates.get(index)[0]);
                cargoBox.setLayoutY(coordinates.get(index)[1]);
                layeredPane.getChildren().add(cargoBox);
                index++;
            }
        }

        for (int i = 0; i < comp.blue; i++) {
            if (index < coordinates.size()) {
                Pane cargoBox = createCargoBox("blue");
                cargoBox.setLayoutX(coordinates.get(index)[0]);
                cargoBox.setLayoutY(coordinates.get(index)[1]);
                layeredPane.getChildren().add(cargoBox);
                index++;
            }
        }

        for (int i = 0; i < comp.yellow; i++) {
            if (index < coordinates.size()) {
                Pane cargoBox = createCargoBox("yellow");
                cargoBox.setLayoutX(coordinates.get(index)[0]);
                cargoBox.setLayoutY(coordinates.get(index)[1]);
                layeredPane.getChildren().add(cargoBox);
                index++;
            }
        }

        for (int i = 0; i < comp.free; i++) {
            if (index < coordinates.size()) {
                Pane cargoBox = createCargoBox("empty");
                cargoBox.setLayoutX(coordinates.get(index)[0]);
                cargoBox.setLayoutY(coordinates.get(index)[1]);
                layeredPane.getChildren().add(cargoBox);
                index++;
            }
        }
    }

    private Pane createCargoBox(String type) {
        Pane box = new Pane();
        box.setPrefSize(30, 30);
        box.setMinSize(30, 30);
        box.setMaxSize(30, 30);

        switch (type) {
            case "red" -> box.setStyle("-fx-background-color: red; -fx-border-color: black; -fx-border-width: 1px;");
            case "green" -> box.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-border-width: 1px;");
            case "blue" -> box.setStyle("-fx-background-color: blue; -fx-border-color: black; -fx-border-width: 1px;");
            case "yellow" -> box.setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-border-width: 1px;");
            case "empty" -> box.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px; -fx-border-style: dashed;");
        }

        return box;
    }

    public boolean removeComponent(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return false;
        }

        String cellId = row + "_" + col;
        ImageView targetCell = getImageViewAt(row, col);

        if (targetCell == null) {
            return false;
        }

        targetCell.setImage(null);
        gridComponents.remove(cellId);
        return true;
    }

    public void clearAllComponents() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                removeComponent(row, col);
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

    protected abstract int getRows();
    protected abstract int getCols();

    protected ImageView getImageViewAt(int row, int col){
        return null;
    }

    public void setShipState(ShipState state) {
        this.shipState = state;
    }

}
