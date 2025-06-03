package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

@SuppressWarnings( "unused")
public class ViewEpidemic extends ViewAdventureCard {


    /**
     * Constructs a {@code ViewEpidemic} instance and initializes it with the specified {@code AdventureCard}.
     * This method sets up the representation of the epidemic adventure card for the view.
     *
     * @param adventureCard the {@code AdventureCard} instance used to initialize the view representation
     */
    public ViewEpidemic(AdventureCard adventureCard) {
        super.initialize(adventureCard);
    }

    public ViewEpidemic() {
        super();
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 4, 5, 6, 7, 8, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "\u001B[1m       Epidemic       \u001B[0m" + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }

    @Override
    public String toString() {
        return
                UP + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "\u001B[1m       Epidemic       \u001B[0m" + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }
}
