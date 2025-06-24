package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to notify them of an error that occurred during login.
 */
public record LoginFailedMessage(
        String username
) implements Message {
    @Override
    public String toString() {
        return username + " login failed.";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().loginFailed(username);
    }
}
