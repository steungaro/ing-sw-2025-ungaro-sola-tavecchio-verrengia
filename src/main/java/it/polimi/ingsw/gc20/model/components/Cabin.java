package it.polimi.ingsw.gc20.model.components;

import it.polimi.ingsw.gc20.exceptions.DeadAlienException;
import it.polimi.ingsw.gc20.model.bank.Alien;
import it.polimi.ingsw.gc20.model.bank.Astronaut;

import java.util.*;

public class Cabin extends Component {
    private List<Astronaut> astronauts;
    private Alien alien;
    private AlienColor color;

    public Cabin() {
        super();
        astronauts = new ArrayList<>();
        alien=null;
        color=AlienColor.NONE;
    }

    /**
     * Function that returns the astronauts in the cabin.
     * @return the astronauts in the cabin
     */
    public List<Astronaut> getAstronauts() {
        return astronauts;
    }

    /**
    * Function that sets the astronauts in the cabin.
    * @param astronauts the astronauts to set
     */
    public void setAstronauts(List<Astronaut> astronauts) {
        if (alien != null) {
            throw new IllegalArgumentException("Cannot have both astronauts and aliens in the same cabin");
        }
        if (astronauts.size() > 2) {
            throw new IllegalArgumentException("Cannot have more than 2 astronauts in the same cabin");

        }
        this.astronauts.addAll(astronauts);

        astronauts.get(0).setCabin(this);
        astronauts.get(1).setCabin(this);
    }

    /**
    * Getter method for alien parameter.
    * @return the alien in the cabin
     */
    public Alien getAlien() {
        return alien;
    }

    /**
    * Function that sets the alien in the cabin.
    * @param a the aliens to set
     */
    public void setAlien(Alien a) {
        if (!astronauts.isEmpty()) {
            throw new IllegalArgumentException("Cannot have both astronauts and aliens in the same cabin.");
        }
        if (color != alien.getColor() && color != AlienColor.BOTH) {
            throw new IllegalArgumentException("Cannot have " + alien.getColor().toString() + " in " + color.toString() + "cabin.");
        }
        alien = a;
        a.setCabin(this);
    }

    /**
     * Function that returns the color of the cabin.
     * @return the color of the cabin
     */
    public AlienColor getColor() {
        return color;
    }

    /**
     * Function that sets the color of the cabin.
     * @param color is the color to set
     * @implNote if the cabin already has a different color, it will be set to BOTH
     */
    public void setColor(AlienColor color) {
        if (this.color != AlienColor.NONE && this.color != color) {
            this.color = AlienColor.BOTH;
        } else {
            this.color = color;
        }
    }

    /**
    * Function that unloads the astronauts from the cabin.
    * @param a the astronaut to unload
     */
    public void unloadAstronaut(Astronaut a) {
        a.setCabin(null);
        astronauts.remove(a);
    }

    /**
    * Function that unloads the alien from the cabin.
    * @param a the alien to unload
     */
    public void unloadAlien(Alien a) {
        alien.setCabin(null);
        alien=null;
    }

    /**
        * Function that adds support to the cabin.
        * @param ls the lifeSupport that's added
     */
    public void addSupport(LifeSupport ls) {
        if (color != AlienColor.NONE && color != ls.getType()) {
            color = AlienColor.BOTH;
        } else {
            color = ls.getType();
        }
    }

    /**
     * Function that removes support to the cabin.
     * @param ls the lifeSupport that's removed
     * @throws DeadAlienException if the alien dies because of lack of support
     */
    public void removeSupport(LifeSupport ls) throws DeadAlienException {
        if (color == ls.getType()) {
            color = AlienColor.NONE;
        } else { // color == BOTH
            color = ls.getType() == AlienColor.BROWN ? AlienColor.PURPLE : AlienColor.BROWN;
            if (alien.getColor() != ls.getType()) {
                throw new DeadAlienException("Alien died because of lack of support");
            }
        }
    }

    public int getOccupants(){
        return astronauts.size() + (alien == null ? 0 : 1);
    }
}