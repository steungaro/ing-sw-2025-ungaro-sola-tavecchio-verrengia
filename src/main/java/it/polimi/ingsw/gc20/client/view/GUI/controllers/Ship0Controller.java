package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class Ship0Controller {

    @FXML private ImageView boardImageView;
    @FXML private GridPane componentsGrid; // Allineato con il nome nel FXML
    @FXML private Pane gridWrapper;
    @FXML private Label X_Label;
    @FXML private Label Y_Label;

    // ImageView per ogni cella della griglia
    @FXML private ImageView imageCell_0_0;
    @FXML private ImageView imageCell_0_1;
    @FXML private ImageView imageCell_0_2;
    @FXML private ImageView imageCell_0_3;
    @FXML private ImageView imageCell_0_4;
    @FXML private ImageView imageCell_1_0;
    @FXML private ImageView imageCell_1_1;
    @FXML private ImageView imageCell_1_2;
    @FXML private ImageView imageCell_1_3;
    @FXML private ImageView imageCell_1_4;
    @FXML private ImageView imageCell_2_0;
    @FXML private ImageView imageCell_2_1;
    @FXML private ImageView imageCell_2_2;
    @FXML private ImageView imageCell_2_3;
    @FXML private ImageView imageCell_2_4;
    @FXML private ImageView imageCell_3_0;
    @FXML private ImageView imageCell_3_1;
    @FXML private ImageView imageCell_3_2;
    @FXML private ImageView imageCell_3_3;
    @FXML private ImageView imageCell_3_4;
    @FXML private ImageView imageCell_4_0;
    @FXML private ImageView imageCell_4_1;
    @FXML private ImageView imageCell_4_2;
    @FXML private ImageView imageCell_4_3;
    @FXML private ImageView imageCell_4_4;

    private final Map<String, Integer> gridComponents = new HashMap<>();

    private final int ROWS = 5;
    private final int COLS = 5;

    @FXML
    private void initialize() {
        if (boardImageView != null && boardImageView.getImage() != null) {
            setupGridBounds();
        } else if (boardImageView != null) {
            boardImageView.imageProperty().addListener((obs, oldImg, newImg) -> {
                if (newImg != null) {
                    setupGridBounds();
                }
            });
        }
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
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
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

        String imagePath = "/tiles/" + componentId + ".png";
        try {
            Image componentImage = new Image(getClass().getResourceAsStream(imagePath));
            targetCell.setImage(componentImage);
            layeredPane.getChildren().add(targetCell);

            setComponentProp(layeredPane, comp);

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
        // Implementazione specifica per ViewBattery, se necessario
        return;
    }

    public void setComponentProp(StackPane layeredPane, ViewCabin comp) {
        // Implementazione specifica per ViewCabin, se necessario
        return;
    }

    public void setComponentProp(StackPane layeredPane, ViewCargoHold comp) {
        // Implementazione specifica per ViewCargoHold, se necessario
        return;
    }

    public void setComponentProp(StackPane layeredPane, ViewStartingCabin comp) {
        // Implementazione specifica per ViewStartingCabin, se necessario
        return;
    }

    public void setComponentProp(StackPane layeredPane, ViewSpecialCargoHold comp) {
        // Implementazione specifica per ViewSpecialCargoHold, se necessario
        return;
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

    private ImageView getImageViewAt(int row, int col) {
        return switch (row) {
            case 0 -> switch (col) {
                case 0 -> imageCell_0_0;
                case 1 -> imageCell_0_1;
                case 2 -> imageCell_0_2;
                case 3 -> imageCell_0_3;
                case 4 -> imageCell_0_4;
                default -> null;
            };
            case 1 -> switch (col) {
                case 0 -> imageCell_1_0;
                case 1 -> imageCell_1_1;
                case 2 -> imageCell_1_2;
                case 3 -> imageCell_1_3;
                case 4 -> imageCell_1_4;
                default -> null;
            };
            case 2 -> switch (col) {
                case 0 -> imageCell_2_0;
                case 1 -> imageCell_2_1;
                case 2 -> imageCell_2_2;
                case 3 -> imageCell_2_3;
                case 4 -> imageCell_2_4;
                default -> null;
            };
            case 3 -> switch (col) {
                case 0 -> imageCell_3_0;
                case 1 -> imageCell_3_1;
                case 2 -> imageCell_3_2;
                case 3 -> imageCell_3_3;
                case 4 -> imageCell_3_4;
                default -> null;
            };
            case 4 -> switch (col) {
                case 0 -> imageCell_4_0;
                case 1 -> imageCell_4_1;
                case 2 -> imageCell_4_2;
                case 3 -> imageCell_4_3;
                case 4 -> imageCell_4_4;
                default -> null;
            };
            default -> null;
        };
    }

    public void clearAllComponents() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                removeComponent(row, col);
            }
        }
        gridComponents.clear();
    }
}