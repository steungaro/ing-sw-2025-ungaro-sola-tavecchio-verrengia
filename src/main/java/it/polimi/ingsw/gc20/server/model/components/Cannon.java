package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewCannon;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

/**
 * The Cannon class represents a specific type of component that can be attached to a ship.
 * It extends the Component class and provides functionality specific to cannons,
 * including managing their power and adding it to the ship's capabilities.
 */
public class Cannon extends Component {
    private int power;

    /**
     * Default constructor for the Cannon class.
     * Initializes a new instance of the Cannon object with default properties.
     */
    public Cannon() {}

    /**
     * Returns the power of the cannon.
     *
     * @return the power of the cannon as a float
     */
    public int getPower() {
        return power;
    }

    /**
     * Sets the power level of the cannon.
     *
     * @param power the new power level to be assigned to the cannon
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * Updates the ship's cannon power parameters based on the current configuration of the cannon.
     * The updates depend on the rotation direction and the power level of the cannon.
     *
     * @param s the ship whose parameters will be updated
     * @param sign a modifier indicating the direction of the update (e.g., increase or decrease)
     */
    @Override
    public void updateParameter(Ship s, int sign) {
        if (!(rotation == Direction.UP)){
             if (power == 1) {
                 s.addSingleCannonsPower(0.5f*sign);
             } else{
                    s.addDoubleCannonsPower(sign);
             }
        } else {
            if (power == 1) {
                s.addSingleCannonsPower(sign);
            } else{
                s.addDoubleCannonsPower(2*sign);
            }
        }
    }

    @Override
    public boolean isCannon() {
        return true;
    }

    @Override
    public ViewComponent createViewComponent() {
        ViewCannon viewCannon = new ViewCannon();
        viewCannon.power = power;
        viewCannon.rotation = rotation.getValue();
        initializeViewComponent(viewCannon);
        return viewCannon;
    }

    @Override
    public Boolean isValid (Component c, Direction d) {
        if (d == rotation && c != null){
            return false;
        }
        return super.isValid(c, d);
    }


}