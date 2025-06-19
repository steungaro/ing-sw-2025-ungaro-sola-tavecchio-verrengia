package it.polimi.ingsw.gc20.server.model.player;

/**
 * Represents the possible colors a player can have in the game.
 * Each color is associated with a specific text styling for terminal user interface (TUI) display.
 */
public enum PlayerColor {
    BLUE,
    RED,
    GREEN,
    YELLOW;

    /**
     * Generates a string representation of the player color formatted for terminal user interface (TUI) display.
     * The output includes color-specific text highlighting for easy differentiation.
     *
     * @return a color-coded string representation of the player, formatted for TUI output
     */
    public String TUIPrint() {
        return switch (this) {
            case BLUE -> //blue text
                    "\u001B[34m" + "P1" + "\u001B[0m";
            case RED -> //red text
                    "\u001B[31m" + "P2" + "\u001B[0m";
            case GREEN -> //green text
                    "\u001B[32m" + "P3" + "\u001B[0m";
            case YELLOW -> //yellow text
                    "\u001B[33m" + "P4" + "\u001B[0m";
        };
    }
}