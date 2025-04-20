package it.polimi.ingsw.gc20.model.components;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;

import java.util.List;

public class SpecialCargoHold extends CargoHold {

    public SpecialCargoHold() {
        super();
    }


    /**
     * This method is used to load a cargo in the cargo hold
     * @param g the cargo to be loaded
     * @throws IllegalArgumentException if the cargo hold cannot hold red cargo
     */
    @Override
    public void loadCargo(CargoColor g) throws IllegalArgumentException {
        if (this.availableSlots == 0) {
            throw new IllegalArgumentException("CargoHold is full");
        }
        cargoHeld.put(g, cargoHeld.getOrDefault(g, 0) + 1);
        this.availableSlots--;
    }
}