package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record HourglassMessage(
        int remainingTime,
        int numberOfRotations,
        long timestamp
) implements Message {
    @Override
    public String toString() {
        return "RemainingTime = " + remainingTime +
                ", numberOfRotations=" + numberOfRotations;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().getBoard().remainingTime = remainingTime;
        ClientGameModel.getInstance().getBoard().numberOfRotations = numberOfRotations;
    }
}
