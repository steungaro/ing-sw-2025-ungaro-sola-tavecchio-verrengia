package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Ship0Controller {

    @FXML
    private ImageView boardImageView;

    @FXML
    private GridPane componentsGrid;

    @FXML
    private Pane gridWrapper;

    @FXML
    private Label X_Label;

    @FXML
    private Label Y_Label;

    // Number of rows and columns in the grid
    private final int ROWS = 5;
    private final int COLS = 5;

    @FXML
    private void initialize() {
        boardImageView.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                Platform.runLater(() -> {
                    double boardWidth = boardImageView.getBoundsInParent().getWidth();
                    double boardHeight = boardImageView.getBoundsInParent().getHeight();

                    double gridX = boardWidth * 0.25; // Esempio: inizia al 25% della larghezza dell'immagine
                    double gridY = boardHeight * 0.18; // Esempio: inizia al 18% dell'altezza dell'immagine
                    double gridWidth = boardWidth * 0.5;  // Esempio: la griglia è larga il 50% dell'immagine
                    double gridHeight = boardHeight * 0.64; // Esempio: la griglia è alta il 64% dell'immagine

                    componentsGrid.setLayoutX(gridX);
                    componentsGrid.setLayoutY(gridY);
                    componentsGrid.setPrefSize(gridWidth, gridHeight);
                    componentsGrid.setMaxSize(gridWidth, gridHeight);

                    updateCoordinateLabels(gridX, gridY);
                });
            }
        });
    }

    /**
     * Updates the X and Y coordinate labels
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    private void updateCoordinateLabels(double x, double y) {
        if (X_Label != null) {
            X_Label.setText(String.format("X: %.2f", x));
        }

        if (Y_Label != null) {
            Y_Label.setText(String.format("Y: %.2f", y));
        }
    }

    /**
     * Places a component image at the specified grid position
     * @param imagePath Path to the component image
     * @param row The row position in the grid
     * @param col The column position in the grid
     * @return The placed ImageView for further configuration if needed
     * @throws IllegalArgumentException if row or column is out of bounds
     */
    public ImageView placeComponent(String imagePath, int row, int col) {
        // Rest of the method remains unchanged
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            throw new IllegalArgumentException("Row or column index out of bounds");
        }

        if (imagePath == null) {
            throw new IllegalArgumentException("Image path cannot be null");
        }

        Image componentImage = new Image(getClass().getResourceAsStream(imagePath));
        ImageView componentView = new ImageView(componentImage);

        // Calculate cell size based on grid dimensions
        double cellWidth = componentsGrid.getPrefWidth() / COLS * 0.8;

        // Set appropriate sizing
        componentView.setPreserveRatio(true);
        componentView.setFitWidth(cellWidth);

        // Add to grid
        componentsGrid.add(componentView, col, row);
        return componentView;
    }

    // Other methods remain unchanged...
    public void clearGridPosition(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            throw new IllegalArgumentException("Row or column index out of bounds");
        }

        // Get children at this position and remove them
        componentsGrid.getChildren().removeIf(node ->
                GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null &&
                        GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col);
    }

    public void clearAllComponents() {
        componentsGrid.getChildren().clear();
    }
}