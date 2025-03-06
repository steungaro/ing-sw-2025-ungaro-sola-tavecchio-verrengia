package it.polimi.ingsw.gc20.model.components;

public class Battery extends Component {

    private Integer energy;
    private Integer slots;

    public Battery() {
    }

    /**
     * Function that creates a new battery with the given parameters.
     * @param q the initial energy of the battery
     * @return the new battery
     */
    public void useEnergy(Integer q) {
        setEnergy(getEnergy() - q);
    }

    public Integer getEnergy() {
        return energy;
    }

    public void setEnergy(Integer q) {
        energy = q;
    }

    public Integer getSlots() {
        return slots;
    }

    public void setSlots(Integer q) {
        slots = q;
    }

}