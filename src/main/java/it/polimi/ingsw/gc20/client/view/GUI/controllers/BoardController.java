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
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.*;

import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

public abstract class BoardController implements GameModelListener, BindCleanUp {

    protected List<Circle> circles = new ArrayList<>();
    protected List<Label> circleLabels = new ArrayList<>();

    @FXML protected Pane rootPane;


    /**
     * Initializes the JavaFX components and sets up the board controller.
     * This method is automatically called by JavaFX after loading the FXML file.
     * It retrieves the current board from the game model and updates the display.
     */
    @FXML
    public void initialize() {
        updateBoardDisplay(ClientGameModel.getInstance().getBoard());

        if (rootPane != null) {
            rootPane.widthProperty().addListener((_, _, _) -> {
                if (rootPane != null) {
                    rootPane.requestLayout();
                }

            });

            rootPane.heightProperty().addListener((_, _, _) -> {
                if (rootPane != null) {
                    rootPane.requestLayout();
                }

            });
        }
    }

    /**
     * Sets a player's position on the board by coloring the corresponding circle.
     * The circle at the specified index will be filled with the color associated
     * with the player's color.
     * 
     * @param circleIndex the index of the circle to color (0-based)
     * @param playerColor the color of the player to represent on the board
     */
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

    /**
     * Clears a position on the board by making the circle transparent and removing any text.
     * This method resets both the circle's fill color and the associated label's text.
     * 
     * @param circleIndex the index of the circle and label to clear (0-based)
     */
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

    /**
     * Updates the entire board display based on the provided board state.
     * This method clears all current positions and redraws player positions
     * according to the new board state. Only players who are in the game
     * and have valid positions will be displayed.
     * 
     * @param newBoard the new board state to display, must not be null
     */
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

    /**
     * Handles ship update events from the game model.
     * This implementation ignores ship updates as they are not relevant for board display.
     * 
     * @param ship the updated ship view object
     */
    @Override
    public void onShipUpdated(ViewShip ship) {
        // ignore - not relevant for board controller
    }

    /**
     * Handles lobby update events from the game model.
     * This implementation provides no functionality as lobby updates are not relevant for board display.
     * 
     * @param lobby the updated lobby view object
     */
    @Override
    public void onLobbyUpdated(ViewLobby lobby) {
        // Empty implementation - not relevant for board controller
    }

    /**
     * Handles error message events from the game model.
     * This implementation ignores error messages as they are handled elsewhere in the UI.
     * 
     * @param message the error message received from the game model
     */
    @Override
    public void onErrorMessageReceived(String message) {
        // ignore - error messages handled elsewhere
    }

    /**
     * Handles component in hand update events from the game model.
     * This implementation ignores hand component updates as they are not relevant for board display.
     * 
     * @param component the updated component in the player's hand
     */
    @Override
    public void onComponentInHandUpdated(ViewComponent component) {
        // ignore - not relevant for board controller
    }

    /**
     * Handles current adventure card update events from the game model.
     * This implementation ignores adventure card updates as they are not relevant for board display.
     * 
     * @param currentCard the updated current adventure card
     */
    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {
        // ignore - not relevant for board controller
    }

    /**
     * Handles board update events from the game model.
     * This method updates the board display whenever the board state changes.
     * 
     * @param board the updated board view object
     */
    @Override
    public void onBoardUpdated(ViewBoard board) {
        updateBoardDisplay(board);
    }
}