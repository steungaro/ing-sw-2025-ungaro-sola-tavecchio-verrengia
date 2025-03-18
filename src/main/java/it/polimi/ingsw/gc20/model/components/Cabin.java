package it.polimi.ingsw.gc20.model.components;

import it.polimi.ingsw.gc20.exceptions.DeadAlienException;

public class Cabin extends Component {
    private int astronauts;
    private boolean alien = false;
    private AlienColor alienColor = AlienColor.NONE;
    private AlienColor cabinColor = AlienColor.NONE;

    public Cabin() {}

    /**
     * Function that returns the astronauts in the cabin.
     * @return the astronauts in the cabin
     */
    public int getAstronauts() {
        return astronauts;
    }

    /**
    * Function that sets the astronauts in the cabin.
    * @param astronauts the astronauts to set
     */
    public void setAstronauts(int astronauts) {
        if (alien) {
            throw new IllegalArgumentException("Cannot have both astronauts and aliens in the same cabin");
        }
        if (astronauts > 2) {
            throw new IllegalArgumentException("Cannot have more than 2 astronauts in the same cabin");

        }
        this.astronauts += astronauts;
    }

    /**
    * Getter method for alien parameter.
    * @return the alien color in the cabin
     */
    public boolean getAlien() {
        return alien;
    }

    /**
    * Function that sets the alien in the cabin.
    * @param color is the color of the alien
     */
    public void setAlien(AlienColor color) {
        if (astronauts != 0) {
            throw new IllegalArgumentException("Cannot have both astronauts and aliens in the same cabin.");
        }
        if (cabinColor != color && cabinColor != AlienColor.BOTH) {
            throw new IllegalArgumentException("Cannot have " + color + " alien in " + cabinColor + "cabin.");
        }
        alien = true;
    }

    /**
     * Function that returns the color of the alien in the cabin.
     * @return the color of the alien
     */
    public AlienColor getAlienColor() {
        return alienColor;
    }

    /**
     * Function that returns the color of the alien in the cabin.
     * @return the color of the cabin
     */
    public AlienColor getCabinColor() {
        return cabinColor;
    }

    /**
     * Function that sets the color of the cabin.
     * @param color is the color to set
     * @implNote if the cabin already has a different color, it will be set to BOTH
     */
    public void setColor(AlienColor color) {
        if (cabinColor != AlienColor.NONE && cabinColor != color) {
            cabinColor = AlienColor.BOTH;
        } else {
            cabinColor = color;
        }
    }

    /**
     * Function that unloads one astronaut from the cabin.
     */
    public void unloadAstronaut() {
        astronauts--;
    }

    /**
    * Function that unloads one alien from the cabin.
     */
    public void unloadAlien() {
        alien = false;
        alienColor = AlienColor.NONE;
    }

    /**
     * Function that adds support to the cabin.
     * @param ls the lifeSupport that's added
     */
    public void addSupport(LifeSupport ls) {
        if (cabinColor != AlienColor.NONE && cabinColor != ls.getType()) {
            cabinColor = AlienColor.BOTH;
        } else {
            cabinColor = ls.getType();
        }
    }

    /**
     * Function that removes support to the cabin.
     * @param ls the lifeSupport that's removed
     * @throws DeadAlienException if the alien dies because of lack of support
     */
    public void removeSupport(LifeSupport ls) throws DeadAlienException {
        if (cabinColor == AlienColor.BOTH) {
            cabinColor = ls.getType() == AlienColor.BROWN ? AlienColor.PURPLE : AlienColor.BROWN;
        } else {
            cabinColor = AlienColor.NONE;
        }
        if (alien && cabinColor != alienColor ) {
            alien = false;
            throw new DeadAlienException("Alien died because of lack of support");
        }
    }

    public int getOccupants(){
        return astronauts + (alien ? 1 : 0);
    }
}