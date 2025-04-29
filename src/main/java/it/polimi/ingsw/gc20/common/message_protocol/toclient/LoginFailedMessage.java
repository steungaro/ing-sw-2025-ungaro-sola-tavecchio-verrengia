package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record LoginFailedMessage(
        String username
) implements Message {
    @Override
    public String toString() {
        return "LoginFailedMessage{" +
                "username='" + username + '\'' +
                '}';
    }

    @Override
    public void handleMessage() {
    }
}
