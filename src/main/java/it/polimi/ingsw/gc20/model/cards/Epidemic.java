package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.states.EpidemicState;
import it.polimi.ingsw.gc20.controller.states.State;
import it.polimi.ingsw.gc20.model.player.Player;

/**
 * @author GC20
 */
public class Epidemic extends AdventureCard {

    /**
     * Default constructor
     */
    public Epidemic() {
    }

    /**
     * @param controller
     */
    @Override
    public void setState(GameController controller) {
        State state = new EpidemicState();
        controller.setState(state);
        try {
            Thread.sleep(5000); // Sleep for 5 seconds (5000 milliseconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        state.automaticAction();
    }

    /**
     * @param p player
     * @implNote The card calls the epidemic method of the player's ship
     */
    public void Effect(Player p) {
        p.getShip().epidemic();
        playCard();
    }

}