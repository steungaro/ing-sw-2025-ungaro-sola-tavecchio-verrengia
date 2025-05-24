package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;

public class Board0Controller {

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

    private List<Circle> circles = new ArrayList<>();
    private List<Label> circleLabels = new ArrayList<>();

    @FXML
    private void initialize() {
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
            label.setFont(new Font(16));
            label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

            label.layoutXProperty().bind(circle.layoutXProperty().subtract(8));
            label.layoutYProperty().bind(circle.layoutYProperty().subtract(12));

            circleLabels.add(label);

            circle.parentProperty().addListener((obs, oldParent, newParent) -> {
                if (newParent != null) {
                    ((Pane) newParent).getChildren().add(label);
                }
            });
        }
    }


    public void setNumberInCircle(int circleIndex, int number) {
        if (circleIndex >= 0 && circleIndex < circleLabels.size()) {
            circleLabels.get(circleIndex).setText(String.valueOf(number));
        }
    }
}