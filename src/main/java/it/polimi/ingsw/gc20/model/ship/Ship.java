package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.bank.Astronaut;
import it.polimi.ingsw.gc20.model.components.*;

import java.util.*;
import it.polimi.ingsw.gc20.model.bank.Cargo;

/**
 * @author GC20
 */
public abstract class Ship {


    protected Set<Component> waste;
    protected Integer singleEngines;
    protected Integer doubleCannons;
    protected Integer doubleEngines;
    protected Integer doubleCannonsPower;
    protected Float singleCannonsPower;
    protected Integer totalEnergy;
    protected List<Cargo> cargo;
    protected Integer astronauts;
    List<Component> trash = new ArrayList<Component>();
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
        cargo = new ArrayList<>();
        astronauts = 0;
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
    protected abstract Component getComponentAt(int row, int col);

    /**
     * Sets a component at the specified position
     * @param c Component to place
     * @param row Row index
     * @param col Column index
     */
    protected abstract void setComponentAt(Component c, int row, int col);

    /**
     * Function that determines the total firepower of the ship
     * it is the sum of single cannons power and double cannons power based on their orientation (cannons facing north have full power, others have half power)
     * the user will select one by one the cannons he wants to use (single cannons automatically selected) every time he selects a cannon the power of the ship will be recalculated
     * it also checks if the ship has the necessary amount of batteries
     * @param cannons Set<Component>: the double cannons the user wants to activate
     * @return power
     * @throws IllegalArgumentException if the number of cannons is greater than the total energy of the ship
     */
    public float firePower(Set<Cannon> cannons, Integer energies) throws IllegalArgumentException {
        if(energies > totalEnergy || cannons.size() != energies)
            throw new IllegalArgumentException("cannon size too large");
        float power  = singleCannonsPower;
        for(Cannon cannon : cannons){
            power += cannon.getPower();
        }
        return power;
    }

