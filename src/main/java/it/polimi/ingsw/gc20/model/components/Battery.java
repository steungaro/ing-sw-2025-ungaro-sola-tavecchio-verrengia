package it.polimi.ingsw.gc20.model.components;
import it.polimi.ingsw.gc20.model.bank.Energy;
import java.util.List;
import java.util.ArrayList;

public class Battery extends Component {

    private List<Energy> energy;
    private Integer slots;

    public Battery() {
        super();
        energy = new ArrayList<>();
        slots = 0;
    }

    /**
     * Function that creates a new battery with the given parameters.
     * @param e the energy of the battery
     */
    public void useEnergy(Energy e) {
        e.setBattery(null);
        energy.remove(e);
    }

    /**
     * Function that returns the energy of the battery.
     * @return the energy of the battery
     */
    public List<Energy> getEnergy() {
        return energy;
    }

    /**
     * Function that returns the slots of the battery.
     * @return the slots of the battery
     */
    public Integer getSlots() {
        return slots;
    }

    /**
     * Function that sets the slots of the battery.
     * @param q the slots of the battery
     */
    public void setSlots(Integer q) {
        this.slots = q;
    }

    /**
     * Function that sets the energy of the battery.
     * @param energy the energy of the battery
     */
    public void setEnergy(List<Energy> energy) {
        this.energy.addAll(energy);
        for (Energy e : energy) {
            e.setBattery(this);
        }
    }

    /**
     * Function that fills the battery.
     */
    public void fillBattery() {

        Energy e = new Energy();
        energy.add(e);
        e.setBattery(this);
    }
}