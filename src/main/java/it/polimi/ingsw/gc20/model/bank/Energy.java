package it.polimi.ingsw.gc20.model.bank;

import it.polimi.ingsw.gc20.model.components.*;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public class Energy {

    private Battery battery;

    /**
     * Default constructor
     */
    public Energy() {
        this.battery = null;
    }

    /** get function for the battery where the energy is stored
     * @return Battery battery where the energy is stored
     */
    public Battery getBattery() {
        return this.battery;
    }

    /** set function for the battery where the energy is stored
     * @param b battery where the energy is stored
     */
    public void setBattery(Battery b) {
        this.battery = b;
    }

}