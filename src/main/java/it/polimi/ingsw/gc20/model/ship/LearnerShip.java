package it.polimi.ingsw.gc20.model.ship;

import java.util.*;

/**
 * @author GC20
 */
public class LearnerShip extends Ship {

    /**
     * Default constructor
     */
    public LearnerShip() {
    }

    /**
     * 
     */
    private Tile table[5][5];

    /**
     * 
     */
    private List<Tile> trash;

    public Integer getRows(){
        return 5;
    }

    public Integer getCols(){
        return 5;
    }

    @Override
    protected  Component getComponentAt(int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            return table[row][col].getComponent();
        }
        return null;
    }
}