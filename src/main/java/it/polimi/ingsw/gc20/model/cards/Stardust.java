package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.states.EpidemicState;
import it.polimi.ingsw.gc20.controller.states.StardustState;
import it.polimi.ingsw.gc20.controller.states.State;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.ship.Ship;

/**
 * @author GC20
 */
public class Stardust extends AdventureCard {

    /**
     * Default constructor
     */
    public Stardust() {
        super();
    }

    /**
     * @param controller
     */
    @Override
    public void setState(GameController controller, GameModel model) {
        State state = new StardustState(controller, model);
        controller.setState(state);
        try {
            Thread.sleep(5000); // Sleep for 5 seconds (5000 milliseconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        state.automaticAction();
    }
}