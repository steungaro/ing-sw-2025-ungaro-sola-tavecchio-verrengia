package it.polimi.ingsw.gc20.model;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public class NormalShip extends Ship {

    /**
     * Default constructor
     */
    public NormalShip() {
    }

    /**
     * 
     */
    private Tile table[7][5];

    /**
     * 
     */
    private Tile booked[2];

    /**
     * 
     */
    private List<Tile> trash;

    /**
     * @return
     */
    public int aliens() {
        // TODO implement here
        return 0;
    }

}