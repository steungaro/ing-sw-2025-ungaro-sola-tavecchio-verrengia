package it.polimi.ingsw.gc20.server.model.player;

/**
 * @author GC20
 */
public enum PlayerColor {
    BLUE,
    RED,
    GREEN,
    YELLOW;

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