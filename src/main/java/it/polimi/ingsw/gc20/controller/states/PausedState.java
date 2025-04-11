package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

public class PausedState extends State {
    State previousState;
    /**
     * Default constructor
     */
    public PausedState(State previousState, GameModel model, GameController controller) {
        super(model, controller);
        this.previousState = previousState;
    }

    public State resume() {
        return previousState;
    }

    @Override
    public String toString() {
        return "PausedState";
    }
}
