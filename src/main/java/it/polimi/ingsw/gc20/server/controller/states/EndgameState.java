package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.MatchController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.stream.Collectors;

public class EndgameState extends State {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    /**
     * Default constructor
     */
    public EndgameState(GameController controller) {
        super(controller);
        scheduler.schedule(this::killGame, 30, TimeUnit.SECONDS);
    }

    /**
     * this method will kill the game after 30 seconds the endgame state is reached
     */
    @Override
    public void killGame() {
        MatchController.getInstance().endGame(getController().getGameID());
        scheduler.shutdown();
    }

    @Override
    public String toString() {
        return "EndgameState";
    }

    /** this method returns the score of the game
     * @return a map with the username and the score of each player
     */
    @Override
    public Map<String, Integer> getScore() {
        return getModel().calculateScore()
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey().getUsername(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
