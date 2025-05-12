package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record AcceptedCardMessage(
        String username
) implements Message {
    @Override
    public String toString() {
        return username + " accepted the card.";
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
