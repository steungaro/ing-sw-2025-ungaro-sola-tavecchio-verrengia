package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

public class ViewSlavers extends ViewAdventureCard {
    public int firePower;
    public int lostCrew;
    public int credits;
    public int lostDays;

    protected ViewSlavers(AdventureCard adventureCard) {
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
                        LATERAL + "       Slavers        " + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "     FirePower: " + firePower + "     " + LATERAL + "\n" +
                        LATERAL + "     Lost crew: " + lostCrew + "     " + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "      Credits: " + credits + "      " + LATERAL + "\n" +
                        LATERAL + "     Lost days: " + lostDays + "     " + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 6, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "       Slavers        " + LATERAL;
            case 4 -> LATERAL + "     FirePower: " + firePower + "     " + LATERAL;
            case 5 -> LATERAL + "     Lost crew: " + lostCrew + "     " + LATERAL;
            case 7 -> LATERAL + "      Credits: " + credits + "      " + LATERAL;
            case 8 -> LATERAL + "     Lost days: " + lostDays + "     " + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }
}
