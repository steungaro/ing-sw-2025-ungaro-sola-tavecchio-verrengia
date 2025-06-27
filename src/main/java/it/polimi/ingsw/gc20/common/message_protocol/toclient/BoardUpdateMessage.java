package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.model.gamesets.Board;
import it.polimi.ingsw.gc20.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This message is sent to the client to update the board state.
 * It contains a ViewBoard object that represents the current state of the board.
 */
public record BoardUpdateMessage(
        ViewBoard viewBoard
) implements Message {

    /**
     * Creates a BoardUpdateMessage from the given board and players.
     *
     * @param board the current state of the board
     * @param players the list of players in the game
     * @param assemblingState indicates if the board is in assembling state
     * @return a new BoardUpdateMessage containing the view of the board
     */
    public static BoardUpdateMessage fromBoard(Board board, List<Player> players, boolean assemblingState){
        List<ViewPlayer> playerList = new ArrayList<>();
        for (Player player: players ){
            ViewPlayer p = new ViewPlayer(player.getUsername(), player.getColor(), assemblingState ? -1 : (player.getPosition() % board.getSpaces() + board.getSpaces()) % board.getSpaces());
            playerList.add(p);
        }
        ViewBoard viewBoard = new ViewBoard(board.isLearner(), playerList.toArray(new ViewPlayer[0]));
        viewBoard.assemblingState = assemblingState;
        return new BoardUpdateMessage(viewBoard);
    }
    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().setBoard(viewBoard);
    }

    @Override
    public String toString() {
        return  "Board Updated";
    }
}
