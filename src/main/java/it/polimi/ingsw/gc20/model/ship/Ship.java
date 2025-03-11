package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.Cargo;

import java.util.*;

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
    protected Integer aliens;

    /**
     * Default constructor
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
        aliens = 0;
    }

    public void addComponent(Component c, int row, int col){
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            setComponentAt( c, row, col);
            updateParameters(c, 1);
        }
    }
    public abstract Integer getRows();

    /**
     * Get the number of columns in this ship
     * @return number of columns
     */
    public abstract Integer getCols();

    protected abstract Component getComponentAt(int row, int col);

    protected abstract void setComponentAt(Component c, int row, int col);
    /**
     * Function that determines the total firepower of the ship
     * it is the sum of single cannons power and double cannons power based on their orientation (cannons facing north have full power, others have half power)
     * the user will select one by one the cannons he wants to use (single cannons automatically selected) every time he selects a cannon the power of the ship will be recalculated
     * it also checks if the ship has the necessary amount of batteries
     * @param cannons Set<Component>: the double cannons the user wants to activate
     * @return power
     */
    public float firePower(Set<Cannon> cannons) {
        if(cannons.size()>totalEnergy)
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
     * @return
     */
    public Integer getTotalEnergy() {
        return totalEnergy;
    }

    /**
     * @return
     */
    public Integer crew() {
        //TODO implement here
        return null;
    }

    /**
     * Function that gets the first component of the ship from a certain direction to determine what component will be hit
     *
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
                if(getComponentAt(i, j) && getComponentAt(i, j) instanceof Shield && ((Shield) (getComponentAt(i, j)).getCoveredSides().contains(d))){
                    return true;
                }
            }
        }
        return null;
    }

    /**
     * Check if there is a cannon that points to a certain direction in a particular row/column
     * @param d Direction
     * @param n Integer
     * @return
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
     * Get the number of connectors that are exposed to space
     * We check all the components at the border of the ship and we count the number of sides that have connectors not attached to another connector
     * @return exposedConnectors
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
     * In order to be valid a ship must have all components connected directly or indirectly to the StartingCabin
     * A component is connected to the StartingCabin if there is a path of connectors that connect the component to the StartingCabin
     * @parameter
     * @return if a ship is valid
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
     * @return
     */
    public Set<Component> getWaste() {
        return waste;
    }

    /**
     * @param c Component
     * @return
     */
    public void addToWaste(Component c) {
        waste.add(c);
    }

    public Boolean killComponent(Tile t){
        Component c = t.getComponent();
        updateParameters(c, -1);
        waste.add(c);
        t.removeComponent();
        return this.isValid();
    }

    /**
     *
     * @param c: the component to be added or removed to the ship
     * @param add: 1 if the component is added, -1 if the component is removed
     */
    public void updateParameters(Component c, Integer add){
        if(c instanceof Cannon){
            if(((Cannon) c).getOrientation()=Direction.UP){
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
            if(((Engine) c).isdouble()){
                doubleEngines += add;
            }else{
                singleEngines += add;
                }
        }else if(c instanceof Battery){
            totalEnergy -= ((Battery) c).getEnergy().length();
        } else if (c instanceof Cabin && add == -1) {
            //kill all the astronauts inside the cabin
            astronauts -= ((Cabin) c).getAstronauts().length();
        } else if (c instanceof CargoHold && add == -1) {
            ((CargoHold) c).getCargoHeld().forEach(k -> cargo.remove(k));
            ((CargoHold) c).cleanCargo();
        }
    }
}