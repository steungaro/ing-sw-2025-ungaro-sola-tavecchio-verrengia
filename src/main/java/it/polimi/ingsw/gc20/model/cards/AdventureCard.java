package it.polimi.ingsw.gc20.model.cards;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import it.polimi.ingsw.gc20.controller.states.*;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Planets.class, name = "Planets"),
        @JsonSubTypes.Type(value = OpenSpace.class, name = "OpenSpace"),
        @JsonSubTypes.Type(value = AbandonedShip.class, name = "AbandonedShip"),
        @JsonSubTypes.Type(value = AbandonedStation.class, name = "AbandonedStation"),
        @JsonSubTypes.Type(value = Stardust.class, name = "Stardust"),
        @JsonSubTypes.Type(value = Epidemic.class, name = "Epidemic"),
        @JsonSubTypes.Type(value = CombatZone.class, name = "CombatZone"),
        @JsonSubTypes.Type(value = Slavers.class, name = "Slavers"),
        @JsonSubTypes.Type(value = Smugglers.class, name = "Smugglers"),
        @JsonSubTypes.Type(value = Pirates.class, name = "Pirates"),
        @JsonSubTypes.Type(value = MeteorSwarm.class, name = "MeteorSwarm")
})

/**
 * @author GC20
 */
public abstract class AdventureCard {

    private int level;
    private int IDCard;
    private boolean played;

    /**
     * Default constructor
     * All the attributes are set to 0
     */
    public AdventureCard() {
        this.level = 0;
        this.IDCard = 0;
        this.played = false;
    }

    /**
     * Public getter for the level attribute
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Public getter for the IDCard attribute
     * @return IDCard
     */
    public int getIDCard() {
        return IDCard;
    }

    /**
     * Public setter for the level attribute
     * @param level the level to set
     * @throws IllegalArgumentException if the level is not 0, 1 or 2
     */
    public void setLevel(int level) {
        if (level != 0 && level != 1 && level != 2) {
            throw new IllegalArgumentException("Invalid level");
        }
        this.level = level;
    }

    public abstract void setState(State gameState);

    /**
     * Public getter for the played attribute
     * @return played
     */
    public boolean isPlayed() {
        return played;
    }

    /**
     * Play the card
     */
    public void playCard() {
        this.played = true;
    }

    /**
     * Public setter for the IDCard attribute
     * @param IDCard the IDCard to set
     */
    public void setIDCard(int IDCard) {
        this.IDCard = IDCard;
    }
}