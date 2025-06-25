package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to inform them that it's not their turn and they should wait for their turn.
 */
public record StandbyMessage(
        String situation
) implements Message {
    @Override
    public String toString() {
        return situation;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().idleMenu(situation);
    }
}
