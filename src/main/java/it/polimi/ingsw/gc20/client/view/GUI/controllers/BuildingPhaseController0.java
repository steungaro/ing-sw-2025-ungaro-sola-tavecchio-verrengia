package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class BuildingPhaseController0 extends BuildingPhaseController {
    private final int ROWS = 5;
    private final int COLS = 5;

    @FXML
    private ImageView imageCell_0_0;
    @FXML
    private ImageView imageCell_0_1;
    @FXML
    private ImageView imageCell_0_2;
    @FXML
    private ImageView imageCell_0_3;
    @FXML
    private ImageView imageCell_0_4;
    @FXML
    private ImageView imageCell_1_0;
    @FXML
    private ImageView imageCell_1_1;
    @FXML
    private ImageView imageCell_1_2;
    @FXML
    private ImageView imageCell_1_3;
    @FXML
    private ImageView imageCell_1_4;
    @FXML
    private ImageView imageCell_2_0;
    @FXML
    private ImageView imageCell_2_1;
    @FXML
    private ImageView imageCell_2_2;
    @FXML
    private ImageView imageCell_2_3;
    @FXML
    private ImageView imageCell_2_4;
    @FXML
    private ImageView imageCell_3_0;
    @FXML
    private ImageView imageCell_3_1;
    @FXML
    private ImageView imageCell_3_2;
    @FXML
    private ImageView imageCell_3_3;
    @FXML
    private ImageView imageCell_3_4;
    @FXML
    private ImageView imageCell_4_0;
    @FXML
    private ImageView imageCell_4_1;
    @FXML
    private ImageView imageCell_4_2;
    @FXML
    private ImageView imageCell_4_3;
    @FXML
    private ImageView imageCell_4_4;

    /**
     * Initializes the building phase controller for ship type 0 and sets up the user interface.
     * This method extends the parent initialization and adds specific functionality for type 0 ships:
     * - Sets up the background image with cardboard-1.jpg
     * - Configures responsive layout that maintains aspect ratio
     * - Sets up layout bounds listeners for dynamic resizing
     * - Calculates and applies proper grid dimensions based on background image proportions
     * - Sets individual cell sizes for all ImageView components in the 5x5 grid
     */
    @Override
    public void initialize() {
        super.initialize();
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/fxml/cardboard/cardboard-1.jpg")).toExternalForm());
        final double imageWidth = backgroundImage.getWidth();
        final double imageHeight = backgroundImage.getHeight();
        final double imageRatio = imageWidth / imageHeight;

        rootPane.layoutBoundsProperty().addListener((_, _, newBounds) -> {
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
     * Adds a component to the ship grid at the specified position with column offset adjustment.
     * This method overrides the parent implementation to apply a column offset specific to ship type 0,
     * adjusting the column position by subtracting 1 before placement.
     * 
     * @param comp the component to add to the ship
     * @param row the row position (0-based)
     * @param col the column position (0-based, will be adjusted by -1)
     * @return true if the component was successfully added, false otherwise
     */
    @Override
    public boolean addComponent(ViewComponent comp, int row, int col) {
        return super.addComponent(comp, row, col-1);
    }

    protected int getRows() {
        return ROWS;
    }

    protected int getCols() {
        return COLS;
    }

    @Override
    protected void updateBookedComponents() {
        // No need to update booked components in this controller
    }

    @Override
    protected boolean checkIsValid(int row,int col){
        if(row == 0 && col!=2)
            return false;
        if(row == 1 && (col == 0 || col == 4))
            return false;
        return row != 4 || col != 2;
    }

    /**
     * Builds and displays all ship components on the grid for ship type 0.
     * This method overrides the parent implementation to handle the extended column range
     * (COLS+1) specific to ship type 0 layout requirements.
     * 
     * @param ship the ship view containing the components to display
     */
    @Override
    public void buildShipComponents(ViewShip ship) {
        if (ship == null || componentsGrid == null) return;

        clearAllComponents();

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols()+1; col++) {
                ViewComponent comp = ship.getComponent(row, col);
                if (comp != null) {
                    addComponent(comp, row, col);
                }
            }
        }
    }


    /**
     * Performs comprehensive cleanup operations specific to ship type 0 controller.
     * This method handles the following cleanup tasks:
     * - Removes layout bounds listener from the root pane
     * - Clears and resets the background image properties
     * - Resets components grid dimensions to zero
     * - Nullifies all 25 ImageView cell references (imageCell_0_0 through imageCell_4_4)
     * - Provides error handling for listener removal operations
     */
    @Override
    public void cleanup() {
        if (rootPane != null) {
            try {
                rootPane.layoutBoundsProperty().removeListener((_, _, _) -> {});
            } catch (Exception e) {
                System.err.println("Error removing rootPane listener: " + e.getMessage());
            }
        }

        if (bgImage != null) {
            bgImage.setImage(null);
            bgImage.setFitWidth(0);
            bgImage.setFitHeight(0);
        }

        if (componentsGrid != null) {
            componentsGrid.setPrefSize(0, 0);
            componentsGrid.setMaxSize(0, 0);
            componentsGrid.setMinSize(0, 0);
        }

        imageCell_0_0 = null;
        imageCell_0_1 = null;
        imageCell_0_2 = null;
        imageCell_0_3 = null;
        imageCell_0_4 = null;
        imageCell_1_0 = null;
        imageCell_1_1 = null;
        imageCell_1_2 = null;
        imageCell_1_3 = null;
        imageCell_1_4 = null;
        imageCell_2_0 = null;
        imageCell_2_1 = null;
        imageCell_2_2 = null;
        imageCell_2_3 = null;
        imageCell_2_4 = null;
        imageCell_3_0 = null;
        imageCell_3_1 = null;
        imageCell_3_2 = null;
        imageCell_3_3 = null;
        imageCell_3_4 = null;
        imageCell_4_0 = null;
        imageCell_4_1 = null;
        imageCell_4_2 = null;
        imageCell_4_3 = null;
        imageCell_4_4 = null;
    }
}