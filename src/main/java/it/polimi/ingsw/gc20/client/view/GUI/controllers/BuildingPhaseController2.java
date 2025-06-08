package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.application.Platform;
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
    private GridPane bookedGrid;
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

    @Override
    public void initialize() {
        super.initialize();
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/fxml/cardboard/cardboard-1.jpg")).toExternalForm());
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
            double bookedGridWidth = actualWidth * 0.26;
            double bookedGridHeight = actualHeight * 0.18;

            double centerX = actualWidth / 2;
            double centerY = actualHeight / 2;
            double bookedGridX = centerX + actualWidth * 0.346 - bookedGridWidth / 2;
            double bookedGridY = centerY - actualHeight * 0.379 - bookedGridHeight / 2;

            bookedGrid.setPrefSize(bookedGridWidth, bookedGridHeight);
            bookedGrid.setMaxSize(bookedGridWidth, bookedGridHeight);
            bookedGrid.setMinSize(bookedGridWidth, bookedGridHeight);

            bookedGrid.setLayoutX(bookedGridX);
            bookedGrid.setLayoutY(bookedGridY);

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
        } else {
            System.out.println("No component in hand to place");
        }
    }

    public void enableBookedComponentsInteraction(Runnable handler) {
        if (bookedGrid != null) {
            uncoveredComponentsPane.setOnMouseClicked(_ -> {
                if (handler != null) {
                    handler.run();
                }
            });
            System.out.println("booked components interaction enabled.");

            for(int i = 0; i < 2; i++){
                Rectangle clickArea = new Rectangle();
                clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                clickArea.setStroke(javafx.scene.paint.Color.LIGHTGREEN);
                clickArea.setStrokeWidth(2);
                clickArea.setOpacity(0.7);

                clickArea.widthProperty().bind(
                        bookedGrid.widthProperty().divide(getCols()).subtract(2)
                );
                clickArea.heightProperty().bind(
                        componentsGrid.heightProperty().divide(getRows()).subtract(1)
                );

                clickArea.setOnMouseClicked(_ -> {
                    if (handler != null) {
                        handler.run();
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
            }
        } else {
            System.err.println("bookedComponentsPane is null. Cannot enable interaction.");
        }
    }

    // Handle click on booked components (no component in hand)
    // TODO
    protected void handleBookedtoHandClick(int i) {
        if (i != 0 && i != 1) {
            return;
        }

        ViewComponent bookedComponent = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername()).getBooked(i);
        if (bookedComponent == null) {
            return;
        }
        try {
            ClientGameModel.getInstance().getClient().takeComponentFromBooked(ClientGameModel.getInstance().getUsername(), i);
        } catch (RemoteException e){
            e.printStackTrace();
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
        if (row == 4 && col == 3)
            return false;
        return true;
    }

    public interface BookedClickHandler {
        void onBookedClicked(int index);
    }

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
                bookedGrid.widthProperty().divide(2).subtract(2)
        );
        clickArea.heightProperty().bind(
                bookedGrid.heightProperty().subtract(2)
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
    }

    private void handleBookedClick(int index) {
        String username = ClientGameModel.getInstance().getUsername();
        try {
            ClientGameModel.getInstance().getClient().addComponentToBooked(username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShipUpdated(ViewShip ship) {
        super.onShipUpdated(ship);
        ViewShip playerShip = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());

        if (playerShip == null) return;

        if (playerShip.getBooked(0) != null) {
            addComponentToImageView(imageBooked_0, playerShip.getBooked(0));
        } else {
            imageBooked_0.setImage(null);
        }

        if (playerShip.getBooked(1) != null) {
            addComponentToImageView(imageBooked_1, playerShip.getBooked(1));
        } else {
            imageBooked_1.setImage(null);
        }
    }

    private void addComponentToImageView(ImageView imageView, ViewComponent component) {
        if (component == null || imageView == null) return;

        String imagePath = "/fxml/tiles/" + component.id + ".jpg";
        try {
            javafx.scene.image.Image componentImage = new javafx.scene.image.Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            imageView.setImage(componentImage);
            imageView.setRotate(component.rotComp * 90);
        } catch (Exception e) {
            System.err.println("Error uploading component img: " + e.getMessage());
        }
    }

    private void updateBookedComponents() {
        imageBooked_0.setImage(null);
        imageBooked_1.setImage(null);

        ViewShip ship = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());
        if (ship != null && (ship.getBooked(0) != null || ship.getBooked(1) != null)) {
            for (int i = 0; i < 2; i++) {
                ViewComponent component = ship.getBooked(i);
                if (component != null) {
                    String imagePath = "/fxml/tiles/" + component.id + ".jpg";
                    try {
                        Image componentImage = new Image(getClass().getResourceAsStream(imagePath));
                        if (i == 0) {
                            imageBooked_0.setImage(componentImage);
                        } else if (i == 1) {
                            imageBooked_1.setImage(componentImage);
                        }
                    } catch (Exception e) {
                        System.err.println("Error uploading image: " + e.getMessage());
                    }

                }
            }
        }
    }
}
