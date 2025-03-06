package it.polimi.ingsw.gc20.model.components;
import it.polimi.ingsw.gc20.model.gamesets.Cargo;

import java.util.*;

public class SpecialCargoHold extends CargoHold {

    public SpecialCargoHold() {
    }

    public void loadCargo(Cargo g) {
        this.getCargoHeld().add(g);
    }
}