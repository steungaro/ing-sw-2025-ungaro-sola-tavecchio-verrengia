package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ship0Controller extends ShipController{

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

    private final int ROWS = 5;
    private final int COLS = 5;


    private ImageView getImageViewAt(int row, int col) {
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
}