    /**Function that determines the total engine power of the ship that the user wants to activate
     * @param doubleEnginesActivated: the number of double engines the user wants to activate => the number of battery cells consumed
     * @return power_of_the_ship
     */
    public Integer enginePower(Integer doubleEnginesActivated) {
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
     * Function that gets the first component of the ship from a certain direction to determine what component will be hit
     * @param d Direction: the direction from which the component will be hit
     * @param n Integer: row or column of the component ATTENTION it is the row or colum get from the dice NOT the row or column of the ship
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
                    if(getComponentAt(i,n)!=null){
                        return getComponentAt(i,n);
                    }
                }
            case RIGHT:
                for(int i = cols-1; i >= 0; i--){
                    if(getComponentAt(i,n)!=null){
                        return getComponentAt(i,n);
                    }
                }
        }
        return null;
    }

    /**
     * Check if the ship is shielded from a certain direction
     * @param d Direction: the direction from which the ship is being attacked
     * @return if side is shielded
     */
    public Boolean getShield(Direction d) {
        //parse throw the component of the ship until it finds a shield that covers the direction d or until all pieces are checked
        int rows = getRows();
        int cols = getCols();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(getComponentAt(i, j) != null && (getComponentAt(i, j) instanceof Shield) && Arrays.asList(((Shield) (getComponentAt(i, j))).getCoveredSides()).contains(d)){
                    return true;
                }
            }
        }
        return null;
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
                if(component instanceof Cannon && ((Cannon) component).getOrientation() == d) {
                    cannons.add((Cannon) component);
                }
            }
        }
        if((d == Direction.LEFT || d == Direction.RIGHT) && (n >= 0 && n < rows)){
            for(int col = 0; col < cols; col++){
                Component component = getComponentAt(n, col);
                if(component instanceof Cannon && ((Cannon) component).getOrientation() == d) {
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
                                adjCol++;
                                break;
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
     * Validates if all components are connected to the starting position
     * @param startRow Starting row for validation
     * @param startCol Starting column for validation
     * @return True if ship is valid, false otherwise
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

                // Check if adjacent cell is within bounds and has a component
                Component adjComponent = getComponentAt(adjRow, adjCol);
                if (adjRow >= 0 && adjRow < rows && adjCol >= 0 && adjCol < cols && adjComponent != null) {
                    if(component instanceof Cannon && dir == ((Cannon) component).getOrientation()) {
                        return false;
                    }
                    if((component instanceof Engine && dir == Direction.DOWN) || (component instanceof Engine && (((Engine) component).getOrientation() != Direction.DOWN))){
                        return false;
                    }
                    if (!visited[adjRow][adjCol]) {
                        // Check if adjacent component has a matching connector
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
     * @return True if ship is valid, false otherwise
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
     * Removes a component from ship and adds it to waste
     * @param c Component to destroy
     * @return True if ship remains valid after removal
     */
    public Boolean killComponent(Component c){
        Tile t = c.getTile();
        updateParameters(c, -1);
        waste.add(c);
        t.removeComponent();
        return this.isValid();
    }

    /**
     * Updates ship parameters when components are added or removed
     * @param c Component being added/removed
     * @param add 1 if adding, -1 if removing
     */
    protected void updateParameters(Component c, Integer add){
        if(c instanceof Cannon){
            if(((Cannon) c).getOrientation()==Direction.UP){
                if(((Cannon) c).getPower() == 1){
                    singleCannonsPower += add;
                }else{
                    doubleCannonsPower += 2*add;
                }
            }else{
                if(((Cannon) c).getPower() == 1) {
                    doubleCannons += add;
                    doubleCannonsPower += add;
                }else{
                    singleCannonsPower += 0.5f*add;
                    }
            }
        }else if(c instanceof Engine){
            if(((Engine) c).getDoublePower()){
                doubleEngines += add;
            }else{
                singleEngines += add;
                }
        }else if(c instanceof Battery){
            totalEnergy -= ((Battery) c).getEnergy().size();
        } else if (c instanceof Cabin && add == -1) {
            //kill all the astronauts inside the cabin
            astronauts -= ((Cabin) c).getAstronauts().size();
        } else if (c instanceof CargoHold && add == -1) {
            ((CargoHold) c).getCargoHeld().forEach(k -> cargo.remove(k));
            ((CargoHold) c).cleanCargo();
        }
    }

    /**
     * Adds a single astronaut to a cabin
     * @param a Astronaut to add
     * @param c Cabin component to place astronaut
     */
    public void addSingleAstronaut(Astronaut a, Component c){
        astronauts++;
        ((Cabin) c).getAstronauts().add(a);
    }


    /**
     * Function that fills the cabins at the beginning of the game
     */
    public void addAllAstronauts(){
        int row = getRows();
        int col = getCols();
        for(int i=0; i<row; row++){
            for(int j=0; j<col; j++){
                Component c = getComponentAt(i, j);
                if(c instanceof Cabin){
                    for(int k=0; k< 2 - ((Cabin) c).getOccupants(); k++){
                        addSingleAstronaut(new Astronaut(), c);
                    }
                }
            }
        }
    }

    /**
     * Function that there are 2 adjacent cabins that have aliens or astronauts inside them kill one beeing per cabin
     * if there are no astronauts or aliens in the cabin, nothing happens
     */
    public void epidemic() {
        int rows = getRows();
        int cols = getCols();
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                Component c = getComponentAt(i, j);
                if(c instanceof Cabin){
                    if(!((Cabin) c).getAstronauts().isEmpty() || ((Cabin) c).getAlien() != null){
                        //check all directions to find a cabin with astronauts or aliens
                        if(i-1 >= 0){
                            Component c1 = getComponentAt(i-1, j);
                            if(c1 instanceof Cabin){
                                if(!((Cabin) c1).getAstronauts().isEmpty() || ((Cabin) c1).getAlien() != null){
                                    if(!((Cabin) c).getAstronauts().isEmpty()){
                                        ((Cabin) c).getAstronauts().remove(0);
                                    }else{
                                        ((Cabin) c).setAliens(null);
                                    }
                                }
                            }
                        }
                        if(i+1 < rows){
                            Component c2 = getComponentAt(i+1, j);
                            if(c2 instanceof Cabin){
                                if(!((Cabin) c2).getAstronauts().isEmpty() || ((Cabin) c2).getAlien() != null){
                                    if(!((Cabin) c).getAstronauts().isEmpty()){
                                        ((Cabin) c).getAstronauts().remove(0);
                                    }else{
                                        ((Cabin) c).setAliens(null);
                                    }
                                }
                            }
                        }
                        if(j-1 >= 0){
                            Component c3 = getComponentAt(i, j-1);
                            if(c3 instanceof Cabin){
                                if(!((Cabin) c3).getAstronauts().isEmpty() || ((Cabin) c3).getAlien() != null){
                                    if(!((Cabin) c).getAstronauts().isEmpty()){
                                        ((Cabin) c).getAstronauts().remove(0);
                                    }else{
                                        ((Cabin) c).setAliens(null);
                                    }
                                }
                            }
                        }
                        if(j+1 < cols) {
                            Component c4 = getComponentAt(i, j + 1);
                            if (c4 instanceof Cabin) {
                                if (!((Cabin) c4).getAstronauts().isEmpty() || ((Cabin) c4).getAlien() != null) {
                                    if (!((Cabin) c).getAstronauts().isEmpty()) {
                                        ((Cabin) c).getAstronauts().remove(0);
                                    } else {
                                        ((Cabin) c).setAliens(null);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*
    * Remove astronaut from the ship
     */
    public void removeAstronaut() {
        astronauts--;
    }

    /*
    * Removes an energy from the ship
     */
    public void useEnergy() {
        totalEnergy -= 1;
    }
}