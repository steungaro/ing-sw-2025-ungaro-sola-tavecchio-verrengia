package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record EndMoveConfirmMessage(
        String username,
        String nextPlayer
) implements Message {
    @Override
    public String toString() {
        if (nextPlayer == null){
            return username + "ended the move. No next player.";
        }
        return username + "ended the move. Next player: " + nextPlayer;

    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
