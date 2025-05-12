package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record HourglassMessage(
        int totalRemainingTime,
        int remainingTime,
        int numberOfRotations
) implements Message {
    @Override
    public String toString() {
        return "totalRemainingTime=" + totalRemainingTime +
                ", remainingTime=" + remainingTime +
                ", numberOfRotations=" + numberOfRotations;
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
