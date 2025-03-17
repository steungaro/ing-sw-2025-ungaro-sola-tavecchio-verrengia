package it.polimi.ingsw.gc20.model.bank;

import it.polimi.ingsw.gc20.model.components.*;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * @author GC20
 */
public class Alien extends Crew {
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
     * throws IllegalArgumentException if the component is not a cabin
     * throws InvalidParameterException if the cabin cannot host this type of alien
     */
    public void setCabin(Component c) throws IllegalArgumentException, InvalidParameterException {
        if (!(c instanceof Cabin)) {
            throw new IllegalArgumentException("component must be a cabin");
        } else {
            if (((Cabin) c).getColor()!=this.color) {
                throw new InvalidParameterException("cabin cannot host this type of alien");
            } else {
            this.component = (Cabin) c;
        }
        }
    }

}