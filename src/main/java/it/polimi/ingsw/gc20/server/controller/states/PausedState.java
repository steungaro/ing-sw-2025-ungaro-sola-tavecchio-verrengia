package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.DrawCardPhaseMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.StandbyMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PausedState extends State {
    final State previousState;
    private final ScheduledExecutorService scheduler;

    private void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    /**
     * Default constructor
     */
    public PausedState(State previousState, GameModel model, GameController controller) {
        super(model, controller);
        this.previousState = previousState;
        this.phase = StatePhase.STANDBY_PHASE;
        getController().getMessageManager().broadcastPhase(new StandbyMessage("Everyone disconnected. Waiting for the game to resume."));
        // If nobody reconnects in 30 seconds, the remaining player wins the game
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            getController().setState(new EndgameState(getController()));
            shutdown();
        }, 120, TimeUnit.SECONDS);
    }

    public void resume(String reconnected) {
        shutdown();
        getController().setState(previousState);
        if (previousState.isConcurrent()){
            try {
                previousState.resume(reconnected);
            } catch (InvalidStateException _) {
                //ignore cannot happen
            }
        } else {
            if (getController().getActiveCard().getName().equals("CombatZone")) {
                phase = StatePhase.DRAW_CARD_PHASE;
                getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            }
            getController().getMessageManager().notifyPhaseChange(previousState.getPhase(), previousState);
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
