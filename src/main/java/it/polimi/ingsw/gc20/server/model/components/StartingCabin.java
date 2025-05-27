package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.server.exceptions.InvalidAlienPlacement;

public class StartingCabin extends Cabin {

    public StartingCabin() {
        this.ID = 1000;
    }

    /** Function that try to set an alien in the cabin.
     * @param color the color of the alien
     * @throws InvalidAlienPlacement because the starting cabin cannot have an alien
     */
    @Override
    public void setAlien(AlienColor color) throws InvalidAlienPlacement {
        throw new InvalidAlienPlacement("Cannot set an alien in the starting cabin");
    }

    /**
     * Function that returns the alien in the cabin.
     * @return false because the starting cabin cannot have an alien
     */
    @Override
    public boolean getAlien() {
        return false;
    }

    /**
     * Function that returns the color of the alien in the cabin.
     * @return NONE because the starting cabin cannot have an alien
     */
    @Override
    public AlienColor getAlienColor() {
        return AlienColor.NONE;
    }

    /**
     * Function that unload alien from the cabin.
     * @throws InvalidAlienPlacement because the starting cabin cannot have an alien
     */
    @Override
    public void unloadAlien() throws InvalidAlienPlacement {
        throw new InvalidAlienPlacement("Cannot unload an alien in the starting cabin");
    }

    /**
     * Function that adds support to the cabin.
     * @param ls the lifeSupport that's added
     */
    @Override
    public void addSupport(LifeSupport ls) {
        super.setColor(AlienColor.NONE);
    }

    /**
     * Function that removes support from the cabin.
     * @param ls the lifeSupport that's removed
     */
    @Override
    public void removeSupport(LifeSupport ls) {
        super.setColor(AlienColor.NONE);
    }

}