package it.polimi.ingsw.gc20.model.cards;

/**
 * @author GC20
 */
public abstract class AdventureCard {

    private Integer level;
    private Integer IDCard;

    /**
     * Default constructor
     * All the attributes are set to 0
     */
    public AdventureCard() {
        this.level = 0;
        this.IDCard = 0;
    }

    /**
     * Public getter for the level attribute
     * @return level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Public getter for the IDCard attribute
     * @return IDCard
     */
    public Integer getIDCard() {
        return IDCard;
    }

    /**
     * Public setter for the level attribute
     * @param level the level to set
     * @throws IllegalArgumentException if the level is not 0, 1 or 2
     */

    public void setLevel(Integer level) {
        if (level != 0 && level != 1 && level != 2) {
            throw new IllegalArgumentException("Invalid level");
        }
        this.level = level;
    }

    /**
     * Public setter for the IDCard attribute
     * @param IDCard the IDCard to set
     */
    public void setIDCard(Integer IDCard) {
        this.IDCard = IDCard;
    }

}