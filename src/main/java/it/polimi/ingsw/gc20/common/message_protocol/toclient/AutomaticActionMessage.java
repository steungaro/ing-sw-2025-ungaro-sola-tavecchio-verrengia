package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to inform them about the automatic action to be performed.
 */
public record AutomaticActionMessage(
        String action
) implements Message {
    @Override
    public String toString() {
        return action;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().automaticAction(action);
    }
}
