package it.polimi.ingsw.gc20.model.ship;

import Components.Component;
import it.polimi.ingsw.gc20.model.components.ConnectorEnum;
import it.polimi.ingsw.gc20.model.components.Direction;

import java.util.*;

/**
 * @author GC20
 */
public abstract class Ship {

    /**
     * Default constructor
     */
    public Ship() {
    }

    /**
     * 
     */
    private Set<Component> waste;

    private Integer singleEngines;
    /**
     * 
     */
    private Integer doubleCannons;

    /**
     * 
     */
    private Integer doubleEngines;

    /**
     * 
     */
    private Integer doubleCannonsPower;
    /**
     * Get the number of rows in this ship
     * @return number of rows
     */
    public abstract Integer getRows();

    /**
     * Get the number of columns in this ship
     * @return number of columns
     */
    public abstract Integer getCols();

    /**
     * 
     */
    private Float singleCannonsPower;

    private Integer totalEnergy;

    private List<Integer> cargo;

    protected abstract Component getComponentAt(int row, int col);
    /**
     * Function that determines the total firepower of the ship
     * it is the sum of single cannons power and double cannons power based on their orientation (cannons facing north have full power, others have half power)
     * the user will select one by one the cannons he wants to use (single cannons automatically selected) every time he selects a cannon the power of the ship will be recalculated
     * it also checks if the ship has the necessary amount of batteries
     * @param cannoni
     * @return power
     */
    public float firePower(Set<Component> cannoni) {
        if(cannoni.size()>totalEnergy)
            throw new IllegalArgumentException("cannon size too large");
        float power  = singleCannonsPower;
        for(Component cannon : cannoni){
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

        return null;
    }

    /**
     * Function that gets the first component of the ship from a certain direction to determine what component will be hit
     *
     * @param Direction d: the direction from which the component will be hit
     * @param Integer n: row or column of the component ATTENTION it is the row or colum get from the dice NOT the row or column of the ship
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
     * @param Direction d
     * @return if side is shielded
     */
    public Boolean getShield(Direction d) {
        //parse throw the component of the ship until it finds a shield that covers the direction d or until all pieces are checked
        int rows = getRows();
        int cols = getCols();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(getComponentAt(i, j) && getComponentAt(i, j) instanceof Shield && getComponentAt(i, j).getCoveredSides().contains(d)){
                    return true;
                }
            }
        }
        return null;
    }

    /**
     * Check if there is a cannon that points to a certain direction in a particular row/column
     * @param Direction d
     * @param Integer n 
     * @return
     */
    public Boolean getCannon(Direction d, Integer n) {
        int cols = getCols();
        int rows = getRows();
        if ((d == Direction.UP || d == Direction.DOWN) && (n >= 0 && n < cols)) {
            for(int row = 0; row < rows; row++){
                Component component = getComponentAt(row, n);
                if(component != null && component instanceof Cannon &&
                        component.getConnectors().get(d) != ConnectorEnum.ZERO) {
                    return true;
                }
            }
            return false;
        }
        if((d == Direction.LEFT || d == Direction.RIGHT) && (n >= 0 && n < rows)){
            for(int col = 0; col < cols; col++){
                Component component = getComponentAt(n, col);
                if(component != null && component instanceof Cannon &&
                        component.getConnectors().get(d) != ConnectorEnum.ZERO) {
                    return true;
                }
            }
            return false;
        }
        return false;
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
     * @return
     */
    public boolean isValid() {
        int rows = getRows();
        int cols = getCols();
        boolean[][] visited = new boolean[rows][cols];
        int startRow = rows/2, startCol = cols/2;

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
                if (adjRow >= 0 && adjRow < rows && adjCol >= 0 && adjCol < cols) {
                    Component adjComponent = getComponentAt(adjRow, adjCol);
                    if (adjComponent != null && !visited[adjRow][adjCol]) {
                        // Check if adjacent component has a matching connector
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
     * @param Component c 
     * @return
     */
    public void addToWaste(Component c) {
        waste.add(c);
    }

    public void killComponent(Tile t){
        Component c = t.getComponent();
        waste.add(c);
        t.removeComponent();
    }

}