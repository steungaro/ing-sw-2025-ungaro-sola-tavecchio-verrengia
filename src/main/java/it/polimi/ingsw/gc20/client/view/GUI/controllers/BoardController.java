package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

public abstract class BoardController {

    protected List<Circle> circles = new ArrayList<>();
    protected List<Label> circleLabels = new ArrayList<>();

    @FXML protected Label playersInfoLabel;
    @FXML protected Label gameStateLabel;
    @FXML protected Label remainingTimeLabel;

    @FXML
    public void initialize() {
        if (playersInfoLabel != null) {
            playersInfoLabel.setText("Players: N/A");
        }
        if (gameStateLabel != null) {
            gameStateLabel.setText("Game State: N/A");
        }
        if (remainingTimeLabel != null) {
            remainingTimeLabel.setText("Remaining Time: N/A");
        }

        updateBoardDisplay(ClientGameModel.getInstance().getBoard());
    }

    public void setPlayerPosition(int circleIndex, PlayerColor playerColor) {
        if (circleIndex >= 0 && circleIndex < circles.size()) {
            Circle circle = circles.get(circleIndex);
            if (circle != null && playerColor != null) {
                Color fxColor = switch (playerColor) {
                    case RED -> Color.RED;
                    case BLUE -> Color.BLUE;
                    case GREEN -> Color.GREEN;
                    case YELLOW -> Color.YELLOW;
                };
                circle.setFill(fxColor);
            }
        }
    }

    public void clearPosition(int circleIndex) {
        if (circleIndex >= 0 && circleIndex < circles.size()) {
            Circle circle = circles.get(circleIndex);
            if (circle != null) {
                circle.setFill(Color.TRANSPARENT);
            }
        }
        if (circleIndex >= 0 && circleIndex < circleLabels.size()) {
            Label label = circleLabels.get(circleIndex);
            if (label != null) {
                label.setText("");
            }
        }
    }

    public void setNumberInCircle(int circleIndex, int number) {
        if (circleIndex >= 0 && circleIndex < circleLabels.size()) {
            Label label = circleLabels.get(circleIndex);
            if (label != null) {
                label.setText(String.valueOf(number));
            }
        }
    }

    public void updateBoardDisplay(ViewBoard newBoard) {
        if (newBoard == null) {
            System.err.println("BoardController: updateBoardDisplay with null newBoard");
            if (playersInfoLabel != null) playersInfoLabel.setText("Players: N/A");
            if (gameStateLabel != null) gameStateLabel.setText("Game State: N/A");
            if (remainingTimeLabel != null) remainingTimeLabel.setText("");
            return;
        }

        for (int i = 0; i < Math.max(circles.size(), circleLabels.size()); i++) {
            clearPosition(i);
        }

        ViewPlayer[] playerPositions = newBoard.players;
        for(ViewPlayer player : playerPositions) {
            if (player != null && player.position >= 0 && player.position < circles.size()) {
                setPlayerPosition(player.position, player.playerColor);
                setNumberInCircle(player.position, player.credits);
                if (circleLabels.size() > player.position) {
                    circleLabels.get(player.position).setText(player.username);
                }
            }
        }

        if (playersInfoLabel != null) {
            if (newBoard.players != null && newBoard.players.length > 0) {
                String playersText = Arrays.stream(newBoard.players)
                        .filter(p -> p != null && p.username != null)
                        .map(p -> p.username + (p.position==1 ? " (Leader)" : ""))
                        .collect(Collectors.joining("\n"));
                playersInfoLabel.setText("Players:\n" + playersText);
            } else {
                playersInfoLabel.setText("Players: N/A");
            }
        }

        if (gameStateLabel != null) {
            gameStateLabel.setText((newBoard.assemblingState ? "Assembling" : "Adventure"));
        }

        if (remainingTimeLabel != null) {
            if (newBoard.timeStampOfLastHourglassRotation > 0) {
                remainingTimeLabel.setText("Remaining Time: " + newBoard.timeStampOfLastHourglassRotation);
                remainingTimeLabel.setVisible(true);
            } else {
                remainingTimeLabel.setText("");
                remainingTimeLabel.setVisible(false);
            }
        }
    }
}
