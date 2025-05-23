package it.polimi.ingsw.gc20.server.model.gamesets;

import java.io.Serializable;

/**
 * 
 */
public enum CargoColor implements Serializable {
    BLUE,
    GREEN,
    YELLOW,
    RED;

    /**
     * @return the color of the cargo as a string
     */
    public String TUIPrint() {
        return switch (this) {
            case BLUE -> "\u001B[34m" + "B" + "\u001B[0m";
            case GREEN -> "\u001B[32m" + "G" + "\u001B[0m";
            case YELLOW -> "\u001B[33m" + "Y" + "\u001B[0m";
            case RED -> "\u001B[31m" + "R" + "\u001B[0m";
        };
    }

    public int value() {
        return ordinal() + 1;
    }
}