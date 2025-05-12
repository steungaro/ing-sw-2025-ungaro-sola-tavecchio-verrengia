package it.polimi.ingsw.gc20.server.model.components;

/**
 * This enumeration represents the connectors of the cards.
 */
public enum ConnectorEnum {
    ZERO,
    S,
    D,
    U;

    /**
     * This function returns the value of the connector.
     * @return the value of the connector
     * 0 = no connector
     * 1 = single connector
     * 2 = double connector
     * 3 = triple connector
     */
    public int getValue() {
        return switch (this) {
            case ZERO -> 0;
            case S -> 1;
            case D -> 2;
            case U -> 3;
            default -> -1; // This should never happen
        };
    }
}