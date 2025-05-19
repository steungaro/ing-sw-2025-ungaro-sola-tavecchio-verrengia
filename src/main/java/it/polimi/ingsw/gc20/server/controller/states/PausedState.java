package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.StandbyMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.network.NetworkService;

public class PausedState extends State {
    final State previousState;
    /**
     * Default constructor
     */
    public PausedState(State previousState, GameModel model, GameController controller) {
        super(model, controller);
        this.previousState = previousState;
        this.phase = StatePhase.STANDBY_PHASE;
        for (String username : getController().getInGameConnectedPlayers()) {
            NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for the game to resume"));
        }
    }

    public void resume() {
        getController().setState(previousState);
    }

    @Override
    public String toString() {
        return "PausedState";
    }

}
