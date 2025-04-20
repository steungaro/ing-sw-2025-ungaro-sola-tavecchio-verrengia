package it.polimi.ingsw.gc20.model.components;

import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.ship.Ship;

import java.security.InvalidParameterException;
import java.util.*;

public class CargoHold extends Component {
    protected Map<CargoColor, Integer> cargoHeld = new HashMap<>();
    protected int slots;
    protected int availableSlots;

    public CargoHold() {}

    /**
     * Function that returns the cargo in the cargo hold.
     * @return the cargo in the cargo hold
     */
    public Map<CargoColor, Integer> getCargoHeld() {
        return cargoHeld;
    }

    /**
     * Function that returns the cargo in the cargo hold.
     * @return the cargo in the cargo hold
     */
    public int getCargoHeld(CargoColor color) {
        return cargoHeld.get(color);
    }

    /**
     * Function that returns the space of the cargo hold.
     * @return the space of the cargo hold
     */
    public int getSlots() {
        return slots;
    }

    /**
     * Function that returns the available slots in the cargo hold.
     * @return the available slots in the cargo hold
     */
    public int getAvailableSlots() {
        return availableSlots;
    }

    /**
     * Function that sets the space of the cargo hold.
     * @param slots the space of the cargo hold
     */
    public void setSlots(int slots) {
        this.slots = slots;
        this.availableSlots = slots;
    }


    /**
     * This method is used to load a cargo in the cargo hold
     * @param g the cargo to be loaded
     * @throws InvalidParameterException if the cargo hold cannot hold red cargo
     * @throws IllegalArgumentException if the cargo hold is full
     */
    public void loadCargo(CargoColor g) throws IllegalArgumentException, InvalidParameterException {
        if (g == CargoColor.RED) {
            throw new InvalidParameterException("CargoHold cannot hold red cargo");
        }
        if (this.availableSlots == 0) {
            throw new IllegalArgumentException("CargoHold is full");
        }
        cargoHeld.put(g, cargoHeld.getOrDefault(g, 0) + 1);
        this.availableSlots--;
    }

    /**
     * This method is used to unload a cargo from the cargo hold
     * @param c the cargo to be unloaded
     * @throws IllegalArgumentException if the cargo is not in the cargo hold
     */
    public void unloadCargo(CargoColor c) {
        cargoHeld.put(c, cargoHeld.get(c) - 1);
        this.availableSlots++;
    }

    /**
     * Function to update the parameter of the ship
     * @param ship ship that is updating his parameter
     * @param sign integer that indicate if the parameter is increasing or decreasing
     */
    @Override
    public void updateParameter (Ship ship, int sign) {
        if (sign < 0) {
            for (CargoColor c : cargoHeld.keySet()) {
                if (cargoHeld.get(c) > 0) {
                    ship.unloadCargo(c, this);
                }

            }
        }
    }
}