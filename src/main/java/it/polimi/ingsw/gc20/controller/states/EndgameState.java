package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.model.gamesets.GameModel;

public class EndgameState extends State {
    /**
     * Default constructor
     */
    public EndgameState() {
        super(new GameModel());
    }

    @Override
    public String toString() {
        return "EndgameState";
    }
}
