package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

public class ViewAbandonedShip extends ViewAdventureCard {
    int lostCrew;
    int credits;
    int lostDays;

    public ViewAbandonedShip() {
        super();
    }

    protected ViewAbandonedShip(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.lostCrew = adventureCard.getCrew();
        this.credits = adventureCard.getCredits();
        this.lostDays = adventureCard.getLostDays();
    }

    @Override
    public String toString() {
        return
                UP + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "    Abandoned Ship    " + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "     Lost crew: "  + lostCrew  + "     " + LATERAL + "\n" +
                LATERAL + "      Credits: "    + credits   + "      " + LATERAL + "\n" +
                LATERAL + "     Lost days: "  + lostDays  + "     " + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 4, 8, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "    Abandoned Ship    " + LATERAL;
            case 5 -> LATERAL + "     Lost crew: " + lostCrew + "     " + LATERAL;
            case 6 -> LATERAL + "      Credits: " + credits + "      " + LATERAL;
            case 7 -> LATERAL + "     Lost days: " + lostDays + "     " + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }
}
