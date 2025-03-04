package it.polimi.ingsw.gc20.model;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public abstract class AdventureCard {

    /**
     * Default constructor
     */
    public AdventureCard() {
    }

    /**
     * level can be 0 for learners, 1 for level 1 cards and 2 for level 2 cards
     */
    private Integer level;

    /**
     * 
     */
    private Integer IDCard;

    /**
     *
     */
    public void ApplyCard(Board b) {
    }

}