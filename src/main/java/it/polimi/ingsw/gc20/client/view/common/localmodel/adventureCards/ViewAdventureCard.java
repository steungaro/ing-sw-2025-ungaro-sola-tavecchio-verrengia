package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

public abstract class ViewAdventureCard {
    public String type;
    public int id;
    protected static final String UP =          "╭──────────────────────╮";
    protected static final String DOWN =        "╰──────────────────────╯";
    protected static final String EMPTY_ROW =    "                      ";
    protected static final String LATERAL =     "│";

    /**
     * Converts a specific part of the card to its string representation
     * based on the given line index.
     *
     * @param i the index of the line to retrieve as a string representation
     * @return the string representation of the specified line; returns an empty string if the line index is invalid
     */
    public abstract String toLine(int i);

    /**
     * Creates a new instance of a {@code ViewAdventureCard} based on the type of the provided {@code AdventureCard}.
     * The specific view card class is dynamically determined using the type provided by the {@code AdventureCard}.
     *
     * @param adventureCard the {@code AdventureCard} object to be converted into a {@code ViewAdventureCard}
     * @return a {@code ViewAdventureCard} instance corresponding to the provided {@code AdventureCard},
     *         or {@code null} if an error occurs during the creation process
     */
    public static ViewAdventureCard createFrom(AdventureCard adventureCard) {
        String type = adventureCard.getName();
        if (type.equals("CombatZone")) {
            type = "CombatZone" + adventureCard.combatType();
        }

        try {
            String className = "it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.View" + type;

            Class<?> clazz = Class.forName(className);

            ViewAdventureCard viewCard = (ViewAdventureCard) clazz.getConstructor(AdventureCard.class).newInstance(adventureCard);

            viewCard.type = type;

            return viewCard;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // or handle the error as needed
        }
    }

    /**
     * Initializes the current view representation of the adventure card
     * by setting its type using the name of the provided {@code AdventureCard}.
     *
     * @param adventureCard the {@code AdventureCard} whose name is used to set the type of the view
     */
    protected void initialize(AdventureCard adventureCard) {
        this.type = adventureCard.getName();
        this.id = adventureCard.getIDCard();
    }
}
