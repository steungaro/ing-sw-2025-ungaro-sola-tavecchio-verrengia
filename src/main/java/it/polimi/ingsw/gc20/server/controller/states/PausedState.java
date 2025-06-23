package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.DrawCardPhaseMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.StandbyMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Represents a paused state in the game, often occurring when players disconnect or the game is temporarily halted.
 * During this state, no game actions can proceed until the game is resumed or concluded.
 */
public class PausedState extends State {
    final State previousState;
    private final ScheduledExecutorService scheduler;
    private final ScheduledFuture <?> future;

    /**
     * Shuts down the scheduled tasks and scheduler associated with the paused state.
     */
    private void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            if (future != null && !future.isDone()) {
                future.cancel(false);
            }
            scheduler.shutdown();
        }
    }

    /**
     * Constructs a PausedState instance representing a temporary halt in the game, such as when all players
     * disconnect. This state notifies the game of the pause and schedules an automatic transition to an
     * EndgameState if the game is not resumed within a specified time limit.
     *
     * @param previousState the state of the game before it was paused
     * @param model the game model containing the current state of the game's data
     * @param controller the game controller responsible for managing game logic and transitions
     */
    public PausedState(State previousState, GameModel model, GameController controller) {
        super(model, controller);
        this.previousState = previousState;
        this.phase = StatePhase.STANDBY_PHASE;
        getController().getMessageManager().broadcastPhase(new StandbyMessage("Everyone disconnected. Waiting for the game to resume."));
        // If nobody reconnects in 90 seconds, the remaining player wins the game
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        future = scheduler.schedule(() -> {
            getController().setState(new EndgameState(getController()));
            shutdown();
        }, 90, TimeUnit.SECONDS);
    }

    /**
     * Resumes the game from a paused state by restoring the previous game state
     * and performing necessary actions based on the type of state.
     *
     * @param reconnected the username of the player who reconnected
     */
    public void resume(String reconnected) {
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
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

    @Override
    public boolean isConcurrent(){
        return previousState.isConcurrent();
    }

}
