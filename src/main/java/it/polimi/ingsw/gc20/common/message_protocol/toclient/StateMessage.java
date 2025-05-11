package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record StateMessage(
        // TODO
) implements Message {
    @Override
    public String toString() {
        return "StateMessage{}";
    }

    @Override
    public void handleMessage() {
        // Handle the state message (client side)
        // This method should be implemented in the client-side code
    }
}
