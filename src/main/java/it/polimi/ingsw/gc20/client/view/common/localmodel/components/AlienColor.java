package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

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
            case PURPLE -> "P";
            case BROWN -> "B";
            case BOTH -> "*";
            case NONE -> " ";
        };
    }
}