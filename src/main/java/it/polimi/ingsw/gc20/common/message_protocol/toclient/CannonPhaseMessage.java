package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

public record CannonPhaseMessage(
        String message
) implements Message {
    @Override
    public String toString() {
        return "CannonPhaseMessage{" +
                "message='" + message + '\'' +
                '}';
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().cannonsMenu(message);
    }
}
