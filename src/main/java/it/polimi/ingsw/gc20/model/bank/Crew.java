package it.polimi.ingsw.gc20.model.bank;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.components.StartingCabin;
import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public class Crew {

    protected Cabin component;

    /**
     * Default constructor
     */
    public Crew() {
        this.component=null;
    }

    /** set function for the cabin of the crew member
     * @param c cabin for the crew member
     * @throws IllegalArgumentException if the component is not a cabin
     */
    public void setCabin(Cabin c) {
        this.component = c;
    }

    /** get function for the cabin of the crew member
     * @return Component cabin of the crew member
     */
    public Cabin getCabin() {
        return this.component;
    }

}