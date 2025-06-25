package it.polimi.ingsw.gc20.server.model.ship;

import it.polimi.ingsw.gc20.server.exceptions.EmptyCabinException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidTileException;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

import java.util.HashMap;
import java.util.Map;

/**
 * The LearnerShip class represents a specific type of ship with predefined
 * layout, cargo capacities, and starting cabin configurations.
 * It extends the functionality of the Ship class and provides additional
 * features like managing components, unloading crew, and accessing dimensions.
 */
public class LearnerShip extends Ship {
    
    /**
     * Initializes a new instance of the LearnerShip class with a specified player color.
     * The constructor sets up the game board, initializes cargo capacities, and places the starting cabin
     * with connectors at the center of the ship.
     *
     * @param color the PlayerColor assigned to the LearnerShip
     */
    public LearnerShip(PlayerColor color) {
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
        cargos.put(CargoColor.RED, 0);

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
        Component sc = new StartingCabin(color);
        sc.setConnectors(connectors);
        try {
            table[2][2].addComponent(sc);
        } catch (InvalidTileException _) {}
        table[2][2].setAvailability(false);
    }

    /**
     * Default constructor for the LearnerShip class.
     * Initializes the game board, cargo capacities, and places the starting cabin with its connectors.
     * This constructor is used to create an instance of the LearnerShip with standard color settings.
     */
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
        cargos.put(CargoColor.RED, 0);

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
     * Retrieves the number of rows of the LearnerShip.
     *
     * @return the number of rows as an int
     */
    @Override
    public int getRows(){
        return 5;
    }

    /**
     * Retrieves the number of columns of the LearnerShip.
     *
     * @return the number of columns as an int
     */
    @Override
    public int getCols(){
        return 5;
    }

    /**
     * Retrieves the component located at the specified row and column on the LearnerShip.
     * If the specified row and column are out of bounds, the method will return null.
     *
     * @param row the row index of the desired component
     * @param col the column index of the desired component
     * @return the component at the specified position, or null if the position is out of bounds
     */
    @Override
    public Component getComponentAt(int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            return table[row][col].getComponent();
        }
        return null;
    }

    /**
     * Sets the specified component at the given row and column on the LearnerShip.
     * The method ensures that the row and column indices are within bounds before adding the component.
     * If the row or column is out of bounds, the method will throw an InvalidTileException.
     *
     * @param c the component to be added to the specified location
     * @param row the row index where the component should be placed
     * @param col the column index where the component should be placed
     * @throws InvalidTileException if the specified row or column indices are out of bounds
     */
    @Override
    protected void setComponentAt(Component c, int row, int col) throws InvalidTileException{
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            table[row][col].addComponent(c);
        }
    }

    /**
     * Unloads crew members from the specified cabin.
     * If the cabin is empty, an EmptyCabinException is thrown.
     *
     * @param c the cabin from which the crew members are to be unloaded
     * @throws EmptyCabinException if the cabin contains no astronauts
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