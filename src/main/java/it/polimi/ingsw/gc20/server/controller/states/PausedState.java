package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;

public class PausedState extends State {
    final State previousState;
    /**
     * Default constructor
     */
    public PausedState(State previousState, GameModel model, GameController controller) {
        super(model, controller);
        this.previousState = previousState;
    }

    public void resume() {
        getController().setState(previousState);
    }

    @Override
    public String toString() {
        return "PausedState";
    }

    @Override
    public void setCurrentPlayer(String currentPlayer) {
        // Implementation not needed in paused state
    }

    @Override
    public void nextPlayer() {
        // Implementation not needed in paused state
    }
}
