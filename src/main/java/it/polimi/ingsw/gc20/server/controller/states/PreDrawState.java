package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.PlayerUpdateMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.player.Player;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Represents the state of the game before a player draws a card.
 * In this state, a timer is started. When the timer expires, the game proceeds to the next round.
 * It also handles player status updates based on their position and astronauts.
 */
public class PreDrawState extends State{

    /**
     * Constructs a PreDrawState.
     * It initializes a scheduler that will call the {@link #nextRound()} method after a 5-second delay.
     * It also notifies the controller to let it connect the players that are in queue.
     * @param controller The game controller.
     */
    public PreDrawState(GameController controller) {
        super(controller);
        getController().preDrawConnect();
        // scheduler executor in 5 seconds call nextRound()
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(this::nextRound, 5, java.util.concurrent.TimeUnit.SECONDS);
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
                getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(p.getUsername(), 0, p.isInGame(), p.getColor(), p.getPosition() % getModel().getGame().getBoard().getSpaces()));
            }
            if (p.getShip().getAstronauts()==0){
                p.setGameStatus(false);
                getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(p.getUsername(), 0, p.isInGame(), p.getColor(), p.getPosition() % getModel().getGame().getBoard().getSpaces()));
            }
        }
        getController().drawCard();
    }


    @Override
    public String toString() {
        return "PreDrawState";
    }

}
