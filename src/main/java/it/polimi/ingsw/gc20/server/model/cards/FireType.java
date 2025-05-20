package it.polimi.ingsw.gc20.server.model.cards;

public enum FireType {
    LIGHT_METEOR,
    HEAVY_METEOR,
    LIGHT_FIRE,
    HEAVY_FIRE;

    public String getFireType() {
        return switch (this) {
            case LIGHT_METEOR -> "Light Meteor";
            case HEAVY_METEOR -> "Heavy Meteor";
            case LIGHT_FIRE -> "Light Fire";
            case HEAVY_FIRE -> "Heavy Fire";
        };
    }
}
