package it.polimi.ingsw.gc20.server.controller.managers;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.StandbyMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.states.StatePhase;
import it.polimi.ingsw.gc20.server.controller.states.State;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.network.NetworkService;

/**
 * This class is responsible for managing messages sent to players.
 * It handles the notification of phase changes, broadcasting updates,
 * and sending messages to specific players.
 */
public class MessageManager {
    private final GameController gameController;

    /**
     * Constructor for MessageManager.
     *
     * @param gameController the GameController instance that manages the game state
     */
    public MessageManager(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Notifies all players about a phase change in the game.
     * Sends a message to the current player and a standby message to others.
     *
     * @param statePhase the phase of the state that is changing
     * @param state      the current state of the game
     */
    public void notifyPhaseChange(StatePhase statePhase, State state){
        for (String username : gameController.getInGameConnectedPlayers()) {
            try {
                if (username.equals(state.getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, statePhase.createMessage(state));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage(state.getStandbyMessage()));
                }
            } catch (InvalidStateException e){
                // This should never happen, as the state is always valid when the phase changes
                e.printStackTrace();
            }
        }
    }

    /**
     * Broadcasts an update model message to all players in the game.
     *
     * @param message the message to be sent to all players
     */
    public void broadcastUpdate(Message message){
        for (Player player : gameController.getPlayers()) {
            NetworkService.getInstance().sendToClient(player.getUsername(), message);
        }
    }

    /**
     * Broadcasts a phase message to all players in the game.
     * This is typically used to notify players of a phase change during the start of a new state
     * @param message the message to be sent to all players
     */
    public void broadcastPhase (Message message){
        for (String username : gameController.getInGameConnectedPlayers()) {
            NetworkService.getInstance().sendToClient(username, message);
        }
    }

    /**
     * Sends a message to a specific player.
     * This is used for targeted communication, such as sending an error message when a player tries to perform an invalid action.
     * @param username the username of the player to whom the message will be sent
     * @param message  the message to be sent
     */
    public void sendToPlayer(String username, Message message) {
        NetworkService.getInstance().sendToClient(username, message);
    }
}
