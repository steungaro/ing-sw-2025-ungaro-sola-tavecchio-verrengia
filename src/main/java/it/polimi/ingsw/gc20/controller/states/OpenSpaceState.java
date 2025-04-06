package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.*;

public class OpenSpaceState extends EnginesState {

    /**
     * Default constructor
     */
    public OpenSpaceState(GameController controller, GameModel model) {
        super(model, controller);
    }

    @Override
    public String toString() {
        return "OpenSpaceState";
    }

    @Override
    public void finalAction(Map<Player, Integer> declaredEngines) {
//TODO             declaredEngines.entrySet()
//                    .forEach(e -> getModel().move(e.getKey(), e.getValue()));
        getController().drawCard();
    }

}
