package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.network.event.Event;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

import java.io.Serializable;

// Event that is generated when a player takes a component from the pile or from booked components
public record TakeComponentMessage(
        String username,
        Integer componentID
) implements Message, Serializable {
    @Override
    public String toString() {
        return "TakeComponentMessage {username = " + username + ", component = " + componentID + "}";
    }

    @Override
    public void handleMessage(MessageHandler messageHandler) {
        //TODO
    }
}
