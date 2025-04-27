package it.polimi.ingsw.gc20.server.model.ship;

import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import java.util.*;

/**
 * @author GC20
 */
public abstract class Ship {


    protected final Set<Component> waste;
    protected Integer singleEngines;
    protected final Integer doubleCannons;
    protected Integer doubleEngines;
    protected Integer doubleCannonsPower;
    protected Float singleCannonsPower;
    protected Integer totalEnergy;
    protected final Map<CargoColor, Integer> cargos;
    protected Integer astronauts;
    protected Tile[][] table;
    /**
     * Default constructor. Initializes default ship values.
     */
    public Ship() {
        waste = new HashSet<>();
        singleEngines = 0;
        doubleCannons = 0;
        doubleEngines = 0;
        doubleCannonsPower = 0;
        singleCannonsPower = 0f;
        totalEnergy = 0;
        cargos = new HashMap<>();
        astronauts = 0;
    }

    /**
     * Function to add a component to the ship
     * @param c component to add
     * @param row where to add the component
     * @param col where to add the component
     */
    public void addComponent(Component c, int row, int col) throws InvalidTileException {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            setComponentAt( c, row, col);
            c.updateParameter(this, 1);
            c.setTile(table[row][col]);
        } else {
            throw new InvalidTileException("Position not valid");
        }
    }

    /**
     * Gets the total number of rows in the ship grid
     * @return Number of rows
     */
    public abstract Integer getRows();

    /**
     * Get the number of columns in this ship
     * @return number of columns
     */
    public abstract Integer getCols();

    /**
     * Gets the component at the specified position
     * @param row Row index
     * @param col Column index
     * @return Component at position, or null if empty
     */
    public abstract Component getComponentAt(int row, int col);

    /**
     * Sets a component at the specified position
     * @param c Component to place
     * @param row Row index
     * @param col Column index
     */
    protected abstract void setComponentAt(Component c, int row, int col) throws InvalidTileException;

    /**
     * Function that determines the total firepower of the ship
     * it is the sum of single cannons power and double cannons power based on their orientation (cannons facing north have full power, others have half-power)
     * the user will select one by one the cannons he wants to use (single cannons automatically selected) every time he selects a cannon the power of the ship will be recalculated
     * it also checks if the ship has the necessary number of batteries
     * @param cannons Set<Component>: the double cannons the user wants to activate
     * @return power
     * @throws EnergyException if the number of cannons is greater than the total energy of the ship
     * @throws InvalidCannonException if a cannon is a single cannon
     */
    public float firePower(Set<Cannon> cannons, Integer energies) throws EnergyException, InvalidCannonException {
        if(energies > totalEnergy || cannons.size() != energies)
            throw new EnergyException("cannon size too large");
        float power  = singleCannonsPower;
        for(Cannon cannon : cannons){
            if (cannon.getPower() == 1){
                throw new InvalidCannonException("cannot select single cannon");
            }
            if (cannon.getOrientation() == Direction.UP)
                power += cannon.getPower();
            else {
                power += cannon.getPower() / 2;
            }
        }
        return power;
    }

    /**Function that determines the total engine power of the ship that the user wants to activate
     * @param doubleEnginesActivated: the number of double engines the user wants to activate => the number of battery cells consumed
     * @return power_of_the_ship
     * @throws IllegalArgumentException if the number of double engines activated is greater than the total number of double engines
     */
    public Integer enginePower(Integer doubleEnginesActivated) throws InvalidEngineException {
        if (doubleEnginesActivated > doubleEngines) {
            throw new InvalidEngineException("not enough double engines");
        }
        return doubleEnginesActivated * 2 + singleEngines;
    }

    /**
     * Gets the total energy available from batteries
     * @return Total energy
     */
    public Integer getTotalEnergy() {
        return totalEnergy;
    }

    /**
     * Gets the total number of astronauts in the ship
     * @return Crew count
     */
    public Integer crew() {
        return astronauts;
    }


    /**
     * @return the number of astronauts
     */
    public Integer getAstronauts() {
        return astronauts;
    }

    /**
     * Function that gets the first component of the ship from a certain direction to determine what component will be hit
     * @param d Direction: the direction from which the component will be hit
     * @param n Integer: row or column of the component ATTENTION it is the row or colum of the ship
     * @return component_hit
     */
    public Component getFirstComponent(Direction d, Integer n) {
        int rows = getRows(), cols = getCols();
        if ((d == Direction.UP || d == Direction.DOWN) && (n < 0 || n >= cols)) {
            return null;
        }
        if ((d == Direction.LEFT || d == Direction.RIGHT) && (n < 0 || n >= rows)) {
            return null;
        }

        switch (d)
        {
            case UP:
                for(int i = 0; i < rows; i++){
                    if(getComponentAt(i,n)!=null){
                        return getComponentAt(i,n);
                    }
                }
            case DOWN:
                for(int i = rows-1; i >= 0; i--){
                    if(getComponentAt(i,n)!=null){
                        return getComponentAt(i,n);
                    }
                }
            case LEFT:
                for(int i = 0; i < cols; i++){
                    if(getComponentAt(n,i)!=null){
                        return getComponentAt(n,i);
                    }
                }
            case RIGHT:
                for(int i = cols-1; i >= 0; i--){
                    if(getComponentAt(n,i)!=null){
                        return getComponentAt(n,i);
                    }
                }
        }
        return null;
    }

    /**
     * Check if the ship is shielded from a certain direction
     * @param d Direction: the direction from which the ship is being attacked
     * @return if the side is shielded
     */
    public Boolean getShield(Direction d) {
        //parse throws the component of the ship until it finds a shield that covers the direction or until all pieces are checked
        int rows = getRows();
        int cols = getCols();

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                Component component = getComponentAt(i, j);
                if (component !=null && component.shieldIn(d)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds all cannons pointing in a specific direction at a given row/column
     * @param d Direction to check
     * @param n Row/column index to check
     * @return List of cannons pointing in that direction
     */
    public List<Cannon> getCannons(Direction d, Integer n) {
        int cols = getCols();
        int rows = getRows();
        List<Cannon> cannons = new ArrayList<>();
        if ((d == Direction.UP || d == Direction.DOWN) && (n >= 0 && n < cols)) {
            for(int row = 0; row < rows; row++){
                Component component = getComponentAt(row, n);
                if(component != null && component.isCannon()) {
                    if (((Cannon) component).getOrientation() == d){
                        cannons.add((Cannon) component);
                    }
                }
            }
        }
        if((d == Direction.LEFT || d == Direction.RIGHT) && (n >= 0 && n < rows)){
            for(int col = 0; col < cols; col++){
                Component component = getComponentAt(n, col);
                if(component != null && component.hasValidOrientation(d) && component.isCannon()) {
                    cannons.add((Cannon) component);
                }
            }
        }
        return cannons;
    }

    /**
     * Counts all connectors that are exposed to space (not connected to another component)
     * @return Number of exposed connectors
     */
    public Integer getAllExposed() {
        int rows = getRows();
        int cols = getCols();
        int exposedConnectors = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Component component = getComponentAt(i, j);
                if (component != null) {
                    Map<Direction, ConnectorEnum> connectors = component.getConnectors();
                    for (Map.Entry<Direction, ConnectorEnum> entry : connectors.entrySet()) {
                        if (entry.getValue() == ConnectorEnum.ZERO) {
                            continue;
                        }
                        int adjRow = i, adjCol = j;
                        switch (entry.getKey()) {
                            case UP -> adjRow--;
                            case DOWN -> adjRow++;
                            case LEFT -> adjCol--;
                            case RIGHT -> adjCol++;
                        }
                        if (adjRow < 0 || adjCol < 0 || adjRow >= rows || adjCol >= cols) {
                            exposedConnectors++;
                        } else {
                            Component adjComponent = getComponentAt(adjRow, adjCol);
                            if (adjComponent == null || !component.isValid(adjComponent, entry.getKey())) {
                                exposedConnectors++;
                            }
                        }
                    }
                }
            }
        }
        return exposedConnectors;
    }

    /**
     * find all the valid components of the ship and kill the invalid ones
     * @param row row of the component to start the search
     * @param col col of the component to start the search
     * @apiNote Note that with row=-1 and col=-1 the function will start with the starting cabin
     */
    public void findValid(int row, int col) {
        if(row == -1 && col == -1){
            row = getRows()/2;
            col = getCols()/2;
        }
        if (row < 0 || col < 0 || row >= getRows() || col >= getCols()) {
            throw new IndexOutOfBoundsException();
        }
        int rows = getRows();
        int cols = getCols();
        boolean[][] visited = new boolean[rows][cols];

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});
        visited[row][col] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int i = current[0];
            int j = current[1];
            Component component = getComponentAt(i, j);

            if (component == null) continue;

            // Check connections in all four directions
            Map<Direction, ConnectorEnum> connectors = component.getConnectors();
            for (Map.Entry<Direction, ConnectorEnum> entry : connectors.entrySet()) {
                Direction dir = entry.getKey();
                ConnectorEnum connector = entry.getValue();

                // Skip if there's no connector in this direction
                if (connector == null || connector == ConnectorEnum.ZERO) {
                    continue;
                }

                // Calculate adjacent cell coordinates
                int adjRow = i, adjCol = j;

                switch (dir) {
                    case UP:
                        adjRow--;
                        break;
                    case DOWN:
                        adjRow++;
                        break;
                    case LEFT:
                        adjCol--;
                        break;
                    case RIGHT:
                    default:
                        adjCol++;
                        break;
                }

                // Check if the adjacent cell is within bounds and has a component
                Component adjComponent = getComponentAt(adjRow, adjCol);
                if (adjRow >= 0 && adjRow < rows && adjCol >= 0 && adjCol < cols && adjComponent != null) {
                    if (!visited[adjRow][adjCol]) {
                        // Check if the adjacent component has a matching connector
                        if (component.isValid(adjComponent, dir)) {
                            // Valid connection found, add to queue
                            queue.add(new int[]{adjRow, adjCol});
                            visited[adjRow][adjCol] = true;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Component component = getComponentAt(i, j);
                if (component != null && !visited[i][j]) {
                    try {
                        killComponent(component);
                    } catch (ComponentNotFoundException _) {
                        // This should never happen, but just in case
                        System.out.println("Component not found in ship");

                    }
                }
            }
        }

    }

    /**
     * Validates if all components are connected to the starting position
     * @param startRow Starting row for validation
     * @param startCol Starting column for validation
     * @return True if a ship is valid, false otherwise
     */
    public boolean isValid(int startRow, int startCol) {
        int rows = getRows();
        int cols = getCols();
        boolean[][] visited = new boolean[rows][cols];

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int i = current[0];
            int j = current[1];
            Component component = getComponentAt(i, j);

            if (component == null) continue;

            // Check connections in all four directions
            Map<Direction, ConnectorEnum> connectors = component.getConnectors();
            for (Map.Entry<Direction, ConnectorEnum> entry : connectors.entrySet()) {
                Direction dir = entry.getKey();
                ConnectorEnum connector = entry.getValue();

                // Skip if there's no connector in this direction
                if (connector == null || connector == ConnectorEnum.ZERO) {
                    continue;
                }

                // Calculate adjacent cell coordinates
                int adjRow = i, adjCol = j;

                switch (dir) {
                    case UP:
                        adjRow--;
                        break;
                    case DOWN:
                        adjRow++;
                        break;
                    case LEFT:
                        adjCol--;
                        break;
                    case RIGHT:
                    default:
                        adjCol++;
                        break;
                }

                // Check if the adjacent cell is within bounds and has a component
                Component adjComponent = getComponentAt(adjRow, adjCol);
                if (adjRow >= 0 && adjRow < rows && adjCol >= 0 && adjCol < cols && adjComponent != null) {
                    if (!visited[adjRow][adjCol]) {
                        // Check if the adjacent component has a matching connector
                        if (component.isValid(adjComponent, dir)) {
                            // Valid connection found, add to queue
                            queue.add(new int[]{adjRow, adjCol});
                            visited[adjRow][adjCol] = true;
                        }else{
                            return false;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Component component = getComponentAt(i, j);
                if (component != null && !visited[i][j]) {
                    return false; // Found an unconnected component
                }
            }
        }

        return true;
    }

    /**
     * Validates if the ship structure is valid (all components connected) starting from the starting cabin
     * @return True if a ship is valid, false otherwise
     */
    public boolean isValid() {
        return isValid(getRows()/2, getCols()/2);
    }

    /**
     * Gets the set of components in the waste
     * @return Set of waste components
     */
    public Set<Component> getWaste() {
        return waste;
    }

    /**
     * Adds a component to the waste
     * @param c Component to add to waste
     */
    public void addToWaste(Component c) {
        waste.add(c);
    }

    /**
     * Removes a component from the ship and adds it to waste
     * @param c Component to destroy
     * @return True if the ship remains valid after removal
     */
    public Boolean killComponent(Component c) throws ComponentNotFoundException {
        if (c == null) {
            throw new ComponentNotFoundException("Component not found in ship");
        }
        int[] position = findComponent(c);
        if (position == null) {
            throw new ComponentNotFoundException("Component not found in ship");
        }
        c.updateParameter(this, -1);
        try {
            table[position[0]][position[1]].removeComponent();
            waste.add(c);
        } catch (InvalidTileException _){}
        return this.isValid();
    }

    /**
     * Function to load a cargo in a cargo hold
     * @param cargo cargo to be loaded
     * @param h cargo hold to load the cargo in
     * @throws CargoFullException if the cargo hold is full
     * @throws CargoNotLoadable if the cargo is not loadable in the cargo hold
     */
    public void loadCargo(CargoColor cargo, CargoHold h) throws CargoFullException, CargoNotLoadable {
        h.loadCargo(cargo);
        cargos.put(cargo, cargos.getOrDefault(cargo, 0) + 1);
    }

    /**
     * Function to unload a cargo from the ship
     * @param cargo cargo to be unloaded
     * @param h cargo hold to unload the cargo from
     * @throws IllegalArgumentException if there is no cargo of that type to unload
     */
    public void unloadCargo(CargoColor cargo, CargoHold h) throws InvalidCargoException {
        h.unloadCargo(cargo);
        cargos.put(cargo, cargos.get(cargo) - 1);
    }

    /**
     * add single cannon power
     * @param power power to add to singleCannonsPower
     */
    public void addSingleCannonsPower(float power) {
        singleCannonsPower += power;
    }

    /**
     * add double cannon power
     * @param power power to add to doubleCannonsPower
     */
    public void addDoubleCannonsPower(int power) {
        doubleCannonsPower += power;
    }

    /**
     * add double engines
     * @param power power to add to doubleEngines
     */
    public void addDoubleEngines(int power) {
        doubleEngines += power;
    }

    /**
     * add single engines
     * @param power power to add to singleEngines
     */
    public void addSingleEngines(int power) {
        singleEngines += power;
    }
    /**
     * add batteries
     * @param batteries power to add to totalEnergy
     */
    public void addBatteries(int batteries) {
        totalEnergy += batteries;
    }

    /**
     * Function that fills the cabins at the beginning of the game
     */
    public void initAstronauts(){
        int row = getRows();
        int col = getCols();
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                Component c = getComponentAt(i, j);
                if (c!= null){
                    astronauts += c.initializeAstronauts();
                }
            }
        }
    }

    /**
     * Function that there are 2 adjacent cabins that have aliens or astronauts inside them kill one being per cabin
     * if there are no astronauts or aliens in the cabin, nothing happens
     */
    public void epidemic() {
        int rows = getRows();
        int cols = getCols();
        Component adj;
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                Component c = getComponentAt(i, j);
                if(c!= null && c.isCabin()){
                    for (int k = 0; k < 4; k++) {
                        if (c.getConnectors().get(Direction.values()[k]) != ConnectorEnum.ZERO) {
                            if (k == 0) {
                                adj = getComponentAt(i, j - 1);
                            } else if (k == 1) {
                                adj = getComponentAt(i - 1, j);
                            } else if (k == 2) {
                                adj = getComponentAt(i, j + 1);
                            } else {
                                adj = getComponentAt(i + 1, j);
                            }
                            if (adj.isCabin()) {
                                try {
                                    unloadCrew((Cabin) c);
                                } catch (EmptyCabinException _) {}
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Function that removes energy from the battery
     * @param battery battery to remove energy from
     * @throws EnergyException if the battery is empty
     */
    public void useEnergy(Battery battery) throws EnergyException {
        if (battery.getAvailableEnergy() < 1) {
            throw new EnergyException("Battery is empty");
        }
        battery.useEnergy();
        totalEnergy--;
    }

    /**
     * Function to unloadCrew from the ship
     * @param c the crew member to be unloaded
     */
    public abstract void unloadCrew(Cabin c) throws EmptyCabinException;


    /**
     * Function that gets the cargo held by the ship
     * @return cargo held by the ship
     */
    public Map<CargoColor, Integer> getCargo() {
        return cargos;
    }

    /**
     * Function that finds the component in the ship
     * @param c component to find
     * @return the position of the component in the ship
     */
    public int[] findComponent (Component c) {
        int rows = getRows();
        int cols = getCols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (getComponentAt(i, j) == c) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * Function that verify is the ship is a normal ship
     * @return true if the ship is a normal ship, false otherwise
     */
    public boolean isNormal (){
        return false; //default implementation
    }

    public void addBookedToWaste() {
        //default implementation
    }


}