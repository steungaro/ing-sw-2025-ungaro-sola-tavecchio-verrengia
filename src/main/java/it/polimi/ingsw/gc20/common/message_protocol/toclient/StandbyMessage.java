package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record StandbyMessage(
        String situation
) implements Message {
    @Override
    public String toString() {
        return situation;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().automaticAction(situation);
    }
}
