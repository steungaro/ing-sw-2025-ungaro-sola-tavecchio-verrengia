package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.util.HashMap;
import java.util.Map;

public class Ship0Controller {

    @FXML private ImageView boardImageView;
    @FXML private GridPane shipGridPane; // Allineato con il nome nel FXML
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

    // Mappa per memorizzare i componenti presenti sulla griglia
    private final Map<String, Integer> gridComponents = new HashMap<>();

    // Numero di righe e colonne nella griglia
    private final int ROWS = 5;
    private final int COLS = 5;

    @FXML
    private void initialize() {
        boardImageView.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                Platform.runLater(() -> {
                    double boardWidth = boardImageView.getBoundsInParent().getWidth();
                    double boardHeight = boardImageView.getBoundsInParent().getHeight();

                    double gridX = boardWidth * 0.25;
                    double gridY = boardHeight * 0.18;
                    double gridWidth = boardWidth * 0.5;
                    double gridHeight = boardHeight * 0.64;

                    shipGridPane.setLayoutX(gridX);
                    shipGridPane.setLayoutY(gridY);
                    shipGridPane.setPrefSize(gridWidth, gridHeight);
                    shipGridPane.setMaxSize(gridWidth, gridHeight);

                    updateCoordinateLabels(gridX, gridY);
                });
            }
        });
    }

    /**
     * Aggiunge un componente alla griglia in base al suo ID e coordinate
     * @param componentId ID del componente da aggiungere
     * @param row Riga della griglia (0-4)
     * @param col Colonna della griglia (0-4)
     * @return true se l'aggiunta è avvenuta con successo, false altrimenti
     */
    public boolean addComponent(int componentId, int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return false;
        }

        String cellId = row + "_" + col;
        ImageView targetCell = getImageViewAt(row, col);

        if (targetCell == null) {
            return false;
        }

        // Carica l'immagine appropriata in base all'ID del componente
        String imagePath = "/images/components/component_" + componentId + ".png";
        try {
            Image componentImage = new Image(getClass().getResourceAsStream(imagePath));
            targetCell.setImage(componentImage);
            gridComponents.put(cellId, componentId);
            return true;
        } catch (Exception e) {
            System.err.println("Impossibile caricare l'immagine del componente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Rimuove un componente dalla posizione specificata
     * @param row Riga della griglia (0-4)
     * @param col Colonna della griglia (0-4)
     * @return true se la rimozione è avvenuta con successo, false altrimenti
     */
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

    /**
     * Ottiene l'ImageView alla posizione specificata
     * @param row Riga della griglia
     * @param col Colonna della griglia
     * @return L'ImageView corrispondente o null se non trovato
     */
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

    /**
     * Aggiorna le etichette delle coordinate
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
     * Pulisce tutta la griglia rimuovendo tutti i componenti
     */
    public void clearAllComponents() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                removeComponent(row, col);
            }
        }
        gridComponents.clear();
    }
}