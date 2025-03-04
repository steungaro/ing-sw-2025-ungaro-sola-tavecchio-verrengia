package it.polimi.ingsw.gc20.model.Gamesets;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public abstract class Board {

    /**
     * Default constructor
     */
    public Board() {
    }

    /**
     * 
     */
    private List<AdventureCard> deck;

    /**
     * 
     */
    private Integer spaces;

    /**
     * 
     */
    public void drawCard() {
        // TODO implement here
    }

    /**
     * @return
     */
    public void createDeck() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Integer getSpaces() {
        // TODO implement here
        return null;
    }

}