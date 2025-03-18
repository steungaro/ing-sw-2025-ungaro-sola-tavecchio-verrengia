package it.polimi.ingsw.gc20.model.components;

public class StartingCabin extends Cabin {

    public StartingCabin() {}

    @Override
    public void setAlien(AlienColor color) {
        throw new IllegalArgumentException("Cannot set an alien in the starting cabin");
    }

    @Override
    public boolean getAlien() {
        return false;
    }

    @Override
    public AlienColor getAlienColor() {
        return AlienColor.NONE;
    }

    @Override
    public void unloadAlien() {
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