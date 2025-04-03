package it.polimi.ingsw.gc20.controller.states;

public class AbandonedShipState extends State{
    int lostCrew;
    int credits;
    int lostDays;

    /**
     * Default constructor
     */
    public AbandonedShipState(int lostCrew, int credits, int lostDays) {
        this.lostCrew = lostCrew;
        this.credits = credits;
        this.lostDays = lostDays;
    }

}
