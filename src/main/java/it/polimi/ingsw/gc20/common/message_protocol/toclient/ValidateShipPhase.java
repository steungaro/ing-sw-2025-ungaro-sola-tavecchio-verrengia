package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record ValidateShipPhase() implements Message {
    @Override
    public String toString() {
        return "your ship is invalid";
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
