package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.components.Component;

import java.util.List;

public record PileUpdateMessage(
        String username,
        int unviewedSize,
        List<Component> viewed,
        String action // can be "taken from viewed", taken from unviewed", "added to viewed"
        ) implements Message {
    @Override
    public String toString() {
        return username + "has" + action + "a component";
    }
    @Override
    public void handleMessage() {
        //TODO
    }
}
