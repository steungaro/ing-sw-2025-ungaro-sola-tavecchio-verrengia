package it.polimi.ingsw.gc20.server.model.components;

/**
 * This enumeration represents the directions of the cards.
 */
public enum Direction {
    UP,
    LEFT,
    DOWN,
    RIGHT;

    /**
     * this function returns a value for the direction
     * @return the value of the direction
     * 0-up
     * 1-right
     * 2-down
     * 3-left
     */
    public int getValue() {
        return switch (this) {
            case UP -> 0;
            case RIGHT -> 1;
            case DOWN -> 2;
            case LEFT -> 3;
            default -> -1;
        };
    }
}