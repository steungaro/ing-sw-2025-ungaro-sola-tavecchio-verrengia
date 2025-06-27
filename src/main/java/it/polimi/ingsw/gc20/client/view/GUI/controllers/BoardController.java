package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.*;

import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

public abstract class BoardController implements GameModelListener, BindCleanUp {

    protected List<Circle> circles = new ArrayList<>();
    protected List<Label> circleLabels = new ArrayList<>();

    @FXML
    public void initialize() {
        updateBoardDisplay(ClientGameModel.getInstance().getBoard());
    }

    public void setPlayerPosition(int circleIndex, PlayerColor playerColor) {
        if (circleIndex >= 0 && circleIndex < circles.size()) {
            Circle circle = circles.get(circleIndex);
            if (circle != null && playerColor != null) {
                Color fxColor = switch (playerColor) {
                    case BLUE -> Color.BLUE;
                    case RED -> Color.RED;
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


    public void updateBoardDisplay(ViewBoard newBoard) {
        if (newBoard == null) {
            System.err.println("BoardController: updateBoardDisplay with null newBoard");
            return;
        }

        for (int i = 0; i < Math.max(circles.size(), circleLabels.size()); i++) {
            clearPosition(i);
        }

        ViewPlayer[] playerPositions = newBoard.players;
        for(ViewPlayer player : playerPositions) {
            if (player != null && player.position >= 0 && player.position < circles.size() && player.inGame) {
                setPlayerPosition(player.position, player.playerColor);
            }
        }
    }

    @Override
    public void onShipUpdated(ViewShip ship) {
        // ignore
    }

    @Override
    public void onLobbyUpdated(ViewLobby lobby) {

    }

    @Override
    public void onErrorMessageReceived(String message) {
        // ignore
    }

    @Override
    public void onComponentInHandUpdated(ViewComponent component) {
        // ignore
    }

    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {
        // ignore
    }


    @Override
    public void onBoardUpdated(ViewBoard board) {
        updateBoardDisplay(board);
    }
}
