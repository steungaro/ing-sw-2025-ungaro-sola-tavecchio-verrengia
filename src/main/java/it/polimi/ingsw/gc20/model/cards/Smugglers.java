package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.states.SmugglersState;
import it.polimi.ingsw.gc20.model.gamesets.*;

import java.util.*;

/**
 * @author GC20
 */
public class Smugglers extends AdventureCard implements Enemy {
    private int lostCargo;
    private int firePower;
    private int lostDays;
    private List<CargoColor> reward;

    /**
     * Default constructor
     */
    public Smugglers() {
        super();
        lostCargo = 0;
        firePower = 0;
        lostDays = 0;
        reward = new ArrayList<>();
    }

    @Override
    public void setState(GameController controller, GameModel model) {
        controller.setState(new SmugglersState(controller, model, lostCargo, firePower, lostDays, reward));
    }

    /**
     * Setter method for lostCargo
     * @param lostCargo lostCargo
     */
    public void setLostCargo(int lostCargo) {
        this.lostCargo = lostCargo;
    }

    /**
     * Getter method for lostCargo
     * @return lostCargo
     */
    public int getLostCargo() {
        return lostCargo;
    }

    /**
     * Setter method for firePower
     * @param firePower firePower
     */
    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    /**
     * Getter method for firePower
     * @return firePower
     */
    public int getFirePower() {
        return firePower;
    }

    /**
     * Setter method for lostDays
     * @param lostDays lostDays
     */
    public void setLostDays(int lostDays) {
        this.lostDays = lostDays;
    }

    /**
     * Getter method for lostDays
     * @return lostDays
     */
    public int getLostDays() {
        return lostDays;
    }

    /**
     * Setter method for reward
     * @param reward reward
     * @implNote reward has to be already ordered by value
     */
    public void setReward(List<CargoColor> reward) {
        this.reward = reward;
    }

    /**
     * Getter method for reward
     * @return reward
     */
    public List<CargoColor> getReward() {
        return reward;
    }
}