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
                UP + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "      Open space      " + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + " ".repeat(10) + "/\\" + " ".repeat(10) + LATERAL + "\n" +
                LATERAL + " ".repeat(9) + "|==|" + " ".repeat(9) + LATERAL + "\n" +
                LATERAL + " ".repeat(9) + "|  |" + " ".repeat(9) + LATERAL + "\n" +
                LATERAL + " ".repeat(9) + "|__|" + " ".repeat(9) + LATERAL + "\n" +
                LATERAL + " ".repeat(9) + "^^^^" + " ".repeat(9) + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 4, 8, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "      Open space      " + LATERAL;
            case 5 -> LATERAL + " ".repeat(10) + "/\\" + " ".repeat(10) + LATERAL;
            case 6 -> LATERAL + " ".repeat(9) + "│==│" + " ".repeat(9) + LATERAL;
            case 7 -> LATERAL + " ".repeat(9) + "│__│" + " ".repeat(9) + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }

}
