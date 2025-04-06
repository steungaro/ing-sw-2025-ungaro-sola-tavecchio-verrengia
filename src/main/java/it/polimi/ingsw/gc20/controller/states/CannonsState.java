package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.components.Cannon;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.List;

public abstract class CannonsState extends PlayingState {
    /**
     * Default constructor
     */
    public CannonsState() {
        super();
    }

    /**
     * @param model
     * @param controller
     */
    public CannonsState(GameModel model, GameController controller) {
        super(model, controller);
    }

    @Override
    public String toString() {
        return "CannonsState";
    }

    @Override
    public void activateCannons(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        //TODO
    }
}
