package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.gamesets.Board;

public record BoardUpdateMessage(
        ViewBoard board
) implements Message {

    /*public BoardUpdateMessage (Board board){
        ViewBoard viewBoard = new ViewBoard();
        viewBoard.isLearner = board.isLearner();

        this (viewBoard);
    }
     */
    @Override
    public void handleMessage() {
        //TODO
    }

    @Override
    public String toString() {
        //TODO
        return null;
    }
}
