package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.components.Engine;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EnginesState extends PlayingState{
    private final Map<Player, Integer> declaredEngines = new HashMap<>();

    public EnginesState(GameModel model, GameController controller) {
        super(model, controller);
    }

    public abstract void finalEngineAction(Map<Player, Integer> declaredEngines);

    @Override
    public void activateEngines(Player player, List<Engine> engines, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        if (!getCurrentPlayer().equals(player.getUsername())) {
            throw new InvalidTurnException("It's not your turn");
        }
        declaredEngines.put(player, getModel().EnginePower(player, engines.size(), batteries));
        nextPlayer();
        if (getCurrentPlayer() == null) {
            finalEngineAction(declaredEngines);
        }
    }
}
