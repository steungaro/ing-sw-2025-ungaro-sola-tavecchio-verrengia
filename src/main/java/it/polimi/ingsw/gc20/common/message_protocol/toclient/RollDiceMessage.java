package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.FireType;

public record RollDiceMessage(
        FireType fireType,
        int direction // 0 = up, 1 = right, 2 = down, 3 = left
) implements Message {

    @Override
    public String toString() {
        return "RollDiceMessage{" +
                "fireType=" + fireType +
                ", direction=" + direction +
                '}';
    }
    @Override
    public void handleMessage() {

    }
}
