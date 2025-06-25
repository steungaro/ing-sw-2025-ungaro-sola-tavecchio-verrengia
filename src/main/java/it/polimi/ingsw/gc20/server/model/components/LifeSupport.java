package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewLifeSupport;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

/**
 * The LifeSupport class represents a component responsible for maintaining
 * life support systems within a ship. It extends the Component class and
 * provides functionality for managing and updating life support parameters.
 */
public class LifeSupport extends Component {

    private AlienColor color;

    public LifeSupport() {
        super();
        color = AlienColor.NONE;
    }

    /**
     * Retrieves the color of the life support component.
     *
     * @return the current color of the life support, represented as an AlienColor enum value.
     */
    public AlienColor getColor() {
        return color;
    }

    /**
     * Sets the color of the life support component.
     *
     * @param color the new color to set, represented as an AlienColor enum value.
     *              Valid values include PURPLE, BROWN, BOTH, and NONE.
     */
    public void setColor(AlienColor color) {
        this.color = color;
    }

    /**
     * Updates the life support parameters of the given ship based on the specified sign.
     * If the sign is negative, it performs operations for removing life support.
     * If the sign is positive, it performs operations for adding life support.
     *
     * @param ship the ship whose life support parameters are to be updated
     * @param sign an integer indicating the type of update. A negative sign indicates
     *             removal of life support, while a positive sign indicates addition of life support
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
     * Indicates whether the component is a life support component.
     *
     * @return true to signify that this component represents life support functionality.
     */
    @Override
    public boolean isLifeSupport() {
        return true;
    }

    @Override
    public ViewComponent createViewComponent() {
        ViewLifeSupport viewLifeSupport = new ViewLifeSupport();
        viewLifeSupport.color = color;
        initializeViewComponent(viewLifeSupport);
        return viewLifeSupport;
    }
}