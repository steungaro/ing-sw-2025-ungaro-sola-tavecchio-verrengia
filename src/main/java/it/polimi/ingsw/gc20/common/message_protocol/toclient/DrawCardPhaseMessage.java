package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to inform them about the phase of drawing a card. The player can choose to keep playing or not.
 */
public record DrawCardPhaseMessage() implements Message {
    @Override
    public String toString() {
        return "DrawCardPhaseMessage{}";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().keepPlayingMenu();
    }
}
