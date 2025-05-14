package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record ActivatedPowerMessage(
        String username,
        float power,
        String powerName //can be "cannon" or "engine"
) implements Message {
    @Override
    public String toString() {
        return username + " activated: " + powerName + " with value: " + power;
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
