package it.polimi.ingsw.gc20.server.model.cards;

/**
 * The FireType enum represents different types of firepower available in the game.
 * It provides the following fire types:
 *  LIGHT_METEOR
 *  HEAVY_METEOR
 *  LIGHT_FIRE
 *  HEAVY_FIRE
 * <p>
 * Each fire type corresponds to a specific type of attack or projectile in the game.
 */
public enum FireType {
    LIGHT_METEOR,
    HEAVY_METEOR,
    LIGHT_FIRE,
    HEAVY_FIRE;

    /**
     * Returns a string representation of the fire type associated with this instance.
     *
     * @return a string describing the fire type, such as "Light Meteor", "Heavy Meteor", "Light Fire",
     * or "Heavy Fire".
     */
    public String getFireType() {
        return switch (this) {
            case LIGHT_METEOR -> "Light Meteor";
            case HEAVY_METEOR -> "Heavy Meteor";
            case LIGHT_FIRE -> "Light Fire";
            case HEAVY_FIRE -> "Heavy Fire";
        };
    }
}
