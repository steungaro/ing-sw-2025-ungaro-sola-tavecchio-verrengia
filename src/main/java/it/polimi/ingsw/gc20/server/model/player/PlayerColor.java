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
            case BLUE -> "P1";
            case RED -> "P2";
            case GREEN -> "P3";
            case YELLOW -> "P4";
        };
    }
}