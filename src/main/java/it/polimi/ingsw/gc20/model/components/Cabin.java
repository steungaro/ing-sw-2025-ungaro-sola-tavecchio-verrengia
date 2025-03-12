package it.polimi.ingsw.gc20.model.components;

import it.polimi.ingsw.gc20.model.bank.Alien;
import it.polimi.ingsw.gc20.model.bank.Astronaut;

import java.util.*;

public class Cabin extends Component {

    private List<Astronaut> astronauts;
    private Alien alien;
    private AlienColor color;

    public Cabin() {
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
        this.astronauts.addAll(astronauts);
    }

    /**
        * Function that adds an astronaut to the cabin.
        * @return astronaut the astronaut to add
     */
    public Alien getAlien() {
        return alien;
    }

    /**
        * Function that sets the aliens in the cabin.
        * @param a the aliens to set
     */
    public void setAliens(Alien a) {
        alien = a;
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
     * @return the color of the cabin
     */
    public void setColor(AlienColor color) {
        this.color = color;
    }

    /**
        * Function that unloads the astronauts from the cabin.
        * @param a the astronaut to unload
     */
    public void unloadAstronauts(Astronaut a) {
        astronauts.remove(a);
    }

    /**
        * Function that unloads the aliens from the cabin.
        * @param a the alien to unload
     */
    public void unloadAliens(Alien a) {
        alien=null;
    }

    /**
        * Function that adds support to the cabin.
        * @param c the color of the support to add
     */
    public void addSupport(AlienColor c) {
        if(c==color || color==AlienColor.BOTH)
            return;

        if((c!=color) && (color!=AlienColor.NONE))
            color=AlienColor.BOTH;
        else
            color=c;
    }

    public Integer getOccupants(){
        return astronauts.size() + (alien == null ? 0 : 1);
    }
}