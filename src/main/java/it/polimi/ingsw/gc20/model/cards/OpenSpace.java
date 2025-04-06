package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.states.MeteorSwarmState;
import it.polimi.ingsw.gc20.controller.states.OpenSpaceState;
import it.polimi.ingsw.gc20.controller.states.State;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

/**
 * @author GC20
 */
public class OpenSpace extends AdventureCard {

    /**
     * Default constructor
     */
    public OpenSpace() {
    }

    /**
     * @param controller
     */
    @Override
    public void setState(GameController controller, GameModel model) {
        State state = new OpenSpaceState(controller, model);
        controller.setState(state);
    }
}