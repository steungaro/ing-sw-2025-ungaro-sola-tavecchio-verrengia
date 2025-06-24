package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;


/**
 * This message is sent to the client to inform them that the login was successful.
 */
public record LoginSuccessfulMessage(
        String username
) implements Message {
    @Override
    public String toString() {
        return username + " login successful.";
    }
    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().loginSuccessful(username);
        ClientGameModel.getInstance().loggedIn = true;
    }
}
