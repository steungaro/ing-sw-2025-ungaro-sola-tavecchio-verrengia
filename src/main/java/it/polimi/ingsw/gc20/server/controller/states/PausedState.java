package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.StandbyMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
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

    public void resume(String reconnected) {
        getController().setState(previousState);
        if (previousState.isConcurrent()){
            try {
                previousState.resume(reconnected);
            } catch (InvalidStateException e) {
                //ignore cannot happen
            }
        } else {
            for (String username : getController().getInGameConnectedPlayers()) {
                try {
                    if (username.equals(previousState.getCurrentPlayer())) {
                        NetworkService.getInstance().sendToClient(username, previousState.getPhase().createMessage(previousState));
                    } else {
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + previousState.getCurrentPlayer() + " to finish their turn"));
                    }
                } catch (InvalidStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "PausedState";
    }

    public boolean isConcurrent(){
        return previousState.isConcurrent();
    }

}
