package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record PlaceComponentMessage() implements Message {
    @Override
    public String toString() {
        return "Place component";
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
