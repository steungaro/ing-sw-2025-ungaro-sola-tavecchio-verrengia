package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

public record HourglassMessage(
        int numberOfRotations,
        long timestamp
) implements Message {
    @Override
    public String toString() {
        return "Timestamp = " + timestamp +
                ", numberOfRotations=" + numberOfRotations;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().getBoard().timeStampOfLastHourglassRotation = timestamp;
        ClientGameModel.getInstance().getBoard().numberOfRotations = numberOfRotations;
    }
}
