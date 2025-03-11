package it.polimi.ingsw.gc20.model.ship;

import java.util.*;
import it.polimi.ingsw.gc20.model.components.*;
/**
 * @author GC20
 */
public class LearnerShip extends Ship {

    /**
     * Default constructor
     */

    /**
     * 
     */
    private Tile[][] table = new Tile[5][5];

    /**
     * 
     */
    public LearnerShip() {
        super();
        table[2][2].setAvailability(false);
        Component sc = new StartingCabin();
        table[2][2].addComponent(sc);
    }
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

    @Override
    protected void setComponentAt(Component c, int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            table[row][col].addComponent(c);
        }
    }
}