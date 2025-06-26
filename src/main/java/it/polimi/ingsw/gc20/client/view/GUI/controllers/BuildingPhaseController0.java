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


    @Override
    public void cleanup() {
        System.out.println("BuildingPhaseController0: Starting cleanup...");

        if (rootPane != null) {
            try {
                rootPane.layoutBoundsProperty().removeListener((_, _, newBounds) -> {});
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

        System.out.println("BuildingPhaseController0: Cleanup completed");
    }
}
