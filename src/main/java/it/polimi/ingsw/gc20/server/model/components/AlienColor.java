package it.polimi.ingsw.gc20.server.model.components;

/**
 * This enumeration represents the colors of the aliens.
 */
public enum AlienColor {
    PURPLE,
    BROWN,
    BOTH,
    NONE;


    public String getColorChar() {
        return switch (this) {
            case PURPLE -> "\u001B[35mP\u001B[0m";
            case BROWN -> "\u001B[33mB\u001B[0m";
            case BOTH -> "*";
            case NONE -> "X";
        };
    }
}