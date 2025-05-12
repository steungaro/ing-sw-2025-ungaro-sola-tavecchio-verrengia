package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record DieResultMessage(
        int dieValue
) implements Message {
    @Override
    public String toString() {
        return "Die result: " + dieValue;
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
