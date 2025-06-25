package it.polimi.ingsw.gc20.server.model.components;

/**
 * Enum representing possible colors of aliens and cabins in the game. The colors include:
 * - PURPLE
 * - BROWN
 * - BOTH: Represents a combination of PURPLE and BROWN.
 * - NONE: Represents absence of color.
 */
public enum AlienColor {
    PURPLE,
    BROWN,
    BOTH,
    NONE;


    /**
     * Retrieves a string representation of the color associated with the current enum constant.
     * The returned string includes an ANSI escape sequence for colored text formatting
     * where applicable, followed by the corresponding character for the color.
     *
     * @return a string representing the color of the enum:
     *         PURPLE as colored "P", BROWN as colored "B", BOTH as "*", and NONE as "X".
     */
    public String getColorChar() {
        return switch (this) {
            case PURPLE -> "\u001B[35mP\u001B[0m";
            case BROWN -> "\u001B[33mB\u001B[0m";
            case BOTH -> "*";
            case NONE -> "X";
        };
    }
}