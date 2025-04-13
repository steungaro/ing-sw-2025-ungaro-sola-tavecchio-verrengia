package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

public class EndgameState extends State {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    /**
     * Default constructor
     */
    public EndgameState(GameController controller) {
        super(controller);
        scheduler.schedule(this::killGame, 30, TimeUnit.SECONDS);
    }

    private void killGame() {
        MatchController.getInstance().endGame(getController().getGameID());
        scheduler.shutdown();
    }

    @Override
    public String toString() {
        return "EndgameState";
    }

    @Override
    public Map<Player, Integer> getScore() {
        return getModel().calculateScore();
    }
}
