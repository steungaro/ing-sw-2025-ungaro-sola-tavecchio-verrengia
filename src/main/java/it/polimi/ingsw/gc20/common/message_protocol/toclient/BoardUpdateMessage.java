package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record BoardUpdateMessage(

) implements Message {
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
