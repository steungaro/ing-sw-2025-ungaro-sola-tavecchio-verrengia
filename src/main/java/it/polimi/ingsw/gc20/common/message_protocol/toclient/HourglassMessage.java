package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to inform them about the new hourglass status.
 */
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
        ClientGameModel.getInstance().setHourglassMessage(numberOfRotations, timestamp);

        ClientGameModel.getInstance().getBoard().timeStampOfLastHourglassRotation = timestamp;
        ClientGameModel.getInstance().getBoard().numberOfRotations = numberOfRotations;
    }
}
