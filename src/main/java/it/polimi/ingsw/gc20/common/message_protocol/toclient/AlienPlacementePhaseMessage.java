package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record AlienPlacementePhaseMessage() implements Message {
    @Override
    public String toString() {
        return "AlienPlacementPhaseMessage{}";
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
