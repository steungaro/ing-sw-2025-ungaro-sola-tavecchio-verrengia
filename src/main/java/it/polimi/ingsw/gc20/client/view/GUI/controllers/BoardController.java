package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
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
import java.util.Map;
import java.util.Optional;

import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

public abstract class BoardController {

    protected List<Circle> circles = new ArrayList<>();
    protected List<Label> circleLabels = new ArrayList<>();

    public void setPlayerPosition(int circleIndex, PlayerColor playerColor) {
        if (circleIndex >= 0 && circleIndex < circles.size()) {
            Circle circle = circles.get(circleIndex);
            if (circle != null && playerColor != null) {
                Color fxColor = null;
                switch (playerColor) {
                    case RED:
                        fxColor = Color.RED;
                        break;
                    case BLUE:
                        fxColor = Color.BLUE;
                        break;
                    case GREEN:
                        fxColor = Color.GREEN;
                        break;
                    case YELLOW:
                        fxColor = Color.YELLOW;
                        break;}
                if (fxColor != null) {
                    circle.setFill(fxColor);
                }
            }
        }
    }

    public void clearPosition(int circleIndex) {
        if (circleIndex >= 0 && circleIndex < circles.size()) {
            Circle circle = circles.get(circleIndex);
            if (circle != null) {
                circle.setFill(Color.TRANSPARENT);
            }
            if (circleIndex >= 0 && circleIndex < circleLabels.size()) {
                circleLabels.get(circleIndex).setText("");
            }
        }
    }

    public void setNumberInCircle(int circleIndex, int number) {
        if (circleIndex >= 0 && circleIndex < circleLabels.size()) {
            circleLabels.get(circleIndex).setText(String.valueOf(number));
        }
    }

    public void updateBoardDisplay(ViewBoard newBoard) {
        if (newBoard == null) {
            System.err.println("BoardController: updateBoardDisplay chiamato con newBoard nullo.");
            return;
        }

        for (int i = 0; i < Math.max(circles.size(), circleLabels.size()); i++) {
            clearPosition(i);
        }

        /*Map<Integer, List<PlayerColor>> playerPositions = newBoard.getPlayerPositions();
        if (playerPositions != null) {
            playerPositions.forEach((positionIndex, colors) -> {
                if (colors != null && !colors.isEmpty()) {
                    setPlayerPosition(positionIndex, colors.getFirst());
                }
            });
        }

        Map<Integer, Integer> numbersOnBoard = newBoard.getNumbersOnBoard();
        if (numbersOnBoard != null) {
            numbersOnBoard.forEach(this::setNumberInCircle);
        }
        System.out.println("BoardController: Display updated");*/
    }
}
