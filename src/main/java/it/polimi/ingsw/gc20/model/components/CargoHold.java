package it.polimi.ingsw.gc20.model.components;
import it.polimi.ingsw.gc20.model.bank.Cargo;


import java.util.*;

public class CargoHold extends Component {

    protected List<Cargo> cargoHold;
    protected Integer slots;

    public CargoHold() {
        cargoHold = new ArrayList<>();
        slots = 0;
    }

    /**
     * Function that returns the cargo in the cargo hold.
     * @return the cargo in the cargo hold
     */
    public List<Cargo> getCargoHold() {
        return cargoHold;
    }

    /**
     * Function that returns the space of the cargo hold.
     * @return the space of the cargo hold
     */
    public Integer getSpace() {
        return slots;
    }

    /**
     * Function that sets the space of the cargo hold.
     * @param slots the space of the cargo hold
     */
    public void setSpace(Integer slots) {
        this.slots = slots;
    }

    /**
     * Function that sets the cargo in the cargo hold.
     * @param newCargoHold the cargo to set
     */
    public void setCargoHold(List<Cargo> newCargoHold) {
        this.cargoHold.addAll( newCargoHold );
    }

    /**
        * This method is used to load a cargo in the cargo hold
        * @param g the cargo to be loaded
     */
    public void loadCargo(Cargo g) {
        cargoHold.add(g);
    }

    /**
        * This method is used to unload a cargo from the cargo hold
        * @param c the cargo to be unloaded
     */
    public void unloadCargo(Cargo c) {
        cargoHold.remove(c);
    }

    /**
     * This method is used to clean the cargo hold
     * @return the cargo hold
     */
    public void cleanCargo() {
        cargoHold.clear();
    }

}