package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record RollDiceMessage(
        String message
) implements Message {

    @Override
    public String toString() {
        return "RollDiceMessage{" +
                "message='" + message + '\'' +
                '}';
    }
    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().rollDiceMenu(message);
    }
}