package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

public class ViewSlavers extends ViewAdventureCard {
    public int firePower;
    public int lostCrew;
    public int credits;
    public int lostDays;

    public ViewSlavers(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.firePower = adventureCard.getFirePower();
        this.lostCrew = adventureCard.getCrew();
        this.credits = adventureCard.getCredits();
        this.lostDays = adventureCard.getLostDays();
    }

    public ViewSlavers() {
        super();
    }

    @Override
    public String toString() {
        return
                        UP + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "\u001B[1m       Slavers        \u001B[0m" + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "     FirePower: \u001B[31m" + firePower + "\u001B[0m     " + LATERAL + "\n" +
                        LATERAL + "     Lost crew: \u001B[31m" + lostCrew + "\u001B[0m     " + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "      Credits: \u001B[32m" + credits + "\u001B[0m      " + LATERAL + "\n" +
                        LATERAL + "     Lost days: \u001B[31m" + lostDays + "\u001B[0m     " + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 6, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "\u001B[1m       Slavers        \u001B[0m" + LATERAL;
            case 4 -> LATERAL + "     FirePower: \u001B[31m" + firePower + "\u001B[0m     " + LATERAL;
            case 5 -> LATERAL + "     Lost crew: \u001B[31m" + lostCrew + "\u001B[0m     " + LATERAL;
            case 7 -> LATERAL + "      Credits: \u001B[32m" + credits + "\u001B[0m      " + LATERAL;
            case 8 -> LATERAL + "     Lost days: \u001B[31m" + lostDays + "\u001B[0m     " + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }
}
