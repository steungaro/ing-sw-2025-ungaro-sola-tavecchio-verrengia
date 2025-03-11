package it.polimi.ingsw.gc20.model.bank;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.components.StartingCabin;
import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public class Crew {

    protected Component component;

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
        if (c instanceof Cabin) {
            this.component = c;
        } else if (c instanceof StartingCabin) {
            this.component = c;
        } else {
            throw new IllegalArgumentException("The component is not a cabin");
        }
    }

    /** get function for the cabin of the crew member
     * @return Component cabin of the crew member
     */
    public Component getCabin() {
        return this.component;
    }

}