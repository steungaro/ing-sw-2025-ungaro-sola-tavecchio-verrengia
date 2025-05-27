package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewAdventureCardTest {


    @Test
    void createFrom() {
        AdventureCard adventureCard = new AdventureCard();
        adventureCard.setName("Stardust");
        ViewAdventureCard viewCard = ViewAdventureCard.createFrom(adventureCard);
    }
}