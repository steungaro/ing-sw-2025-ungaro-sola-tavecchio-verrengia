package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.PlayerUpdateMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.StandbyMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.player.Player;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Represents the state of the game before a player draws a card.
 * In this state, a timer is started. When the timer expires, the game proceeds to the next round.
 * It also handles player status updates based on their position and astronauts.
 */
public class PreDrawState extends State{
    ScheduledExecutorService scheduler;
    ScheduledFuture<?> future;

    /**
     * Shuts down the scheduler and cancels any scheduled tasks.
     * This method should be called when the game is over or when transitioning to a different state
     * to ensure that no further actions are taken after the game has ended.
     */
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            if (future != null && !future.isDone()) {
                future.cancel(true);
            }
            scheduler.shutdown();
        }
    }

    /**
     * Constructs a PreDrawState.
     * It initializes a scheduler that will call the {@link #nextRound()} method after a 5-second delay.
     * It also notifies the controller to let it connect the players that are in the queue.
     * @param controller The game controller.
     */
    public PreDrawState(GameController controller) {
        super(controller);
        getController().preDrawConnect();
        // scheduler executor in 5 seconds call nextRound()
        scheduler = Executors.newSingleThreadScheduledExecutor();
        future = scheduler.schedule(this::nextRound, 5, java.util.concurrent.TimeUnit.SECONDS);
    }

    /**
     * Transitions the game to the next round.
     * This method sorts players by their current position on the board.
     * It then checks each player (except the leader) to see if they have been lapped by the leader
     * or if they have run out of astronauts. If either condition is met, the player's looses
     * and an update message is sent to all connected players.
     * Finally, it instructs the game controller to proceed with drawing a card.
     */
    @Override
    public void nextRound(){
        getModel().getGame().sortPlayerByPosition();
        for (Player p : getModel().getGame().getPlayers()) {
            if (getModel().getGame().getPlayers().getFirst().getPosition()-p.getPosition() >= getModel().getGame().getBoard().getSpaces()){
                p.setGameStatus(false);
                getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(p.getUsername(), 0, p.isInGame(), p.getColor(), (p.getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces()));
                getController().getMessageManager().sendToPlayer(p.getUsername(), new StandbyMessage("You have been lapped by the leader. You retired from the game."));
            }
            if (p.getShip().getAstronauts()==0){
                p.setGameStatus(false);
                getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(p.getUsername(), 0, p.isInGame(), p.getColor(), (p.getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces()));
                getController().getMessageManager().sendToPlayer(p.getUsername(), new StandbyMessage("you don't have any humans left on the ship. You retired from the game"));
            }
        }
        getController().drawCard();
    }
}
