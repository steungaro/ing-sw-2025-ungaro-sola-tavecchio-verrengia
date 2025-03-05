package it.polimi.ingsw.gc20.model.Components;

import java.io.*;
import java.util.*;

public class Battery extends Component {

    private Integer energy;
    private Integer slots;

    public Battery(Integer InitialEnergy, Integer ID, Integer slots) {
        this.setIDComponent(ID);
        this.energy = InitialEnergy;
        this.slots = slots;
    }

    public void useEnergy(Integer q) {
        energy -= q;
    }

    public Integer getSlots() {
        return slots;
    }

    public Integer getEnergy() {
        return energy;
    }
}