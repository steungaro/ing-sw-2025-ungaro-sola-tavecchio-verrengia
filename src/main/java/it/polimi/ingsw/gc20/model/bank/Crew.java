package it.polimi.ingsw.gc20.model.bank;

import it.polimi.ingsw.gc20.model.components.*;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public class Crew {

    private Component component;

    /**
     * Default constructor
     */
    public Crew() {
        this.component=null;
    }

    /** set function for the cabin of the crew member
     * @param c cabin for the crew member
     * throws IllegalArgumentException if the component is not a cabin
     */
    public void setCabin(Component c) {
        if (!(c instanceof Cabin) && !(c instanceof StartingCabin)) {
            throw new IllegalArgumentException("component must be a cabin");
        } else {
            this.component = c;
        }
    }

    /** get function for the cabin of the crew member
     * @return Component cabin of the crew member
     */
    public Component getCabin() {
        return this.component;
    }

}