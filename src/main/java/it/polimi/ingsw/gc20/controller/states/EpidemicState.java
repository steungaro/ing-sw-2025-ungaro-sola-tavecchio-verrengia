package it.polimi.ingsw.gc20.controller.states;

public class EpidemicState extends State {
    /**
     * Default constructor
     */
    public EpidemicState() {
        super();
    }

    @Override
    public String toString() {
        return "EpidemicState";
    }

    public void epidemic(){
    }

    @Override
    public void automaticAction() {
        model.getInGamePlayers().stream()
                .filter(p -> controller.isPlayerDisconnected(p.getUsername()))
                .forEach(p -> {p.getShip().epidemic();});
    }
}
