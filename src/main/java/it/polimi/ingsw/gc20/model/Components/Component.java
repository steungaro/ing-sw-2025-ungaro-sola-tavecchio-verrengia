package it.polimi.ingsw.gc20.model.Components;

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
    private Map<Direction; ConnectorEnum> connectors;

    /**
     * 
     */
    private Integer IDComponent;

    /**
     * 
     */
    private Integer ? orientation;

    /**
     * @return
     */
    public void rotateClockwise() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void rotateCounterclockwise() {
        // TODO implement here
        return null;
    }

}