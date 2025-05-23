package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

import java.util.ArrayList;
import java.util.List;

public record DeckPeekedMessage(
        String username,
        List<AdventureCard> cards
) implements Message {
    @Override
    public String toString() {
        return username + " peeked a deck";
    }

    @Override
    public void handleMessage() {
        List<ViewAdventureCard> viewAdventureCards= new ArrayList<>();
        for( AdventureCard card : cards) {
            viewAdventureCards.add(ViewAdventureCard.createFrom(card));
        }
        ClientGameModel.getInstance().buildingMenu(viewAdventureCards);
    }
}
