package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Ship2Controller {

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

    private final int ROWS = 5;
    private final int COLS = 7;

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

    public ImageView placeComponent(String imagePath, int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            throw new IllegalArgumentException("Indice di riga o colonna fuori dai limiti");
        }
        if (imagePath == null) {
            throw new IllegalArgumentException("Il percorso dell'immagine non può essere nullo");
        }
        if (componentsGrid == null) {
            throw new IllegalStateException("componentsGrid non è inizializzato.");
        }


        Image componentImage = new Image(getClass().getResourceAsStream(imagePath));
        ImageView componentView = new ImageView(componentImage);

        double cellWidth = componentsGrid.getPrefWidth() / COLS * 0.8; // Fattore di scala opzionale (0.8)

        componentView.setPreserveRatio(true);
        componentView.setFitWidth(cellWidth);

        componentsGrid.add(componentView, col, row);
        return componentView;
    }

    public void clearGridPosition(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            throw new IllegalArgumentException("Indice di riga o colonna fuori dai limiti");
        }
        if (componentsGrid == null) return;

        componentsGrid.getChildren().removeIf(node ->
                GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null &&
                GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col);
    }

    public void clearAllComponents() {
        if (componentsGrid != null) {
            componentsGrid.getChildren().clear();
        }
    }
}