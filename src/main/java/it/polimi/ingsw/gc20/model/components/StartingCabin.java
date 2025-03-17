package it.polimi.ingsw.gc20.model.components;
import it.polimi.ingsw.gc20.model.bank.Alien;
import it.polimi.ingsw.gc20.model.bank.Astronaut;

import java.util.ArrayList;
import java.util.List;

public class StartingCabin extends Cabin {

    private List<Astronaut> astronauts;

    public StartingCabin() {
        super();
        astronauts = new ArrayList<Astronaut>();
    }

    @Override
    public void setAlien(Alien a) {
        throw new IllegalArgumentException("Cannot set an alien in the starting cabin");
    }

    @Override
    public Alien getAlien() {
        return null;
    }

    @Override
    public void unloadAlien(Alien a) {
        throw new IllegalArgumentException("Cannot unload an alien in the starting cabin");
    }

    /**
     * Function that adds support to the cabin.
     * @param ls the lifeSupport that's added
     */
    @Override
    public void addSupport(LifeSupport ls) {
        super.setColor(AlienColor.NONE);
    }

    @Override
    public void removeSupport(LifeSupport ls) {
        super.setColor(AlienColor.NONE);
    }

}