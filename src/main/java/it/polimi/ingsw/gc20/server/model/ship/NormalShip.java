package it.polimi.ingsw.gc20.server.model.ship;

import java.util.*;

import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

/**
 * Represents a normal ship in the game, extending the base behavior of the Ship class.
 * Includes features for managing components, aliens, and gameplay-specific attributes
 * such as cargo, booked components, and the ship's layout.
 */
public class NormalShip extends Ship {
    private final Component[] booked = new Component[2];
    boolean brownAlien;
    boolean purpleAlien;
    private AlienColor colorHostable = AlienColor.NONE;

    /**
     * Default constructor for the NormalShip class.
     * Initializes a standard ship layout along with predefined attributes and configurations.
     */
    public NormalShip() {
        super();
        brownAlien = false;
        purpleAlien = false;

        table = new Tile[5][7];
        // Init table
        for (int i=0; i<5; i++) {
            for (int j=0; j<7; j++) {
                table[i][j] = new Tile();
            }
        }

        cargos.put(CargoColor.BLUE, 0);
        cargos.put(CargoColor.GREEN, 0);
        cargos.put(CargoColor.YELLOW, 0);
        cargos.put(CargoColor.RED, 0);

        table[0][0].setAvailability(false);
        table[0][1].setAvailability(false);
        table[1][0].setAvailability(false);
        table[0][3].setAvailability(false);
        table[0][6].setAvailability(false);
        table[0][5].setAvailability(false);
        table[1][6].setAvailability(false);
        table[4][3].setAvailability(false);

        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.U);
        connectors.put(Direction.DOWN, ConnectorEnum.U);
        connectors.put(Direction.LEFT, ConnectorEnum.U);
        connectors.put(Direction.RIGHT, ConnectorEnum.U);
        Component sc = new StartingCabin();
        sc.setConnectors(connectors);
        try {
            addComponent(sc, 2, 3);
        } catch (InvalidTileException _) {}
        table[2][3].setAvailability(false);
    }

    /**
     * Constructs a NormalShip instance with a specific player color.
     * Initializes the ship configuration with a predefined layout, assigns default cargo values,
     * and sets up the starting cabin and its connectors.
     *
     * @param color The color associated with the player's ship.
     */
    public NormalShip(PlayerColor color) {
        super();
        brownAlien = false;
        purpleAlien = false;

        table = new Tile[5][7];
        // Init table
        for (int i=0; i<5; i++) {
            for (int j=0; j<7; j++) {
                table[i][j] = new Tile();
            }
        }

        cargos.put(CargoColor.BLUE, 0);
        cargos.put(CargoColor.GREEN, 0);
        cargos.put(CargoColor.YELLOW, 0);
        cargos.put(CargoColor.RED, 0);

        table[0][0].setAvailability(false);
        table[0][1].setAvailability(false);
        table[1][0].setAvailability(false);
        table[0][3].setAvailability(false);
        table[0][6].setAvailability(false);
        table[0][5].setAvailability(false);
        table[1][6].setAvailability(false);
        table[4][3].setAvailability(false);

        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.U);
        connectors.put(Direction.DOWN, ConnectorEnum.U);
        connectors.put(Direction.LEFT, ConnectorEnum.U);
        connectors.put(Direction.RIGHT, ConnectorEnum.U);
        Component sc = new StartingCabin(color);
        sc.setConnectors(connectors);
        try {
            addComponent(sc, 2, 3);
        } catch (InvalidTileException _) {}
        table[2][3].setAvailability(false);
    }


    /**
     * Retrieves the current table layout of tiles in the ship.
     *
     * @return a 2-dimensional array of Tile objects representing the layout of the ship.
     */
    public Tile[][] getTable() {
        return table;
    }

    /**
     * Retrieves the color that the cabin can host within the ship.
     *
     * @return the color that can be hosted, represented as an instance of the AlienColor enum.
     */
    public AlienColor getColorHostable() {
        return colorHostable;
    }

    /**
     * Sets the color that the cabin can host within the ship. This determines which type of aliens
     * are allowed to occupy the cabin.
     *
     * @param colorHostable the color that the cabin can host, represented as an instance of the AlienColor enum.
     */
    public void setColorHostable(AlienColor colorHostable) {
        this.colorHostable = colorHostable;
    }

    /**
     * Adds all components from the booked array to the waste collection.
     * After transferring the components, the booked array is cleared by
     * setting its elements to null.
     */
    @Override
    public void addBookedToWaste() {
        waste.addAll(Arrays.asList(booked));
        booked[0]=null;
        booked[1]=null;
    }

    /**
     * Removes a component from the list of booked components. If the component
     * is not found in the booked array, a {@link ComponentNotFoundException} is thrown.
     *
     * @param c The component to be removed from the booked components.
     * @throws ComponentNotFoundException if the specified component is not found in the booked array.
     */
    public void removeBooked(Component c) throws ComponentNotFoundException {
        if (booked[0] == c) {
            booked[0]=null;
        } else if (booked[1] == c) {
            booked[1]=null;
        }else{
            throw new ComponentNotFoundException("Component not found");
        }
    }
    /**
     * Retrieves the list of components that are currently booked on the ship.
     *
     * @return a list of Component objects representing the booked components.
     */
    public List<Component> getBooked() {
        return Arrays.asList(booked);
    }

    /**
     * Adds a component to the booked components list. If both slots are already occupied,
     * a {@link NoSpaceException} is thrown.
     *
     * @param c The component to be added to the booked components.
     * @throws NoSpaceException if there is no available slot in the booked array.
     */
    public void addBooked(Component c) throws NoSpaceException{
        if (booked[0] == null) {
            booked[0]=c;
        } else if (booked[1] == null) {
            booked[1]=c;
        } else {
            throw new NoSpaceException("Already 2 booked components");
        }
    }

    /**
     * Retrieves the number of rows in the ship's layout.
     *
     * @return the number of rows in the ship's layout
     */
    @Override
    public int getRows(){
        return 5;
    }

    /**
     * Retrieves the number of columns in the ship's layout.
     *
     * @return the number of columns in the ship's layout
     */
    @Override
    public int getCols(){
        return 7;
    }

    /**
     * Retrieves the component located at the specified row and column in the ship's layout.
     * If the specified position is out of bounds, the method returns null.
     *
     * @param row the row index of the desired component
     * @param col the column index of the desired component
     * @return the component at the specified position, or null if the position is invalid
     */
    @Override
    public Component getComponentAt(int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            return table[row][col].getComponent();
        }
        return null;
    }

    /**
     * Sets a component at the specified row and column in the ship's table layout.
     * If the specified position is out of bounds, the operation is not performed.
     *
     * @param c   the component to be placed at the specified position
     * @param row the row index where the component will be placed
     * @param col the column index where the component will be placed
     * @throws InvalidTileException if the specified position is invalid
     */
    @Override
    protected void setComponentAt(Component c, int row, int col) throws InvalidTileException {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            table[row][col].addComponent(c);
        }
    }

    /**
     * Calculates the total firing power of the ship based on the given set of cannons and available energies.
     * If the ship belongs to a "purple alien," additional power is added.
     *
     * @param cannons the set of cannons installed on the ship
     * @param energies the amount of energy available for firing
     * @return the total firing power as a float
     * @throws EnergyException if there is not enough energy to operate the cannons
     * @throws InvalidCannonException if the set of cannons contains invalid or malfunctioning cannons
     */
    @Override
    public float firePower(Set<Cannon> cannons, int energies) throws EnergyException, InvalidCannonException {
        float power = super.firePower(cannons, energies);
        return power + (purpleAlien ? 2 : 0);
    }

    /**
     * Calculates the engine power of the ship based on the number of double engines activated,
     * combined with single engines and an additional factor influenced by the presence of a brown alien.
     *
     * @param doubleEnginesActivated the number of double engines currently activated on the ship
     * @return the total engine power as an integer
     */
    @Override
    public int enginePower(int doubleEnginesActivated) {
        return doubleEnginesActivated * 2 + singleEngines + (brownAlien ? 2 : 0);
    }

    /**
     * Unloads a crew member (either an astronaut or an alien) from the specified cabin.
     * Updates the ship's status related to crew occupancy and alien presence accordingly.
     * Throws an exception if the cabin is empty.
     *
     * @param c The cabin from which the crew member (alien or astronaut) should be unloaded.
     * @throws EmptyCabinException if the specified cabin has no occupants to unload.
     */
    @Override
    public void unloadCrew(Cabin c) throws EmptyCabinException{
        if (c.getOccupants() < 1) {
            throw new EmptyCabinException ("Empty cabin");
        }
        if (c.getAlien()) {
            if (c.getAlienColor() == AlienColor.BROWN) {
                brownAlien = false;
            } else {
                purpleAlien = false;
            }
            try {
                c.unloadAlien();
            }catch (InvalidAlienPlacement _){}
        } else {
            c.unloadAstronaut();
            astronauts--;
        }
    }

    /**
     * Updates the state of cabins connected to a life support component that has been removed from the ship.
     * The method checks for adjacent components and ensures any cabins lose support from the specified life support.
     *
     * @param c The life support component that has been removed from the ship. The method will locate
     *          adjacent components and perform the necessary updates to any connected cabins.
     */
    public void updateLifeSupportRemoved(Component c) {
        //Find if there is a Cabin connected to the LifeSupport
        int[] position = findComponent(c);
        if (position == null) {
            return;
        }
        Map<Direction, ConnectorEnum> connectors = c.getConnectors();
        for (Map.Entry<Direction, ConnectorEnum> entry : connectors.entrySet()) {
            int row = position [0];
            int col = position [1];

            switch (entry.getKey()) {
                case UP:
                    row--;
                    break;
                case DOWN:
                    row++;
                    break;
                case LEFT:
                    col--;
                    break;
                case RIGHT:
                    col++;
                    break;
            }
            if (row>getRows() || row<0 || col>getCols() || col<0) {
                continue;
            }
            if ( table[row][col].getComponent() == null) {
                continue;
            }
            if (entry.getValue() == ConnectorEnum.ZERO || !(table[row][col].getComponent().isCabin())) {
                continue;
            }

            if (c.isValid(table[row][col].getComponent(), entry.getKey())) {
                Cabin comp = (Cabin) table[row][col].getComponent();
                comp.removeSupport((LifeSupport) c);
            }
        }
    }

    /**
     * Updates the state of cabins connected to a life support component that has been added to the ship.
     * The method locates the position of the specified life support component, checks its connectors, and updates
     * any adjacent cabins to add their support.
     *
     * @param c The life support component that has been added to the ship. The method will locate
     *          adjacent components and perform the necessary updates to any connected cabins.
     */
    public void updateLifeSupportAdded(Component c) {
        //Find if there is a Cabin connected to the LifeSupport
        int[] position = findComponent(c);
        if (position == null) {
            return;
        }

        //Found the lifeSupport
        Map<Direction, ConnectorEnum> connectors = c.getConnectors();
        for (Map.Entry<Direction, ConnectorEnum> entry : connectors.entrySet()) {
            int row = position[0];
            int col = position[1];

            switch (entry.getKey()) {
                case UP:
                    row--;
                    break;
                case DOWN:
                    row++;
                    break;
                case LEFT:
                    col--;
                    break;
                case RIGHT:
                    col++;
                    break;
            }
            if (row>=getRows() || row<0 || col>=getCols() || col<0) {
                continue;
            }
            if ( table[row][col].getComponent() == null) {
                continue;
            }

            if (entry.getValue() == ConnectorEnum.ZERO || !(table[row][col].getComponent().isCabin())) {
                continue;
            }


            if (c.isValid(table[row][col].getComponent(), entry.getKey())) {
                Cabin comp = (Cabin) table[row][col].getComponent();
                    comp.addSupport((LifeSupport) c);
            }
        }
    }

    /**
     * Adds an alien of the specified color to a given cabin. This method ensures that only one alien
     * of each color can be placed on the ship at a time. If an alien of the specified color is already present,
     * an exception is thrown.
     *
     * @param alien the color of the alien to add, represented as an instance of the AlienColor enum
     * @param c the cabin where the alien will be added
     * @throws InvalidAlienPlacement if an alien of the same color is already present on the ship
     */
    public void addAlien(AlienColor alien, Cabin c) throws InvalidAlienPlacement {
        c.setAlien(alien);
        if (alien == AlienColor.BROWN){
            if (brownAlien) {
                throw new InvalidAlienPlacement("Brown alien already present");
            }
            brownAlien = true;
        } else {
            if (purpleAlien) {
                throw new InvalidAlienPlacement("Purple alien already present");
            }
            purpleAlien = true;
        }
    }

    /**
     * Calculates and returns the total number of crew members currently on the ship.
     * This includes astronauts and aliens based on the ship's current state.
     * If a brown alien is present, it adds 1 to the count.
     * If a purple alien is present, it adds 1 to the count.
     *
     * @return the total number of crew members on the ship, including astronauts and any present aliens.
     */
    @Override
    public int crew(){
        return astronauts + (brownAlien ? 1 : 0) + (purpleAlien ? 1 : 0);
    }

    /**
     * Adds a component to the specified position in the ship's layout.
     * The component is placed at the given row and column if the position
     * is valid within the ship's grid. If the position is invalid, an exception is thrown.
     *
     * @param c   The component to be added to the ship.
     * @param row The row index where the component will be added.
     * @param col The column index where the component will be added.
     * @throws InvalidTileException if the specified position is outside the bounds of the ship's layout.
     */
    @Override
    public void addComponent(Component c, int row, int col) throws InvalidTileException{
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            setComponentAt( c, row, col);
            c.updateParameter(this, 1);
        }
        else
            throw new InvalidTileException("Invalid position");
    }

    /**
     * Determines whether the ship is of normal configuration.
     *
     * @return true if the ship is in a normal state, otherwise false
     */
    @Override
    public boolean isNormal(){
        return true;
    }

    /**
     * Retrieves the type of alien(s) currently present on the ship.
     * The method checks the presence of brown and/or purple aliens
     * and returns the corresponding AlienColor value.
     *
     * @return an instance of the AlienColor enum indicating the type of alien(s) present on the ship.
     */
    @Override
    public AlienColor getAliens(){
        if (brownAlien) {
            if (purpleAlien) {
                return AlienColor.BOTH;
            }
            return AlienColor.BROWN;
        } else if (purpleAlien) {
            return AlienColor.PURPLE;
        }
        return AlienColor.NONE;
    }

}