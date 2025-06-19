package it.polimi.ingsw.gc20.server.model.gamesets;

import java.io.Serializable;

/**
 * Enum representing different colors that can be assigned to cargo.
 * This enum supports serialization and provides utility methods to work with its values.
 */
public enum CargoColor implements Serializable {
    BLUE,
    GREEN,
    YELLOW,
    RED;

    /**
     * Returns the game value of the cargo color.
     *
     * @return the ordinal value of the enum plus one.
     */
    public int value() {
        return ordinal() + 1;
    }
}