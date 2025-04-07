package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.states.MeteorSwarmState;
import it.polimi.ingsw.gc20.controller.states.State;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

import java.util.*;

/**
 * @author GC20
 */
public class
MeteorSwarm extends AdventureCard {
    private List<Projectile> meteors;

    /**
     * Default constructor
     */
    public MeteorSwarm() {
        super();
        meteors = new ArrayList<>();
    }

    /**
     * @param controller
     */
    @Override
    public void setState(GameController controller, GameModel model) {
        State state = new MeteorSwarmState(controller, model, meteors);
        controller.setState(state);
    }

    public void setMeteors(List<Projectile> meteors) {
        this.meteors = meteors;
    }

    public List<Projectile> getMeteors() {
        return meteors;
    }
}