package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to notify them of an error that occurred during the game.
 */
public record ErrorMessage(
        String errorMessage
) implements Message {
    @Override
    public String toString() {
        return "Error: " + errorMessage;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().displayErrorMessage(errorMessage);
    }
}
