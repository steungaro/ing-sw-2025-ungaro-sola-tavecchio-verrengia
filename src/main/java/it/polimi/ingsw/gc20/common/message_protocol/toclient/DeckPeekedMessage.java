package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

import java.util.ArrayList;
import java.util.List;

/**
 * This message is sent to the client to inform them that a player has peeked at a deck of adventure cards.
 * It contains the username of the player who peeked and a list of AdventureCard objects representing the cards in the deck.
 */
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
