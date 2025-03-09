package it.polimi.ingsw.gc20.model.ship;

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
     * 
     */
    private List<Component> booked;

    /**
     * @return
     */
    public Integer aliens() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void addBookedToWaste() {
        // TODO implement here
        return null;
    }

    /**
     * @param Component c 
     * @return
     */
    public void removeBooked(void Component c) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Component> getBooked() {
        // TODO implement here
        return null;
    }

    /**
     * @param Component c 
     * @return
     */
    public void addBooked(void Component c) {
        // TODO implement here
        return null;
    }

    /**
     * @param AlienColor c 
     * @return
     */
    public Integer aliens(void AlienColor c) {
        // TODO implement here
        return null;
    }

    public Integer getRows(){
        return 7;
    }

    public Integer getCols(){
        return 5;
    }

    @Override
    protected Component getComponentAt(int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            return table[row][col].getComponent();
        }
        return null;
    }

}