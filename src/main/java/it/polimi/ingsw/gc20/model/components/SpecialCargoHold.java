package it.polimi.ingsw.gc20.model.components;
import it.polimi.ingsw.gc20.model.bank.Cargo;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;

import java.util.List;

public class SpecialCargoHold extends CargoHold {

    public SpecialCargoHold() {
        super();
    }

    /**
     * Function that sets the cargo in the cargo hold.
     * @param newCargoHeld the cargo to set
     */
    @Override
    public void setCargoHeld(List<Cargo> newCargoHeld) {
        this.cargoHeld.addAll(newCargoHeld);
        this.availableSlots -= cargoHeld.size();
    }

}