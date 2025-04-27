package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

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

    /**
     * Function that update the parameter of the ship
     * @param ship ship that is updating his parameter
     * @param sign integer that indicate if the parameter is increasing or decreasing
     */
    @Override
    public void updateParameter(Ship ship, int sign) {
        if (sign < 0) {
            ((NormalShip) ship).updateLifeSupportRemoved(this);
        } else if (sign > 0) {
            ((NormalShip) ship).updateLifeSupportAdded(this);
        }
    }

    /**
     * Function that returns true if the component is a life support
     * @return true if the component is a life support
     */
    @Override
    public boolean isLifeSupport() {
        return true;
    }
}