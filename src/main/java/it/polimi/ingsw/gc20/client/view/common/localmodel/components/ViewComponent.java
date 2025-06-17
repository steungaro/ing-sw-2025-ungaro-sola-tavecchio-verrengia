package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

import java.io.Serializable;

/**
 * The ViewComponent class serves as a base class for various visual components in a system.
 * It implements the Serializable interface to allow instances to be serialized for storage
 * or transmission. Subclasses of ViewComponent can represent specific types of components
 * by overriding methods and adding additional properties.
 */
public class ViewComponent implements Serializable {
    public int id;
    public int rotation; // 0 = up, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
    public int rotComp; // 0 = up, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
    public int upConnectors;
    public int downConnectors;
    public int leftConnectors;
    public int rightConnectors;

    protected static final String EMPTY_ROW = "             ";

    private static final String UP3 =    "╭──↑───↑───↑──╮";
    private static final String UP2 =    "╭──↑───────↑──╮";
    private static final String UP1 =    "╭──────↑──────╮";
    private static final String UP0 =    "╭─────────────╮";

    private static final String DOWN3 =  "╰──↓───↓───↓──╯";
    private static final String DOWN2 =  "╰──↓───────↓──╯";
    private static final String DOWN1 =  "╰──────↓──────╯";
    private static final String DOWN0 =  "╰─────────────╯";

    private static final String[] LEFT0 = {
            "│",
            "│",
            "│"
    };
    private static final String[] LEFT1 = {
            "│",
            "←",
            "│"
    };
    private static final String[] LEFT2 = {
            "←",
            "│",
            "←"
    };
    private static final String[] LEFT3 = {
            "←",
            "←",
            "←"
    };

    private static final String[] RIGHT0 = {
            "│",
            "│",
            "│"
    };
    private static final String[] RIGHT1 = {
            "│",
            "→",
            "│"
    };
    private static final String[] RIGHT2 = {
            "→",
            "│",
            "→"
    };
    private static final String[] RIGHT3 = {
            "→",
            "→",
            "→"
    };

    /**
     * Determines the first row of the component's visual representation based on the
     * number of "up connectors". Returns one of the predefined string representations
     * (UP0, UP1, UP2, or UP3) corresponding to the number of connectors.
     *
     * @return the string representation of the first row, based on the number of "up connectors"
     */
    protected String firstRow() {
        return upConnectors == 0 ? UP0 : upConnectors == 1 ? UP1 : upConnectors == 2 ? UP2 : UP3;
    }

    /**
     * Determines the last row of the component's visual representation based on
     * the number of "down connectors". Returns one of the predefined string
     * representations (DOWN0, DOWN1, DOWN2, or DOWN3) corresponding to the number
     * of connectors.
     *
     * @return the string representation of the last row, based on the number of "down connectors"
     */
    protected String lastRow() {
        return downConnectors == 0 ? DOWN0 : downConnectors == 1 ? DOWN1 : downConnectors == 2 ? DOWN2 : DOWN3;
    }

    /**
     * Returns the visual representation of the specified line of the component.
     * The line is determined by the provided index.
     *
     * @param i the index of the line to retrieve
     * @return the string representation of the specified line
     */
    public String toLine(int i) {
        return coveredLine(i);
    }

    /**
     * Provides a string representation of the component.
     * The representation is defined by the static method {@code covered}, which
     * generates a predefined visual representation.
     *
     * @return the string representation of the component as defined by the {@code covered} method
     */
    public String toString() {
        return covered();
    }

    /**
     * Determines the visual representation of the left column for the specified row.
     * The representation is based on the value of the "left connectors".
     *
     * @param row the index of the row for which the left column representation is determined
     * @return the string representation of the left column for the specified row, or null
     *         if the "left connectors" value does not match any predefined cases
     */
    protected String leftCol(int row) {
        return switch (leftConnectors) {
            case 0 -> LEFT0[row];
            case 1 -> LEFT1[row];
            case 2 -> LEFT2[row];
            case 3 -> LEFT3[row];
            default -> null;
        };
    }

    /**
     * Determines the visual representation of the right column for the specified row.
     * The representation is based on the value of the "right connectors".
     *
     * @param row the index of the row for which the right column representation is determined
     * @return the string representation of the right column for the specified row, or null
     *         if the "right connectors" value does not match any predefined cases
     */
    protected String rightCol(int row) {
        return switch (rightConnectors) {
            case 0 -> RIGHT0[row];
            case 1 -> RIGHT1[row];
            case 2 -> RIGHT2[row];
            case 3 -> RIGHT3[row];
            default -> null;
        };
    }

    /**
     * Generates and returns a predefined visual representation of a generic component.
     * The representation consists of a rectangular shape.
     *
     * @return the string representation of a rectangular shape
     */
    public static String covered() {
        return
                """
                        ╭─────────────╮
                        │             │
                        │             │
                        │             │
                        ╰─────────────╯""";
    }

    /**
     * Returns a string representation of a specific line of a rectangular shape
     * based on the provided index. Each index corresponds to a predefined visual
     * representation of the shape's line.
     *
     * @param i the index of the line to retrieve. Valid values are:
     *          0 for the top line,
     *          1-3 for the middle lines,
     *          4 for the bottom line.
     *          Any other value will return null.
     * @return the string representation of the line corresponding to the provided index,
     *         or null if the index does not match any predefined case.
     */
    public static String coveredLine(int i) {
        return switch (i) {
            case 0 -> "╭─────────────╮";
            case 1, 2, 3 -> "│             │";
            case 4 -> "╰─────────────╯";
            default -> null;
        };
    }

    public boolean isCabin() {
        return false;
    }

    public boolean isEngine() {
        return false;
    }

    public boolean isBattery() {
        return false;
    }

    public boolean isCannon() {
        return false;
    }

    public boolean isCargoHold() {
        return false;
    }

    public boolean isPipes() {
        return false;
    }

    public boolean isSpecialCargoHold() {
        return false;
    }

    public boolean isLifeSupport() {
        return false;
    }

    public boolean isShield() {
        return false;
    }

    public boolean isStartingCabin() {
        return false;
    }
}
