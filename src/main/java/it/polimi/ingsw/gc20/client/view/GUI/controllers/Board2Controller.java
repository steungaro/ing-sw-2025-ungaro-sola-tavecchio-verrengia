package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;

public class Board2Controller extends BoardController {

    @FXML private Pane rootPane;
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

    private final double originalContentWidth = 800.0; // original width
    private final double originalContentHeight = 330.0; // original height

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
            label.setFont(new Font(16));
            label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

            label.layoutXProperty().bind(circle.layoutXProperty().subtract(label.widthProperty().divide(2)).add(circle.getRadius() / 2 - 8));
            label.layoutYProperty().bind(circle.layoutYProperty().subtract(label.heightProperty().divide(2)).add(circle.getRadius() / 2 - 12));

            circleLabels.add(label);

            circle.parentProperty().addListener((obs, oldParent, newParent) -> {
                try{
                    ((Group) newParent).getChildren().add(label);
                } catch (ClassCastException e) {
                    System.err.println("Error: Parent of circle is not a Group. Cannot add label.");
                }
            });
            if (scalableContent != null && circle.getParent() == scalableContent) {
                scalableContent.getChildren().add(label);
            }
        }

        Runnable updateOperation = this::updateScaleAndPosition;

        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWindow, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.showingProperty().addListener((obsShowing, oldShowing, showing) -> {
                            if (showing) {
                                Platform.runLater(updateOperation);
                            }
                        });
                    }
                });
            }
        });

        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> updateOperation.run());
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> updateOperation.run());
    }

    private void updateScaleAndPosition() {
        double newPaneWidth = rootPane.getWidth();
        double newPaneHeight = rootPane.getHeight();

        if (scalableContent == null || newPaneWidth <= 0 || newPaneHeight <= 0) {
            return;
        }

        double originalContentWidth = 800.0;
        double originalContentHeight = 330.0;
        double scaleX = newPaneWidth / originalContentWidth;
        double scaleY = newPaneHeight / originalContentHeight;
        double scaleFactor = Math.min(scaleX, scaleY);

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

        scalableContent.setLayoutX(offsetX);
        scalableContent.setLayoutY(offsetY);
    }
}