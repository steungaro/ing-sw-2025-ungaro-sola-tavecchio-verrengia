package it.polimi.ingsw.gc20.server.model.gamesets;

/**
 * 
 */
public enum CargoColor {
    BLUE,
    GREEN,
    YELLOW,
    RED;

    public int value() {
        return ordinal() + 1;
    }
}