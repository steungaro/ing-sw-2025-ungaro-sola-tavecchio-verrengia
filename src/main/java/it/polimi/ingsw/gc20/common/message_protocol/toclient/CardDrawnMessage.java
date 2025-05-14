package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

public record CardDrawnMessage(
        int cardId,
        String cardName
) implements Message {
    @Override
    public String toString() {
        return cardName +"has been drawn";
    }

    @Override
    public void handleMessage() {
        //TODO
    }

}
