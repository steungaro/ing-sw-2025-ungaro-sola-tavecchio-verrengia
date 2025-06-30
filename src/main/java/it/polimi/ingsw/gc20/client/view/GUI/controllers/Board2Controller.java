package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;

public class Board2Controller extends BoardController {

    @FXML private Group scalableContent;

    @FXML private Circle circle0;
    @FXML private Circle circle1;
    @FXML private Circle circle2;
    @FXML private Circle circle3;
    @FXML private Circle circle4;
    @FXML private Circle circle5;
    @FXML private Circle circle6;
    @FXML private Circle circle7;
    @FXML private Circle circle8;
    @FXML private Circle circle9;
    @FXML private Circle circle10;
    @FXML private Circle circle11;
    @FXML private Circle circle12;
    @FXML private Circle circle13;
    @FXML private Circle circle14;
    @FXML private Circle circle15;
    @FXML private Circle circle16;
    @FXML private Circle circle17;
    @FXML private Circle circle18;
    @FXML private Circle circle19;
    @FXML private Circle circle20;
    @FXML private Circle circle21;
    @FXML private Circle circle22;
    @FXML private Circle circle23;

    /**
     * Initializes the JavaFX components and sets up the board controller for type 2 board.
     * This method is automatically called by JavaFX after loading the FXML file.
     * It performs the following operations:
     * - Calls the parent class initialization
     * - Adds all 24 circles (circle0 to circle23) to the circles collection
     * - Creates and binds labels for each circle with proper positioning and styling
     * - Sets up parent property listeners with error handling for type casting
     * - Configures responsive scaling and positioning listeners for window resizing
     * - Sets up automatic layout updates when the window is shown or resized
     */
    @FXML
    public void initialize() {
        super.initialize();

        circles.add(circle0);
        circles.add(circle1);
        circles.add(circle2);
        circles.add(circle3);
        circles.add(circle4);
        circles.add(circle5);
        circles.add(circle6);
        circles.add(circle7);
        circles.add(circle8);
        circles.add(circle9);
        circles.add(circle10);
        circles.add(circle11);
        circles.add(circle12);
        circles.add(circle13);
        circles.add(circle14);
        circles.add(circle15);
        circles.add(circle16);
        circles.add(circle17);
        circles.add(circle18);
        circles.add(circle19);
        circles.add(circle20);
        circles.add(circle21);
        circles.add(circle22);
        circles.add(circle23);

        for (Circle circle : circles) {
            Label label = new Label();
            label.setFont(new Font(8));
            label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

            label.layoutXProperty().bind(circle.layoutXProperty().subtract(label.widthProperty().divide(2)).add(circle.getRadius() / 2 - 4));
            label.layoutYProperty().bind(circle.layoutYProperty().subtract(label.heightProperty().divide(2)).add(circle.getRadius() / 2 - 6));

            circleLabels.add(label);

            circle.parentProperty().addListener((_, _, newParent) -> {
                try{
                    ((Group) newParent).getChildren().add(label);
                } catch (ClassCastException e) {
                    System.err.println("Error casting newParent to Group: " + e.getMessage());
                }
            });
            if (scalableContent != null && circle.getParent() == scalableContent) {
                scalableContent.getChildren().add(label);
            }
        }

        Runnable updateOperation = this::updateScaleAndPosition;

        rootPane.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((_, _, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.showingProperty().addListener((_, _, showing) -> {
                            if (showing) {
                                Platform.runLater(updateOperation);
                            }
                        });
                    }
                });
            }
        });

        rootPane.widthProperty().addListener((_, _, _) -> updateOperation.run());
        rootPane.heightProperty().addListener((_, _, _) -> updateOperation.run());
    }

    private void updateScaleAndPosition() {
        double newPaneWidth = rootPane.getWidth();
        double newPaneHeight = rootPane.getHeight();

        if (scalableContent == null || newPaneWidth <= 0 || newPaneHeight <= 0) {
            return;
        }

        double originalContentWidth = 270.0;
        double originalContentHeight = 150.0;
        
        double maxAllowedWidth = Math.min(newPaneWidth, 300.0);
        double maxAllowedHeight = Math.min(newPaneHeight, 180.0);
        
        double scaleX = maxAllowedWidth / originalContentWidth;
        double scaleY = maxAllowedHeight / originalContentHeight;
        double scaleFactor = Math.min(scaleX, scaleY);
        
        scaleFactor = Math.min(scaleFactor, 1.0);

        if (Double.isInfinite(scaleFactor) || Double.isNaN(scaleFactor) || scaleFactor <= 0) {
            scaleFactor = 1.0;
        }

        scalableContent.getTransforms().clear();
        Scale scaleTransform = new Scale(scaleFactor, scaleFactor, 0, 0);
        scalableContent.getTransforms().add(scaleTransform);

        double scaledContentActualWidth = originalContentWidth * scaleFactor;
        double scaledContentActualHeight = originalContentHeight * scaleFactor;

        double offsetX = (newPaneWidth - scaledContentActualWidth) / 2;
        double offsetY = (newPaneHeight - scaledContentActualHeight) / 2;

        scalableContent.setLayoutX(Math.max(0, offsetX));
        scalableContent.setLayoutY(Math.max(0, offsetY));
    }

    /**
     * Performs comprehensive cleanup operations for this board controller to prevent memory leaks.
     * This method handles the following cleanup tasks:
     * - Removes this controller as a listener from the game model
     * - Unbinds layout properties from all circle labels and removes their event handlers
     * - Removes event handlers from all circles and clears parent property listeners
     * - Removes property listeners from the root pane (scene, width, height)
     * - Clears transforms and children from scalable content and resets its position
     * - Nullifies all circle references (circle0 through circle23)
     * - Nullifies root pane and scalable content references
     * 
     * Note: This implementation handles 24 circles compared to Board0Controller's 18 circles.
     */
    @Override
    public void cleanup() {
        ClientGameModel gameModel = ClientGameModel.getInstance();
        if (gameModel != null) {
            gameModel.removeListener(this);
        }

        if (circleLabels != null) {
            for (Label label : circleLabels) {
                if (label != null) {
                    label.layoutXProperty().unbind();
                    label.layoutYProperty().unbind();
                    label.setOnMouseClicked(null);
                    label.setOnMouseEntered(null);
                    label.setOnMouseExited(null);
                }
            }
            circleLabels.clear();
        }

        if (circles != null) {
            for (Circle circle : circles) {
                if (circle != null) {
                    circle.setOnMouseClicked(null);
                    circle.setOnMouseEntered(null);
                    circle.setOnMouseExited(null);
                    try {
                        circle.parentProperty().removeListener((_, _, _) -> {});
                    } catch (Exception e) {
                        System.err.println("Error removing parent listener: " + e.getMessage());
                    }
                }
            }
            circles.clear();
        }

        if (rootPane != null) {
            try {
                rootPane.sceneProperty().removeListener((_, _, _) -> {});
                rootPane.widthProperty().removeListener((_, _, _) -> {});
                rootPane.heightProperty().removeListener((_, _, _) -> {});
            } catch (Exception e) {
                System.err.println("Error removing rootPane listeners: " + e.getMessage());
            }
        }

        if (scalableContent != null) {
            scalableContent.getTransforms().clear();
            scalableContent.getChildren().clear();
            scalableContent.setLayoutX(0);
            scalableContent.setLayoutY(0);
        }

        circle0 = null;
        circle1 = null;
        circle2 = null;
        circle3 = null;
        circle4 = null;
        circle5 = null;
        circle6 = null;
        circle7 = null;
        circle8 = null;
        circle9 = null;
        circle10 = null;
        circle11 = null;
        circle12 = null;
        circle13 = null;
        circle14 = null;
        circle15 = null;
        circle16 = null;
        circle17 = null;
        circle18 = null;
        circle19 = null;
        circle20 = null;
        circle21 = null;
        circle22 = null;
        circle23 = null;

        rootPane = null;
        scalableContent = null;
    }
}