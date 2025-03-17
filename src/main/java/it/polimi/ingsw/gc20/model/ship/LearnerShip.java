package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.bank.Alien;
import it.polimi.ingsw.gc20.model.bank.Astronaut;
import it.polimi.ingsw.gc20.model.bank.Crew;
import it.polimi.ingsw.gc20.model.components.*;
/**
 * @author GC20
 */
public class LearnerShip extends Ship {

    /**
     * Matrix of tiles representing the ship
     */
    private Tile[][] table = new Tile[5][5];

    /**
     * Default constructor
     */
    public LearnerShip() {
        super();
        table[2][2].setAvailability(false);
        Component sc = new StartingCabin();
        table[2][2].addComponent(sc);
    }

    /**
     * Getter for the number of rows
     * @return rows
     */
    public Integer getRows(){
        return 5;
    }

    /**
     * Getter for the number of columns
     * @return cols
     */
    public Integer getCols(){
        return 5;
    }

    /**
     * @param row: position of the component
     * @param col: position of the component
     * @return the component at the given position
     */
    @Override
    protected  Component getComponentAt(int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            return table[row][col].getComponent();
        }
        return null;
    }

    /**
     * Add a component to the ship
     * @param c: the component to be added
     * @param row: position of the component
     * @param col: position of the component
     */
    @Override
    protected void setComponentAt(Component c, int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            table[row][col].addComponent(c);
        }
    }

    /**
     * Adds a component to the ship at the specified position and updates ship parameters
     * @param c Component to add
     * @param row Row position
     * @param col Column position
     */
    public void addComponent(Component c, int row, int col){
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            setComponentAt( c, row, col);
            updateParameters(c, 1);
            c.setTile(table[row][col]);
        }
    }

    @Override
    public void unloadCrew(Crew c) {
        c.getCabin().unloadAstronaut((Astronaut)c);
        astronauts--;
    }
}