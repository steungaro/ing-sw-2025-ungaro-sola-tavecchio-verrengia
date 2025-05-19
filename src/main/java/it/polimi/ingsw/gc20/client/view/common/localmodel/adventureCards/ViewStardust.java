package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

public class ViewStardust extends ViewAdventureCard {

    @Override
    protected void initialize(AdventureCard adventureCard) {
        super.initialize(adventureCard);
    }

    @Override
    public String toString() {
        return
                UP + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "       Stardust       " + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "          ֎֎          " + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 4, 5, 7, 8, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "       Stardust       " + LATERAL;
            case 6 -> LATERAL + "          ֎֎          " + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }


}
