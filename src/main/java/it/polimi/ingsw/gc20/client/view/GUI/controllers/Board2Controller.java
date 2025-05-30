package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

public class Board2Controller extends BoardController{

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
        circles.add(circle18);
        circles.add(circle19);
        circles.add(circle20);
        circles.add(circle21);
        circles.add(circle22);
        circles.add(circle23);

        for (Circle circle : circles) {
            Label label = new Label();
            label.setFont(new Font(16.0));
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

        ClientGameModel clientGameModel = ClientGameModel.getInstance();
        if (clientGameModel != null) {
            String currentUsername = clientGameModel.getUsername();
            List<ViewPlayer> players = clientGameModel.getPlayers();
            if (players != null && currentUsername != null) {
                Optional<ViewPlayer> currentPlayerOpt = players.stream()
                        .filter(p -> currentUsername.equals(p.username))
                        .findFirst();
                currentPlayerOpt.ifPresent(this::updateStatisticBoard);
            }
        }
    }

}