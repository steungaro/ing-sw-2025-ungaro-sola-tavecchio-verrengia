package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;

public class Board0Controller extends BoardController {

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

        for (Circle circle : circles) {
            Label label = new Label();
            label.setFont(new Font(8));
            label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

            label.layoutXProperty().bind(circle.layoutXProperty().subtract(label.widthProperty().divide(2)).add(circle.getRadius() / 2 - 4));
            label.layoutYProperty().bind(circle.layoutYProperty().subtract(label.heightProperty().divide(2)).add(circle.getRadius() / 2 - 6));

            circleLabels.add(label);

            circle.parentProperty().addListener((obs, oldParent, newParent) -> {
                ((Group) newParent).getChildren().add(label);
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

    @Override
    public void cleanup() {
        System.out.println("Board0Controller: Starting cleanup...");

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
                        circle.parentProperty().removeListener((obs, oldParent, newParent) -> {});
                    } catch (Exception e) {
                        System.err.println("Error removing parent listener: " + e.getMessage());
                    }
                }
            }
            circles.clear();
        }

        if (rootPane != null) {
            try {
                rootPane.sceneProperty().removeListener((obs, oldScene, newScene) -> {});
                rootPane.widthProperty().removeListener((obs, oldVal, newVal) -> {});
                rootPane.heightProperty().removeListener((obs, oldVal, newVal) -> {});
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

        rootPane = null;
        scalableContent = null;

        System.out.println("Board0Controller: Cleanup completed");
    }
}