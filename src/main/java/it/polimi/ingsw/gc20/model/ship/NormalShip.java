package it.polimi.ingsw.gc20.model.ship;

import java.util.*;

import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.components.*;
/**
 * @author GC20
 */
public class NormalShip extends Ship {

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
     *  Components that the player is holding in hand
     */
    private final Component[] booked = new Component[2];


    /**
     * if a brown/purple alien is present in the ship
     */
    Boolean brownAlien;

    Boolean purpleAlien;

    private AlienColor colorHostable = AlienColor.NONE;

    public Tile[][] getTable() {
        return table;
    }

    public AlienColor getColorHostable() {
        return colorHostable;
    }

    public void setColorHostable(AlienColor colorHostable) {
        this.colorHostable = colorHostable;
    }

    /**
     * Function to be called at the end of construction phase to move the booked components to the waste
     */
    @Override
    public void addBookedToWaste() {
        waste.addAll(Arrays.asList(booked));
        booked[0]=null;
        booked[1]=null;
    }

    /**
     * Function to remove a component from the booked components
     * @param c is the component to be removed
     * @throws ComponentNotFoundException in booked
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
     * @return booked components
     */
    public List<Component> getBooked() {
        return Arrays.asList(booked);
    }

    /**
     * Add a component to the booked components
     * @param c the component to be added to booked
     * @throws NoSpaceException if the booked array is already full
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
     * @return the number of rows of the ship
     */
    public Integer getRows(){
        return 5;
    }

    /**
     * @return the number of columns of the ship
     */
    public Integer getCols(){
        return 7;
    }

    /**
     * Function that returns the component give the coordinates
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
     * @throws InvalidTileException if the position is invalid
     */
    @Override
    protected void setComponentAt(Component c, int row, int col) throws InvalidTileException {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            table[row][col].addComponent(c);
        }
    }

    /**
     * Function to calculate the firepower of the ship
     * @param cannons Set<Component>: the double cannons the user wants to activate
     * @return power: the firepower of the ship
     * @throws EnergyException if the energy are not enough
     * @throws InvalidCannonException if the cannon is single
     */
    @Override
    public float firePower(Set<Cannon> cannons, Integer energies) throws EnergyException, InvalidCannonException {
        float power = super.firePower(cannons, energies);
        return power + (purpleAlien ? 2 : 0);
    }

    /**
     * Function to calculate the engine power of the ship
     * @param doubleEnginesActivated: the number of double engines the user wants to activate => the number of battery cells consumed
     * @return enginePower: the engine power of the ship
     */
    @Override
    public Integer enginePower(Integer doubleEnginesActivated) {
        return doubleEnginesActivated * 2 + singleEngines + (brownAlien ? 2 : 0);
    }

    /**
     * Function to unload a crew member from the ship
     * @param c is the crew member to be unloaded
     * @throws EmptyCabinException the cabin is empty
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
     * Function to update the life support of the ship, we check the components that are connected to the life support if they are a cabin we update the color of the cabin
     * @param c: the component that was removed from the ship
     * @throws DeadAlienException if in the cabin there was an alien and it died
     */
    public void updateLifeSupportRemoved(Component c) {
        //Find if is there a Cabin connected to the LifeSupport
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

            if (table[row][col].getComponent() == null) {
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

    /** Function to update the life support of the ship, we check the components that are connected to the life support if they are a cabin we update the color of the cabin
     * @param c: the component that was added from the ship
     */
    public void updateLifeSupportAdded(Component c) {
        //Find if is there a Cabin connected to the LifeSupport
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
            if (table[row][col].getComponent() == null) {
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
     * Function to add an alien to the ship
     * @param alien: the alien to be added
     * @param c: the cabin where the alien will be added
     * @throws InvalidAlienPlacement: the cabin cannot host the alien
     * @throws InvalidAlienPlacement: the alien is already present in the ship
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
     * Gets the total number of astronauts in the ship
     * @return Crew count
     */
    @Override
    public Integer crew(){
        return astronauts + (brownAlien ? 1 : 0) + (purpleAlien ? 1 : 0);
    }

    /**
     * Adds a component to the ship at the specified position and updates ship parameters
     * @param c Component to add
     * @param row Row position
     * @param col Column position
     * @throws InvalidTileException if the position is invalid
     */
    public void addComponent(Component c, int row, int col) throws InvalidTileException{
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            setComponentAt( c, row, col);
            c.updateParameter(this, 1);
        }
        else
            throw new InvalidTileException("Invalid position");
    }

    /**
     * Function that returns true if the ship is a normal ship
     * @return true if the ship is a normal ship
     */
    @Override
    public boolean isNormal(){
        return true;
    }

}