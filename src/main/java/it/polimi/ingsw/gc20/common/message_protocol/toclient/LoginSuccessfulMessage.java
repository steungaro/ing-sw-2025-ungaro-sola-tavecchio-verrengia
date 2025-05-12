package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record LoginSuccessfulMessage(
        String username
) implements Message {
    @Override
    public String toString() {
        return username + " login successful.";
    }
    @Override
    public void handleMessage() {

    }
}
