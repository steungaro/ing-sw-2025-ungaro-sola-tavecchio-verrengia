package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.model.cards.Projectile;

import java.util.List;

public class MeteorSwarmState extends State {
    private final List<Projectile> meteors;
    /**
     * Default constructor
     */
    public MeteorSwarmState(List<Projectile> meteors) {
        super();
        this.meteors = meteors;
    }

    @Override
    public String toString() {
        return "MeteorSwarmState";
    }
}
