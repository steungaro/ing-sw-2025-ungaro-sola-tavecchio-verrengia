package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record EndMoveConfirmMessage(
        String username
) implements Message {
    @Override
    public String toString() {
        return username + "ended the move.";
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
