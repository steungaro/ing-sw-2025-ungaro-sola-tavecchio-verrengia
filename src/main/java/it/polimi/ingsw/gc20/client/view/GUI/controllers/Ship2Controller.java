package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Controller class for the second ship type in the game.
 * Manages a 5x7 grid of cells representing the ship's layout and handles
 * its specific initialization, rendering, and validation rules.
 */
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

    /**
     * Initializes the controller after its root element has been completely processed.
     * Loads the background image and sets up responsive resizing for the ship grid.
     * This method extends the parent class initialization with specific layout for ship type 2.
     */
    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/fxml/cardboard/cardboard-1b.jpg")).toExternalForm());
        final double imageWidth = backgroundImage.getWidth();
        final double imageHeight = backgroundImage.getHeight();
        final double imageRatio = imageWidth / imageHeight;

        rootPane.layoutBoundsProperty().addListener((_, _, newBounds) -> {
            bgImage.setFitWidth(newBounds.getWidth()*0.95);
            bgImage.setFitHeight(newBounds.getHeight()*0.95);
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

    /**
     * Retrieves the ImageView at the specified grid position.
     * Maps row and column coordinates to the corresponding ImageView in the grid.
     *
     * @param row The row index (0-4)
     * @param col The column index (0-6)
     * @return The ImageView at the specified position, or null if invalid coordinates
     */
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

    /**
     * Returns the number of rows in the ship grid.
     *
     * @return The number of rows (5)
     */
    protected int getRows() {
        return ROWS;
    }

    /**
     * Returns the number of columns in the ship grid.
     *
     * @return The number of columns (7)
     */
    protected int getCols() {
        return COLS;
    }

    /**
     * Handles the current card update event from the game model.
     * This implementation ignores the event as it's not relevant for this controller.
     *
     * @param currentCard The updated adventure card view model
     */
    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {
        // ignore
    }

    /**
     * Cleans up resources used by this controller.
     * Extends the parent cleanup method with specific cleanup for Ship2Controller resources.
     * Removes listeners, clears images, and nullifies references to avoid memory leaks.
     */
    @Override
    public void cleanup() {

        super.cleanup();

        if (rootPane != null) {
            rootPane.layoutBoundsProperty().removeListener((_, _, newBounds) -> {
                bgImage.setFitWidth(newBounds.getWidth()*0.7);
                bgImage.setFitHeight(newBounds.getHeight()*0.7);
            });
        }

        if (bgImage != null) {
            bgImage.setImage(null);
            bgImage.setFitWidth(0);
            bgImage.setFitHeight(0);
        }

        imageCell_0_0 = null; imageCell_0_1 = null; imageCell_0_2 = null; imageCell_0_3 = null;
        imageCell_0_4 = null; imageCell_0_5 = null; imageCell_0_6 = null;
        imageCell_1_0 = null; imageCell_1_1 = null; imageCell_1_2 = null; imageCell_1_3 = null;
        imageCell_1_4 = null; imageCell_1_5 = null; imageCell_1_6 = null;
        imageCell_2_0 = null; imageCell_2_1 = null; imageCell_2_2 = null; imageCell_2_3 = null;
        imageCell_2_4 = null; imageCell_2_5 = null; imageCell_2_6 = null;
        imageCell_3_0 = null; imageCell_3_1 = null; imageCell_3_2 = null; imageCell_3_3 = null;
        imageCell_3_4 = null; imageCell_3_5 = null; imageCell_3_6 = null;
        imageCell_4_0 = null; imageCell_4_1 = null; imageCell_4_2 = null; imageCell_4_3 = null;
        imageCell_4_4 = null; imageCell_4_5 = null; imageCell_4_6 = null;
        bgImage = null;
    }

    /**
     * Checks if a cell at the specified position is valid for component placement.
     * Implements ship-specific layout rules determining where components can be placed.
     *
     * @param row The row index to check
     * @param col The column index to check
     * @return True if the position is valid for component placement, false otherwise
     */
    @Override
    protected boolean checkIsValid(int row,int col){
        if(row + col <= 1)
            return false;
        if(row == 0 && (col == 3 || col == 5 || col == 6))
            return false;
        if(row == 1 && col == 6)
            return false;
        return row != 4 || col != 3;
    }
}