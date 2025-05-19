package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record EngineActivationPhaseMessage() implements Message {
    @Override
    public String toString() {
        return "EngineActivationPhaseMessage{}";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().engineMenu(toString());
    }
}
