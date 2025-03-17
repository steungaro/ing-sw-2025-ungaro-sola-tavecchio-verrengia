package it.polimi.ingsw.gc20.model.components;
import it.polimi.ingsw.gc20.model.bank.Cargo;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;


import java.util.*;

public class CargoHold extends Component {

    protected List<Cargo> cargoHeld;
    protected int slots;
    protected int availableSlots;

    public CargoHold() {
        super();
        cargoHeld = new ArrayList<>();
        slots = 0;
        availableSlots = 0;
    }

    /**
     * Function that returns the cargo in the cargo hold.
     * @return the cargo in the cargo hold
     */
    public List<Cargo> getCargoHeld() {
        return cargoHeld;
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
     * Function that sets the cargo in the cargo hold.
     * @param newCargoHeld the cargo to set
     */
    public void setCargoHeld(List<Cargo> newCargoHeld) {
        if (newCargoHeld.stream().anyMatch(c -> c.getColor() == CargoColor.RED)) {
            throw new IllegalArgumentException("CargoHold cannot hold red cargo");
        }
        this.cargoHeld.addAll( newCargoHeld );
        this.availableSlots -= cargoHeld.size();
    }

    /**
        * This method is used to load a cargo in the cargo hold
        * @param g the cargo to be loaded
     */
    public void loadCargo(Cargo g) {
        cargoHeld.add(g);
        this.availableSlots--;
    }

    /**
        * This method is used to unload a cargo from the cargo hold
        * @param c the cargo to be unloaded
     */
    public void unloadCargo(Cargo c) {
        cargoHeld.remove(c);
        this.availableSlots++;
    }

    /**
     * This method is used to clean the cargo hold
     * @return the cargo hold
     */
    public void cleanCargo() {
        cargoHeld.clear();
        this.availableSlots = slots;
    }

}