package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

public class ViewAbandonedShip extends ViewAdventureCard {
    int lostCrew;
    int credits;
    int lostDays;

    @Override
    protected void initialize(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.lostCrew = adventureCard.getCrew();
        this.credits = adventureCard.getCredits();
        this.lostDays = adventureCard.getLostDays();
    }

    @Override
    public String toString() {
        return
                up() + "\n" +
                        lateral() + "  Abandoned Ship      " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  LostCrew: " + lostCrew + "         " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  Credits: " + credits + "$         " + lateral() + "\n" +
                        lateral() + "  LostDays: " + lostDays + "         " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        down();
    }
}
