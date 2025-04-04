package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.model.gamesets.CargoColor;

import java.util.List;

public class AbandonedStationState extends State {
    private final int crewNeeded;
    private final List<CargoColor> reward;
    private final int lostDays;


    /**
     * Default constructor
     */
    public AbandonedStationState(int crewNeeded, List<CargoColor> reward, int lostDays) {
        super();
        this.crewNeeded = crewNeeded;
        this.reward = reward;
        this.lostDays = lostDays;
    }

    @Override
    public String toString() {
        return "AbandonedStationState";
    }
}
