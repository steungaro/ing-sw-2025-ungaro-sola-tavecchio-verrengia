package it.polimi.ingsw.gc20.model.components;

public class Battery extends Component {

    private int availableEnergy;
    private int slots;

    public Battery() {}

    /**
     * Function that uses a power unit.
     */
    public void useEnergy() {
        availableEnergy--;
    }

    /**
     * Function that returns the available energy of the battery.
     * @return the energy of the battery
     */
    public int getAvailableEnergy() {
        return availableEnergy;
    }

    /**
     * Function that returns the slots of the battery.
     * @return the slots of the battery
     */
    public int getSlots() {
        return slots;
    }

    /**
     * Function that sets the slots of the battery.
     * @param q the slots of the battery
     */
    public void setSlots(int q) {
        this.slots = q;
    }

    /**
     * Function that fills the battery.
     */
    public void fillBattery() {
        availableEnergy = slots;
    }
}