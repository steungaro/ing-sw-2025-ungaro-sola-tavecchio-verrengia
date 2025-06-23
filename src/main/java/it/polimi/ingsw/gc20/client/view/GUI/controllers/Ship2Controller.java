package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
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
import java.util.Objects;

public class Ship2Controller extends ShipController{

    private final int ROWS = 5;
    private final int COLS = 7;

    @FXML private ImageView imageCell_0_0;
    @FXML private ImageView imageCell_0_1;
    @FXML private ImageView imageCell_0_2;
    @FXML private ImageView imageCell_0_3;
    @FXML private ImageView imageCell_0_4;
    @FXML private ImageView imageCell_0_5;
    @FXML private ImageView imageCell_0_6;
    @FXML private ImageView imageCell_1_0;
    @FXML private ImageView imageCell_1_1;
    @FXML private ImageView imageCell_1_2;
    @FXML private ImageView imageCell_1_3;
    @FXML private ImageView imageCell_1_4;
    @FXML private ImageView imageCell_1_5;
    @FXML private ImageView imageCell_1_6;
    @FXML private ImageView imageCell_2_0;
    @FXML private ImageView imageCell_2_1;
    @FXML private ImageView imageCell_2_2;
    @FXML private ImageView imageCell_2_3;
    @FXML private ImageView imageCell_2_4;
    @FXML private ImageView imageCell_2_5;
    @FXML private ImageView imageCell_2_6;
    @FXML private ImageView imageCell_3_0;
    @FXML private ImageView imageCell_3_1;
    @FXML private ImageView imageCell_3_2;
    @FXML private ImageView imageCell_3_3;
    @FXML private ImageView imageCell_3_4;
    @FXML private ImageView imageCell_3_5;
    @FXML private ImageView imageCell_3_6;
    @FXML private ImageView imageCell_4_0;
    @FXML private ImageView imageCell_4_1;
    @FXML private ImageView imageCell_4_2;
    @FXML private ImageView imageCell_4_3;
    @FXML private ImageView imageCell_4_4;
    @FXML private ImageView imageCell_4_5;
    @FXML private ImageView imageCell_4_6;

    @FXML protected ImageView bgImage;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/fxml/cardboard/cardboard-1b.jpg")).toExternalForm());
        final double imageWidth = backgroundImage.getWidth();
        final double imageHeight = backgroundImage.getHeight();
        final double imageRatio = imageWidth / imageHeight;

        rootPane.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            bgImage.setFitWidth(newBounds.getWidth()*0.7);
            bgImage.setFitHeight(newBounds.getHeight()*0.7);
            double containerWidth = bgImage.getFitWidth();
            double containerHeight = bgImage.getFitHeight();
            double containerRatio = containerWidth / containerHeight;

            double actualWidth, actualHeight;

            if (imageRatio > containerRatio) {
                actualWidth = containerWidth;
                actualHeight = containerWidth / imageRatio;
            } else {
                actualHeight = containerHeight;
                actualWidth = containerHeight * imageRatio;
            }

            double gridWidth = actualWidth * (1 - 0.04 - 0.04);
            double gridHeight = actualHeight * (1 - 0.04 - 0.04);

            componentsGrid.setPrefSize(gridWidth, gridHeight);
            componentsGrid.setMaxSize(gridWidth, gridHeight);
            componentsGrid.setMinSize(gridWidth, gridHeight);

            // Debug output
            // System.out.println("Container: " + containerWidth + "x" + containerHeight);
            // System.out.println("Immagine effettiva: " + actualWidth + "x" + actualHeight);
            // System.out.println("Griglia: " + gridWidth + "x" + gridHeight);

            // Set the size of each ImageView to match the grid cell size
            double cellWidth = gridWidth / COLS;
            double cellHeight = gridHeight / ROWS;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    ImageView imageView = getImageViewAt(row, col);
                    if (imageView != null) {
                        imageView.setFitWidth(cellWidth);
                        imageView.setFitHeight(cellHeight);
                    }
                }
            }
        });
    }

    protected ImageView getImageViewAt(int row, int col) {
        return switch (row) {
            case 0 -> switch (col) {
                case 0 -> imageCell_0_0; case 1 -> imageCell_0_1; case 2 -> imageCell_0_2;
                case 3 -> imageCell_0_3; case 4 -> imageCell_0_4; case 5 -> imageCell_0_5;
                case 6 -> imageCell_0_6; default -> null;
            };
            case 1 -> switch (col) {
                case 0 -> imageCell_1_0; case 1 -> imageCell_1_1; case 2 -> imageCell_1_2;
                case 3 -> imageCell_1_3; case 4 -> imageCell_1_4; case 5 -> imageCell_1_5;
                case 6 -> imageCell_1_6; default -> null;
            };
            case 2 -> switch (col) {
                case 0 -> imageCell_2_0; case 1 -> imageCell_2_1; case 2 -> imageCell_2_2;
                case 3 -> imageCell_2_3; case 4 -> imageCell_2_4; case 5 -> imageCell_2_5;
                case 6 -> imageCell_2_6; default -> null;
            };
            case 3 -> switch (col) {
                case 0 -> imageCell_3_0; case 1 -> imageCell_3_1; case 2 -> imageCell_3_2;
                case 3 -> imageCell_3_3; case 4 -> imageCell_3_4; case 5 -> imageCell_3_5;
                case 6 -> imageCell_3_6; default -> null;
            };
            case 4 -> switch (col) {
                case 0 -> imageCell_4_0; case 1 -> imageCell_4_1; case 2 -> imageCell_4_2;
                case 3 -> imageCell_4_3; case 4 -> imageCell_4_4; case 5 -> imageCell_4_5;
                case 6 -> imageCell_4_6; default -> null;
            };
            default -> null;
        };
    }

    protected int getRows() {
        return ROWS;
    }

    protected int getCols() {
        return COLS;
    }

    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {
        // ignore
    }
}