package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

public class ViewOpenSpace extends ViewAdventureCard {

    // In ViewSlavers.java
    @Override
    protected void initialize(AdventureCard adventureCard) {
        super.initialize(adventureCard);
    }

    @Override
    public String toString() {
        return
                up() + "\n" +
                        lateral() + "  OpenSpace           " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + spaces(10) + "/\\" + spaces(10) + lateral() + "\n" +
                        lateral() + spaces(9) + "|__|" + spaces(9) + lateral() + "\n" +
                        lateral() + spaces(9) + "|  |" + spaces(9) + lateral() + "\n" +
                        lateral() + spaces(9) + "|__|" + spaces(9) + lateral() + "\n" +
                        lateral() + spaces(9) + "^^^^" + spaces(9) + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        down();
    }

}
