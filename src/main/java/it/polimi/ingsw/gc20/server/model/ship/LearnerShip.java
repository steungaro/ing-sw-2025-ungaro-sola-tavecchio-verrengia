package it.polimi.ingsw.gc20.server.model.ship;

import it.polimi.ingsw.gc20.server.exceptions.EmptyCabinException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidTileException;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GC20
 */
public class LearnerShip extends Ship {



    public LearnerShip() {
        super();
        table = new Tile[5][7];
        // Init table
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                table[i][j] = new Tile();
            }
        }

        cargos.put(CargoColor.BLUE, 0);
        cargos.put(CargoColor.GREEN, 0);
        cargos.put(CargoColor.YELLOW, 0);

        table[0][0].setAvailability(false);
        table[0][1].setAvailability(false);
        table[0][3].setAvailability(false);
        table[0][4].setAvailability(false);
        table[1][0].setAvailability(false);
        table[1][4].setAvailability(false);
        table[4][2].setAvailability(false);

        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.U);
        connectors.put(Direction.DOWN, ConnectorEnum.U);
        connectors.put(Direction.LEFT, ConnectorEnum.U);
        connectors.put(Direction.RIGHT, ConnectorEnum.U);
        Component sc = new StartingCabin();
        sc.setConnectors(connectors);
        try {
            table[2][2].addComponent(sc);
        } catch (InvalidTileException _) {}
        table[2][2].setAvailability(false);
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
    public Component getComponentAt(int row, int col) {
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
     * @throws  InvalidTileException if the tile is not available
     */
    @Override
    protected void setComponentAt(Component c, int row, int col) throws InvalidTileException{
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            table[row][col].addComponent(c);
        }
    }

    /**
     * Function to unload a crew member from the ship
     * @param c is the crew member to be unloaded
     * @throws EmptyCabinException if the cabin is empty
     */
    @Override
    public void unloadCrew(Cabin c) throws EmptyCabinException {
        if (c.getAstronauts() < 1) {
            throw new EmptyCabinException("Empty cabin");
        }
        c.unloadAstronaut();
        astronauts--;
    }

}