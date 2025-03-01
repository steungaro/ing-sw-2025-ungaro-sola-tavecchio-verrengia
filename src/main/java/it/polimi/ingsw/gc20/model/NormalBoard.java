package it.polimi.ingsw.gc20.model;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public class NormalBoard extends Board {

    /**
     * Default constructor
     */
    public NormalBoard() {
    }

    /**
     * 
     */
    private Player[] spaces;

    /**
     * 
     */
    private List<AdventureCard> firstVisible;

    /**
     * 
     */
    private List<AdventureCard> secondVisible;

    /**
     * 
     */
    private List<AdventureCard> thirdVisible;

    /**
     * 
     */
    private List<AdventureCard> invisible;

}