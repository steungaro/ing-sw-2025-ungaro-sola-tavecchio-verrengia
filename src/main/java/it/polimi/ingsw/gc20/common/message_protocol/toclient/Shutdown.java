package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to signal that the game should be shut down.
 * It triggers the shutdown process in the ClientGameModel.
 */
public record Shutdown() implements Message {

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().shutdown();
    }
}
