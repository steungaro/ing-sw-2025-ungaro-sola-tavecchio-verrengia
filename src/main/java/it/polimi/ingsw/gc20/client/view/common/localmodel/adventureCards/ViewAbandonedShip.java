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
                LATERAL + "\u001B[1m    Abandoned Ship    \u001B[22m" + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "     Lost crew: \u001B[31m"  + lostCrew  + "\u001B[0m     " + LATERAL + "\n" +
                LATERAL + "      Credits: \u001B[32m"    + credits   + "\u001B[0m      " + LATERAL + "\n" +
                LATERAL + "     Lost days: \u001B[31m"  + lostDays  + "\u001B[0m     " + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 4, 8, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "\u001B[1m    Abandoned Ship    \u001B[22m" + LATERAL;
            case 5 -> LATERAL + "     Lost crew: \u001B[31m" + lostCrew + "\u001B[0m     " + LATERAL;
            case 6 -> LATERAL + "      Credits: \u001B[32m" + credits + "\u001B[0m      " + LATERAL;
            case 7 -> LATERAL + "     Lost days: \u001B[31m" + lostDays + "\u001B[0m     " + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }
}
