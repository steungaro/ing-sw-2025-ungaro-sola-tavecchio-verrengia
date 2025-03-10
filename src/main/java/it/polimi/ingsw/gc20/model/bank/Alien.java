package it.polimi.ingsw.gc20.model.bank;

import it.polimi.ingsw.gc20.model.components.AlienColor;
import it.polimi.ingsw.gc20.model.components.Component;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public class Alien extends Crew {
    private Component component;
    private AlienColor color;

    /**
     * Default constructor
     */
    public Alien() {
        super();
        this.color = null;
    }


    /** set funcion for the color of the alien
     * @param c colore dell'alieno
     * @return
     */
    public void setColor(AlienColor c) {
        this.color=c;
    }

    /** get function for the color of the alien
     * @return AlienColor colore dell'alieno
     */
    public AlienColor getColor() {
        return this.color;
    }

    /** set function for the cabin of the crew member
     * @param c cabin for the alien
     * throws IllegalArgumentException if the cabin cannot host alien
     */
    public void setCabin(Component c) throws IllegalArgumentException {
        if (c.color == null) {
            throw new IllegalArgumentException("cabin must have a color to host an alien");
        } else {
            this.component = c;
        }
    }

}