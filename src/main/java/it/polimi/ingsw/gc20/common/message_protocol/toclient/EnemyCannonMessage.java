package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record EnemyCannonMessage(
        int power
) implements Message {
    @Override
    public String toString() {
        return "EnemyCannonMessage{" +
                "power=" + power +
                '}';
    }

    @Override
    public void handleMessage() {
        // No action needed for this message
    }
}
