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
                LATERAL + "\u001B[1m      Open space      \u001B[0m" + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + " ".repeat(10) + "\u001B[31m/\\\u001B[0m" + " ".repeat(10) + LATERAL + "\n" +
                LATERAL + " ".repeat(9) + "|\u001B[36m==\u001B[0m|" + " ".repeat(9) + LATERAL + "\n" +
                LATERAL + " ".repeat(9) + "|  |" + " ".repeat(9) + LATERAL + "\n" +
                LATERAL + " ".repeat(9) + "|\u001B[36m__\u001B[0m|" + " ".repeat(9) + LATERAL + "\n" +
                LATERAL + " ".repeat(9) + "\u001B[33m^^^^\u001B[0m" + " ".repeat(9) + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 4, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "\u001B[1m      Open space      \u001B[0m" + LATERAL;
            case 5 -> LATERAL + " ".repeat(10) + "\u001B[31m/\\\u001B[0m" + " ".repeat(10) + LATERAL;
            case 6 -> LATERAL + " ".repeat(9) + "│\u001B[36m==\u001B[0m│" + " ".repeat(9) + LATERAL;
            case 7 -> LATERAL + " ".repeat(9) + "│\u001B[36m__\u001B[0m│" + " ".repeat(9) + LATERAL;
            case 8 -> LATERAL + " ".repeat(9) + "\u001B[33m^^^^\u001B[0m" + " ".repeat(9) + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }

}
