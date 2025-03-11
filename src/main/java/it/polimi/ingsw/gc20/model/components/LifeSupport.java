package it.polimi.ingsw.gc20.model.components;

public class LifeSupport extends Component {

    private AlienColor type;

    public LifeSupport() {
        type = AlienColor.NONE;
    }

    /**
     * This function returns the type of the life support
     * @return the type of the life support
     */
    public AlienColor getType() {
        return type;
    }

    /**
     * This function sets the type of the life support
     * @param type the type of the life support
     */
    public void setType(AlienColor type) {
        this.type = type;
    }
}