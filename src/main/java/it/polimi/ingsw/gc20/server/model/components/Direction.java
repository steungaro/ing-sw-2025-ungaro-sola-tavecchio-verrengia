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
        };
    }

    /**
     * Returns the descriptive string representation of the direction.
     *
     * @return a string representing the direction: "upper" for UP, "right-hand" for RIGHT,
     *         "lower" for DOWN, and "left-hand" for LEFT.
     */
    public String getDirection() {
        return switch (this) {
            case UP -> "upper";
            case RIGHT -> "right-hand";
            case DOWN -> "lower";
            case LEFT -> "left-hand";
        };
    }
}