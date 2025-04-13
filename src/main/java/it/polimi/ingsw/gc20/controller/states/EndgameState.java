package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.HashMap;
import java.util.Map;

public class EndgameState extends State {
    /**
     * Default constructor
     */
    public EndgameState(GameController controller) {
        super(controller);
        MatchController.getInstance().endGame(controller.getGameID());
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
