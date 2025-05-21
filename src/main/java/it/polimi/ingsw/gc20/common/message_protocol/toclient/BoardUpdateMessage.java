package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.gamesets.Board;
import it.polimi.ingsw.gc20.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public record BoardUpdateMessage(
        ViewBoard viewBoard
) implements Message {

    public static BoardUpdateMessage fromBoard(Board board, List<Player> players, Boolean assemblingState){
        List<ViewPlayer> playerList = new ArrayList<>();
        for (Player player: players ){
            ViewPlayer p = new ViewPlayer(player.getUsername(), player.getColor(), 0);
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
