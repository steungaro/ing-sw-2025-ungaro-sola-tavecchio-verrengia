package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.rmi.RemoteException;
import java.util.Objects;

public class BuildingPhaseController2 extends BuildingPhaseController {
    private final int ROWS = 5;
    private final int COLS = 7;

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
    private ImageView imageCell_0_5;
    @FXML
    private ImageView imageCell_0_6;
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
    private ImageView imageCell_1_5;
    @FXML
    private ImageView imageCell_1_6;
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
    private ImageView imageCell_2_5;
    @FXML
    private ImageView imageCell_2_6;
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
    private ImageView imageCell_3_5;
    @FXML
    private ImageView imageCell_3_6;
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
    @FXML
    private ImageView imageCell_4_5;
    @FXML
    private ImageView imageCell_4_6;
    @FXML
    private ImageView imageBooked_0;
    @FXML
    private ImageView imageBooked_1;

    /**
     * Initializes the building phase controller for ship type 2 and sets up the user interface.
     * This method extends the parent initialization and adds specific functionality for type 2 ships:
     * - Sets up the background image with cardboard-1.jpg
     * - Configures responsive layout that maintains aspect ratio
     * - Sets up layout bounds listeners for dynamic resizing
     * - Calculates and applies proper grid dimensions for the 5x7 ship grid
     * - Configures the booked components grid with proper positioning and sizing
     * - Sets individual cell sizes for all ImageView components in both grids
     * - Handles both the main components grid and the 2-slot booked components area
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

            double gridWidth = actualWidth * (1 - 0.04 - 0.04);
            double gridHeight = actualHeight * (1 - 0.04 - 0.04);
            double bookedGridWidth = actualWidth * 0.26;
            double bookedGridHeight = actualHeight * 0.18;

            double bookedGridX = actualWidth * 0.346;
            double bookedGridY = -actualHeight * 0.379;

            bookedGrid.setPrefSize(bookedGridWidth, bookedGridHeight);
            bookedGrid.setMaxSize(bookedGridWidth, bookedGridHeight);
            bookedGrid.setMinSize(bookedGridWidth, bookedGridHeight);

            bookedGrid.setTranslateX(bookedGridX);
            bookedGrid.setTranslateY(bookedGridY);

            componentsGrid.setPrefSize(gridWidth, gridHeight);
            componentsGrid.setMaxSize(gridWidth, gridHeight);
            componentsGrid.setMinSize(gridWidth, gridHeight);

            double bookedCellWidth = bookedGridWidth / 2;

            imageBooked_0.setFitWidth(bookedCellWidth);
            imageBooked_0.setFitHeight(bookedGridHeight);
            imageBooked_1.setFitWidth(bookedCellWidth);
            imageBooked_1.setFitHeight(bookedGridHeight);

            imageBooked_0.setPreserveRatio(false);
            imageBooked_1.setPreserveRatio(false);

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
                case 5 -> imageCell_0_5;
                case 6 -> imageCell_0_6;
                default -> null;
            };
            case 1 -> switch (col) {
                case 0 -> imageCell_1_0;
                case 1 -> imageCell_1_1;
                case 2 -> imageCell_1_2;
                case 3 -> imageCell_1_3;
                case 4 -> imageCell_1_4;
                case 5 -> imageCell_1_5;
                case 6 -> imageCell_1_6;
                default -> null;
            };
            case 2 -> switch (col) {
                case 0 -> imageCell_2_0;
                case 1 -> imageCell_2_1;
                case 2 -> imageCell_2_2;
                case 3 -> imageCell_2_3;
                case 4 -> imageCell_2_4;
                case 5 -> imageCell_2_5;
                case 6 -> imageCell_2_6;
                default -> null;
            };
            case 3 -> switch (col) {
                case 0 -> imageCell_3_0;
                case 1 -> imageCell_3_1;
                case 2 -> imageCell_3_2;
                case 3 -> imageCell_3_3;
                case 4 -> imageCell_3_4;
                case 5 -> imageCell_3_5;
                case 6 -> imageCell_3_6;
                default -> null;
            };
            case 4 -> switch (col) {
                case 0 -> imageCell_4_0;
                case 1 -> imageCell_4_1;
                case 2 -> imageCell_4_2;
                case 3 -> imageCell_4_3;
                case 4 -> imageCell_4_4;
                case 5 -> imageCell_4_5;
                case 6 -> imageCell_4_6;
                default -> null;
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
    protected void activatePlacementMode() {
        if (ClientGameModel.getInstance().getComponentInHand() != null) {
            placementModeActive = true;
            componentInHandPane.setStyle("-fx-border-color: green; -fx-border-width: 2;");
            uncoveredComponentsPane.setStyle("-fx-border-color: green; -fx-border-width: 2;");

            enableGridInteraction(this::handleCellClick);
            enableUncoveredComponentsInteraction(this::handleUncoveredClick);
            enableBookedComponentsInteraction(this::handleBookedClick);
        }
    }

    @Override
    protected void deactivatePlacementMode(){
        disableGridInteraction();
        disableUncoveredComponentsInteraction();
        disableBookedComponentsInteraction();
    }

    @Override
    protected void updateComponentInHand() {
        super.updateComponentInHand();
        if (ClientGameModel.getInstance().getComponentInHand() == null) {
            enableBookedToHandInteraction(this::handleBookedToHandClick);
        }
    }

    public interface BookedToHandClickHandler {
        void onBookedToHandClicked(int index);
    }

    /**
     * Enables interaction with the booked components area for transferring components to hand.
     * This method creates clickable areas over the booked component slots that allow players
     * to move booked components back to their hand. Only slots with actual components are
     * made interactive.
     * 
     * @param handler the handler to process booked-to-hand click events
     */
    public void enableBookedToHandInteraction(BookedToHandClickHandler handler) {
        if (bookedGrid != null) {

            bookedGrid.getChildren().removeIf(node ->
                    node.getClass().equals(javafx.scene.shape.Rectangle.class));

            for (int i = 0; i < 2; i++) {
                if(ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername()).getBooked(i) == null)
                    continue;
        
                Rectangle clickArea = new Rectangle();
                clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                clickArea.setStroke(javafx.scene.paint.Color.LIGHTGREEN);
                clickArea.setStrokeWidth(2);
                clickArea.setOpacity(0.7);

                ImageView imageView = i == 0 ? imageBooked_0 : imageBooked_1;
                if (imageView != null) {
                    clickArea.widthProperty().bind(
                        imageView.fitWidthProperty()
                    );
                    clickArea.heightProperty().bind(
                        imageView.fitHeightProperty()
                    );
                }

                final int index = i;
                clickArea.setOnMouseClicked(_ -> {
                    if (handler != null) {
                        handler.onBookedToHandClicked(index);
                    }
                });

                clickArea.setOnMouseEntered(_ -> {
                    clickArea.setFill(javafx.scene.paint.Color.color(0, 1, 0, 0.2));
                    clickArea.setCursor(javafx.scene.Cursor.HAND);
                });

                clickArea.setOnMouseExited(_ -> {
                    clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                    clickArea.setCursor(javafx.scene.Cursor.DEFAULT);
                });

                bookedGrid.add(clickArea, i, 0);
                GridPane.setHalignment(clickArea, javafx.geometry.HPos.CENTER);
                GridPane.setValignment(clickArea, javafx.geometry.VPos.CENTER);
                GridPane.setMargin(clickArea, new javafx.geometry.Insets(2));
            }
        } else {
            System.err.println("bookedComponentsPane is null. Cannot enable interaction.");
        }
    }

    protected void handleBookedToHandClick(int i) {
        if (i != 0 && i != 1) {
            return;
        }

        ViewComponent bookedComponent = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername()).getBooked(i);
        if (bookedComponent == null) {
            return;
        }
        try {
            ClientGameModel.getInstance().getClient().takeComponentFromBooked(ClientGameModel.getInstance().getUsername(), i);
            updateBookedComponents();
        } catch (RemoteException e){
            System.err.println("Error taking component from booked: " + e.getMessage());
        }
    }

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

    public interface BookedClickHandler {
        void onBookedClicked(int index);
    }

    /**
     * Enables interaction with the booked components area for component placement.
     * This method creates clickable areas over both booked component slots to allow
     * placing components from hand into the booked area during placement mode.
     * 
     * @param handler the handler to process booked area click events
     */
    public void enableBookedComponentsInteraction(BookedClickHandler handler) {
        addBookedClickArea(0, handler);
        addBookedClickArea(1, handler);
    }

    private void addBookedClickArea(int index, BookedClickHandler handler) {
        ImageView imageView = index == 0 ? imageBooked_0 : imageBooked_1;
        if (imageView == null) return;

        Rectangle clickArea = new Rectangle();
        clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
        clickArea.setStroke(javafx.scene.paint.Color.LIGHTGREEN);
        clickArea.setStrokeWidth(2);
        clickArea.setOpacity(0.7);

        clickArea.widthProperty().bind(
                imageView.fitWidthProperty().subtract(4)
        );
        clickArea.heightProperty().bind(
                imageView.fitHeightProperty().subtract(4)
        );

        clickArea.setOnMouseClicked(_ -> {
            if (handler != null) {
                handler.onBookedClicked(index);
            }
        });

        clickArea.setOnMouseEntered(_ -> {
            clickArea.setFill(javafx.scene.paint.Color.color(0, 1, 0, 0.2));
            clickArea.setCursor(javafx.scene.Cursor.HAND);
        });

        clickArea.setOnMouseExited(_ -> {
            clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
            clickArea.setCursor(javafx.scene.Cursor.DEFAULT);
        });

        bookedGrid.add(clickArea, index, 0);

        GridPane.setHalignment(clickArea, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(clickArea, javafx.geometry.VPos.CENTER);
        GridPane.setMargin(clickArea, new javafx.geometry.Insets(2));
    }

    @Override
    protected void updateBookedComponents() {
        imageBooked_0.setImage(null);
        imageBooked_1.setImage(null);

        ViewShip ship = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());
        if (ship != null && (ship.getBooked(0) != null || ship.getBooked(1) != null)) {
            disableBookedComponentsInteraction();
            enableBookedToHandInteraction(this::handleBookedToHandClick);
            for (int i = 0; i < 2; i++) {
                ViewComponent component = ship.getBooked(i);
                if (component != null) {
                    String imagePath = "/fxml/tiles/" + component.id + ".jpg";
                    try {
                        Image componentImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                        if (i == 0) {
                            imageBooked_0.setImage(componentImage);
                            imageBooked_0.setRotate(component.rotComp * 90);
                        } else {
                            imageBooked_1.setImage(componentImage);
                            imageBooked_1.setRotate(component.rotComp * 90);
                        }
                    } catch (Exception e) {
                        System.err.println("Error uploading image: " + e.getMessage());
                    }

                }
            }
        }
    }

    /**
     * Performs comprehensive cleanup operations specific to ship type 2 controller.
     * This method handles the following cleanup tasks:
     * - Removes layout bounds listener from the root pane
     * - Clears and resets the background image properties
     * - Resets main components grid dimensions to zero
     * - Clears and resets the booked components grid (position, size, children)
     * - Unbinds properties and removes event handlers from booked ImageViews
     * - Nullifies all 35 main grid ImageView cell references (imageCell_0_0 through imageCell_4_6)
     * - Nullifies both booked ImageView references (imageBooked_0, imageBooked_1)
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

        if (bookedGrid != null) {
            bookedGrid.getChildren().clear();
            bookedGrid.setPrefSize(0, 0);
            bookedGrid.setMaxSize(0, 0);
            bookedGrid.setMinSize(0, 0);
            bookedGrid.setTranslateX(0);
            bookedGrid.setTranslateY(0);
        }

        if (imageBooked_0 != null) {
            imageBooked_0.setImage(null);
            imageBooked_0.setFitWidth(0);
            imageBooked_0.setFitHeight(0);
            imageBooked_0.fitWidthProperty().unbind();
            imageBooked_0.fitHeightProperty().unbind();
            imageBooked_0.setOnMouseClicked(null);
            imageBooked_0.setOnMouseEntered(null);
            imageBooked_0.setOnMouseExited(null);
        }

        if (imageBooked_1 != null) {
            imageBooked_1.setImage(null);
            imageBooked_1.setFitWidth(0);
            imageBooked_1.setFitHeight(0);

            imageBooked_1.fitWidthProperty().unbind();
            imageBooked_1.fitHeightProperty().unbind();
            imageBooked_1.setOnMouseClicked(null);
            imageBooked_1.setOnMouseEntered(null);
            imageBooked_1.setOnMouseExited(null);
        }

        imageCell_0_0 = null;
        imageCell_0_1 = null;
        imageCell_0_2 = null;
        imageCell_0_3 = null;
        imageCell_0_4 = null;
        imageCell_0_5 = null;
        imageCell_0_6 = null;
        imageCell_1_0 = null;
        imageCell_1_1 = null;
        imageCell_1_2 = null;
        imageCell_1_3 = null;
        imageCell_1_4 = null;
        imageCell_1_5 = null;
        imageCell_1_6 = null;
        imageCell_2_0 = null;
        imageCell_2_1 = null;
        imageCell_2_2 = null;
        imageCell_2_3 = null;
        imageCell_2_4 = null;
        imageCell_2_5 = null;
        imageCell_2_6 = null;
        imageCell_3_0 = null;
        imageCell_3_1 = null;
        imageCell_3_2 = null;
        imageCell_3_3 = null;
        imageCell_3_4 = null;
        imageCell_3_5 = null;
        imageCell_3_6 = null;
        imageCell_4_0 = null;
        imageCell_4_1 = null;
        imageCell_4_2 = null;
        imageCell_4_3 = null;
        imageCell_4_4 = null;
        imageCell_4_5 = null;
        imageCell_4_6 = null;
        imageBooked_0 = null;
        imageBooked_1 = null;
    }
}