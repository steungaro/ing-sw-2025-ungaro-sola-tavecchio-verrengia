package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewBattery;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

/**
 * The Battery class represents a component of a ship with rechargeable energy storage functionality.
 * It extends the abstract Component class and provides specific behavior related to energy management.
 */
public class Battery extends Component {

    private int availableEnergy;
    private int slots;

    public Battery() {}

    /**
     * Consumes one unit of energy from the battery by decrementing the current available energy.
     * This method is used to update the energy level after performing an action that requires energy.
     * If energy management or validation is required, ensure checks are implemented before calling this method.
     */
    public void useEnergy() {
        availableEnergy--;
    }

    /**
     * Retrieves the amount of energy currently available in the battery.
     *
     * @return the current available energy of the battery
     */
    public int getAvailableEnergy() {
        return availableEnergy;
    }
    /**
     * Sets the current available energy for the battery.
     *
     * @param q the amount of energy to set as the available energy
     */
    public void setAvailableEnergy(int q) {
        this.availableEnergy = q;
    }

    /**
     * Retrieves the number of slots available in the battery.
     *
     * @return the number of slots in the battery
     */
    public int getSlots() {
        return slots;
    }

    /**
     * Sets the total number of slots for the battery.
     *
     * @param q the number of slots to set for the battery
     */
    public void setSlots(int q) {
        this.slots = q;
    }

    /**
     * Updates the battery parameter of the specified ship by modifying its battery quantity.
     * The change in the battery quantity is determined by the sign parameter and the available energy.
     *
     * @param s the ship whose battery parameter is updated
     * @param sign determines whether to add or subtract energy from the ship's batteries (e.g., +1 to add, -1 to subtract)
     */
    @Override
    public void updateParameter(Ship s, int sign){
        s.addBatteries (sign*availableEnergy);
    }


    /**
     * Creates and initializes a new {@link ViewComponent} that represents this battery.
     * The method sets the available energy and the total slots of the battery view
     * and performs further initialization using the {@code initializeViewComponent} method.
     *
     * @return a {@link ViewComponent} instance representing the current battery state
     */
    @Override
    public ViewComponent createViewComponent() {
        ViewBattery battery= new ViewBattery();
        battery.availableEnergy = availableEnergy;
        battery.totalSlots = slots;
        this.initializeViewComponent(battery);
        return battery;
    }
}