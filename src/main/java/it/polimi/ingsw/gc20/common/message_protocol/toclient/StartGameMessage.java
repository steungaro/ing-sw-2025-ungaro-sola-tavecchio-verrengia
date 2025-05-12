package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record StartGameMessage() implements Message {
    @Override
    public String toString() {
        return "Game Started";
    }

    @Override
    public void handleMessage() {
        // Handle the start game message (client side)
        // This method should be implemented in the client-side code
    }

}
