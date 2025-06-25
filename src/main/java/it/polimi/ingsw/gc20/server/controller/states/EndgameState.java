package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.LeaderboardMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.MatchController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents the endgame state of the game, which is reached when the game concludes.
 * This state is responsible for calculating and broadcasting the final scores of players,
 * and it schedules a task to end the game after a specified timeout.
 */
public class EndgameState extends State {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * Constructs an EndgameState instance, which is the final state of the game.
     * This state is reached when the game ends, either due to all players disconnecting
     * or when the game is manually ended. It schedules a task to end the game after a timeout.
     *
     * @param controller the game controller responsible for managing game logic and transitions
     */
    public EndgameState(GameController controller) {
        super(controller);
        getScore();
        scheduler.schedule(this::killGame, 30, TimeUnit.SECONDS);
    }

    @Override
    public void killGame() {
        MatchController.getInstance().endGame(getController().getGameID());
        scheduler.shutdown();
    }


    @Override
    public void getScore() {
        Map<String, Integer> score = getModel().calculateScore()
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey().getUsername(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        getController().getMessageManager().broadcastUpdate(new LeaderboardMessage(score));
    }
}
