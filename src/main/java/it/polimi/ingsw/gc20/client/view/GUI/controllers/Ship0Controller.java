package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Controller class for the beginner ship (type 0) in the game.
 * Manages a 5x5 grid of cells representing the ship's layout and handles
 * its specific initialization, rendering, and validation rules.
 * This ship is designed for learner players with a simpler layout.
 */
public class Ship0Controller extends ShipController{

    private final int ROWS = 5;
    private final int COLS = 5;

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

    @FXML protected ImageView bgImage;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Loads the background image and sets up responsive resizing for the ship grid.
     * This method extends the parent class initialization with specific layout for the beginner ship.
     */
    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/fxml/cardboard/cardboard-1.jpg")).toExternalForm());
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

            double gridWidth = actualWidth * (1 - 0.167 - 0.167);
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
     * @param col The column index (0-4)
     * @return The ImageView at the specified position, or null if invalid coordinates
     */
    protected ImageView getImageViewAt(int row, int col) {
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
     * Adds a component to the ship grid at the specified position.
     * This implementation adjusts the column index to account for the 
     * beginner ship's specific layout requirements.
     *
     * @param comp The component to add
     * @param row The row position on the grid
     * @param col The column position on the grid
     * @return True if the component was successfully added, false otherwise
     */
    @Override
    public boolean addComponent(ViewComponent comp, int row, int col) {
        return super.addComponent(comp, row, col-1);
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
     * @return The number of columns (5)
     */
    protected int getCols() {
        return COLS;
    }

    /**
     * Handles the current card update event from the game model.
     * This implementation is empty as it's not relevant for this controller.
     *
     * @param currentCard The updated adventure card view model
     */
    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {

    }

    /**
     * Cleans up resources used by this controller.
     * Extends the parent cleanup method with specific cleanup for Ship0Controller resources.
     * Removes listeners, clears images, and nullifies references to avoid memory leaks.
     */
    @Override
    public void cleanup() {
        System.out.println("Ship0Controller: Starting cleanup...");

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

        imageCell_0_0 = null; imageCell_0_1 = null; imageCell_0_2 = null; imageCell_0_3 = null; imageCell_0_4 = null;
        imageCell_1_0 = null; imageCell_1_1 = null; imageCell_1_2 = null; imageCell_1_3 = null; imageCell_1_4 = null;
        imageCell_2_0 = null; imageCell_2_1 = null; imageCell_2_2 = null; imageCell_2_3 = null; imageCell_2_4 = null;
        imageCell_3_0 = null; imageCell_3_1 = null; imageCell_3_2 = null; imageCell_3_3 = null; imageCell_3_4 = null;
        imageCell_4_0 = null; imageCell_4_1 = null; imageCell_4_2 = null; imageCell_4_3 = null; imageCell_4_4 = null;
        bgImage = null;
    }

    /**
     * Checks if a cell at the specified position is valid for component placement.
     * Implements ship-specific layout rules determining where components can be placed.
     * The beginner ship has a simplified valid placement pattern.
     *
     * @param row The row index to check
     * @param col The column index to check
     * @return True if the position is valid for component placement, false otherwise
     */
    @Override
    protected boolean checkIsValid(int row,int col){
        if(row == 0 && col!=2)
            return false;
        if(row == 1 && (col == 0 || col == 4))
            return false;
        return row != 4 || col != 2;
    }
}