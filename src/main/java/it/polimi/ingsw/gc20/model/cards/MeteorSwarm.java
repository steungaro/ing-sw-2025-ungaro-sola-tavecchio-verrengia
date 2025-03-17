package it.polimi.ingsw.gc20.model.cards;

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

    public void setMeteors(List<Projectile> meteors) {
        this.meteors = meteors;
    }

    public List<Projectile> getMeteors() {
        return meteors;
    }


    /**
     * @return the list of meteors
     * @apiNote the controller needs to scroll through the list of meteors and apply the effects observing the rules
     * @see Projectile
     */
    public List<Projectile> Effect() {
        return getMeteors();
    }

}