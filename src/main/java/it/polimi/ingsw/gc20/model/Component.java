package it.polimi.ingsw.gc20.model;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public abstract class Component {

    /**
     * Default constructor
     */
    public Component() {
    }

    /**
     * 
     */
    private Integer[] connectors;

    /**
     * 
     */
    private Integer IDComponent;

    /**
     * 
     */
    private Integer orientation;

    /**
     * 
     */
    public void rotate() {
        // TODO implement here
    }

}