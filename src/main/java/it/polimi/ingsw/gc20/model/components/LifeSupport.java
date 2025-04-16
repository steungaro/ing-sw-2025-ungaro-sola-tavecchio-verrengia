package it.polimi.ingsw.gc20.model.components;

import it.polimi.ingsw.gc20.model.ship.NormalShip;
import it.polimi.ingsw.gc20.model.ship.Ship;

public class LifeSupport extends Component {

    private AlienColor color;

    public LifeSupport() {
        super();
        color = AlienColor.NONE;
    }

    /**
     * This function returns the type of the life support
     * @return the type of the life support
     */
    public AlienColor getColor() {
        return color;
    }

    /**
     * This function sets the type of the life support
     * @param color the type of the life support
     */
    public void setColor(AlienColor color) {
        this.color = color;
    }

    @Override
    public void updateParameter(Ship ship, int sign) {
        if (sign < 0) {
            ((NormalShip) ship).updateLifeSupportRemoved(this);
        } else if (sign > 0) {
            ((NormalShip) ship).updateLifeSupportAdded(this);
        }
    }

    @Override
    public boolean isLifeSupport() {
        return true;
    }
}