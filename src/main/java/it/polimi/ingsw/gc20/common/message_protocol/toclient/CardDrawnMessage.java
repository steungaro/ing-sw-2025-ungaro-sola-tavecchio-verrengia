package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

/**
 * This message is sent to the client when a card is drawn from the adventure deck.
 * It contains the drawn card and updates the current card in the client's game model.
 */
public record CardDrawnMessage(
        AdventureCard card
) implements Message {
    @Override
    public String toString() {
        return card.getName() +"has been drawn";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().setCurrentCard(ViewAdventureCard.createFrom(this.card));
    }

}
