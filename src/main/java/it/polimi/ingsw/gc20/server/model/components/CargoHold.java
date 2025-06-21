package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewCargoHold;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.exceptions.CargoFullException;
import it.polimi.ingsw.gc20.server.exceptions.CargoNotLoadable;
import it.polimi.ingsw.gc20.server.exceptions.InvalidCargoException;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

import java.util.*;

/**
 * The CargoHold class represents a storage compartment for handling different types of cargo
 * in a ship. It is responsible for managing cargo load/unload operations as well as tracking
 * the current capacity and available slots.
 * <p>
 * This class extends the Component class and maintains a map to track the amount of different
 * types of cargo being held.
 */
public class CargoHold extends Component {
    protected final Map<CargoColor, Integer> cargoHeld = new HashMap<>();
    protected int slots;
    protected int availableSlots;

    public CargoHold() {}

    /**
     * Retrieves the cargo currently held in the cargo hold.
     *
     * @return a map where the keys represent the colors of the cargo (CargoColor)
     *         and the values represent the quantities of each corresponding cargo.
     */
    public Map<CargoColor, Integer> getCargoHeld() {
        return cargoHeld;
    }

    /**
     * Retrieves the quantity of cargo held for a specific color in the cargo hold.
     *
     * @param color the color of the cargo (CargoColor) to retrieve the quantity for
     * @return the quantity of cargo held for the specified color
     */
    public int getCargoHeld(CargoColor color) {
        return cargoHeld.get(color);
    }

    /**
     * Retrieves the total slots in the cargo hold.
     *
     * @return the total number of slots in the cargo hold
     */
    public int getSlots() {
        return slots;
    }

    /**
     * Retrieves the number of available slots in the cargo hold.
     *
     * @return the number of available slots
     */
    public int getAvailableSlots() {
        return availableSlots;
    }

    /**
     * Sets the total number of slots for the cargo hold and updates the available slots to match.
     *
     * @param slots the total number of slots to set in the cargo hold
     */
    public void setSlots(int slots) {
        this.slots = slots;
        this.availableSlots = slots;
    }


    /**
     * Loads a cargo into the cargo hold. The method adds the cargo of the specified color to the cargo hold
     * if there are available slots and the color is not restricted.
     *
     * @param g the color of the cargo to be loaded
     * @throws CargoFullException if the cargo hold is full
     * @throws CargoNotLoadable if the cargo color is not allowed to be loaded
     */
    public void loadCargo(CargoColor g) throws CargoFullException, CargoNotLoadable {
        if (g == CargoColor.RED) {
            throw new CargoNotLoadable("CargoHold cannot hold red cargo");
        }
        if (this.availableSlots == 0) {
            throw new CargoFullException("CargoHold is full");
        }
        cargoHeld.put(g, cargoHeld.getOrDefault(g, 0) + 1);
        this.availableSlots--;
    }

    /**
     * Unloads a cargo of the specified color from the cargo hold. The method reduces the quantity
     * of the specified cargo in the cargo hold and increases the available slots. If the specified
     * cargo is not present or its quantity is zero, an exception is thrown.
     *
     * @param c the color of the cargo to be unloaded
     * @throws InvalidCargoException if the specified cargo is not present in the cargo hold
     *                               or its quantity is zero
     */
    public void unloadCargo(CargoColor c) throws InvalidCargoException {
        if (!cargoHeld.containsKey(c) || cargoHeld.get(c) == 0) {
            throw new InvalidCargoException("Cargo not in cargo hold");
        }
        cargoHeld.put(c, cargoHeld.get(c) - 1);
        this.availableSlots++;
    }

    /**
     * Updates the cargo hold state based on the provided sign.
     * If the sign is negative, it attempts to unload all types of cargo currently held in the cargo hold.
     * Each type of cargo is unloaded into the specified ship.
     *
     * @param ship the ship to transfer the unloaded cargo to
     * @param sign the operation indicator; a negative value signifies the unloading of cargo
     */
    @Override
    public void updateParameter (Ship ship, int sign) {
        if (sign < 0) {
            for (CargoColor c : cargoHeld.keySet()) {
                if (cargoHeld.get(c) > 0) {
                    try {
                        ship.unloadCargo(c, this);
                    } catch (InvalidCargoException _) {}
                }

            }
        }
    }

    @Override
    public ViewComponent createViewComponent() {
        ViewCargoHold viewCargoHold = new ViewCargoHold();
        viewCargoHold.red = cargoHeld.getOrDefault(CargoColor.RED, 0);
        viewCargoHold.green = cargoHeld.getOrDefault(CargoColor.GREEN, 0);
        viewCargoHold.blue = cargoHeld.getOrDefault(CargoColor.BLUE, 0);
        viewCargoHold.yellow = cargoHeld.getOrDefault(CargoColor.YELLOW, 0);
        viewCargoHold.free = availableSlots;
        initializeViewComponent(viewCargoHold);
        return viewCargoHold;
    }
}