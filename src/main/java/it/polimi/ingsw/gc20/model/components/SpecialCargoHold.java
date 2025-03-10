package it.polimi.ingsw.gc20.model.components;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;

public class SpecialCargoHold extends CargoHold {

    public SpecialCargoHold() {
    }

    public void loadCargo(CargoColor g) {
        this.getCargoHeld().add(g);
    }
}