package it.polimi.ingsw.gc20.model.ship;

import java.util.*;
import it.polimi.ingsw.gc20.model.components.*;
/**
 * @author GC20
 */
public class NormalShip extends Ship {

    public NormalShip() {
        super();
        brownAlien = false;
        purpleAlien = false;

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
        addComponent(sc, 2, 3);
        table[2][3].setAvailability(false);
    }

    /**
     * Matrix of tiles representing the ship
     */
    private Tile[][] table = new Tile[5][7];

    /**
     *  Components that the player is holding in hand
     */
    private Component[] booked = new Component[2];


    /**
     * if a brown/purple alien is present in the ship
     */
    private Boolean brownAlien = false;

    private Boolean purpleAlien = false;

    private AlienColor colorHostable = AlienColor.NONE;

    public AlienColor getColorHostable() {
        return colorHostable;
    }

    public void setColorHostable(AlienColor colorHostable) {
        this.colorHostable = colorHostable;
    }

    /**
     * Function to be called at the end of construction phase to move the booked components to the waste
     */
    public void addBookedToWaste() {
        trash.addAll(Arrays.asList(booked));
        booked[0]=null;
        booked[1]=null;
    }

    /**
     * Function to remove a component from the booked components
     * @param c is the component to be removed
     * @throws IllegalArgumentException Component not valid, not in booked
     */
    public void removeBooked(Component c) throws IllegalArgumentException {
        if (booked[0] == c) {
            booked[0]=null;
        } else if (booked[1] == c) {
            booked[1]=null;
        }else{
            throw new IllegalArgumentException("Component not found");
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
     */
    public void addBooked(Component c) {
        if (booked[0] == null) {
            booked[0]=c;
        } else if (booked[1] == null) {
            booked[1]=c;
        } else {
            throw new IllegalArgumentException("Already 2 booked components");
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
     */
    @Override
    protected void setComponentAt(Component c, int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            table[row][col].addComponent(c);
        }
    }

    /**
     * Function to calculate the firepower of the ship
     * @param cannons Set<Component>: the double cannons the user wants to activate
     * @return power: the firepower of the ship
     */
    @Override
    public float firePower(Set<Cannon> cannons, Integer energies) throws IllegalArgumentException {
        if(energies > totalEnergy || energies != cannons.size())
            throw new IllegalArgumentException("cannon size too large");
        float power  = singleCannonsPower;
        for(Cannon cannon : cannons){
            if (cannon.getOrientation() == Direction.UP)
                power += cannon.getPower();
            else {
                power += cannon.getPower() / 2;
            }
        }
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

    @Override
    public void unloadCrew(Cabin c) {
        if (c.getOccupants() < 1) {
            throw new IllegalArgumentException("Empty cabin");
        }
        if (c.getAlien()) {
            if (c.getAlienColor() == AlienColor.BROWN) {
                brownAlien = false;
            } else {
                purpleAlien = false;
            }
            c.unloadAlien();
        } else {
            c.unloadAstronaut();
            astronauts--;
        }
    }

    /**
     * Function to update the life support of the ship, we check the components that are connected to the life support if they are a cabin we update the color of the cabin
     * @param c: the component that was added or removed from the ship
     */
    private void updateLifeSupportRemoved(Component c) {
        //Find if is there a Cabin connceted to the LifeSupport
        int row, col, i, j;
        i = 0;
        j = 0;
        outer_loop:
        for (i = 0; i < getRows(); i++) {
            for (j = 0; j < getCols(); j++) {
                if (table[i][j].getComponent() == c) {
                    break outer_loop;
                }
            }
        }
        //Finded the lifeSupport
        Map<Direction, ConnectorEnum> connectors = c.getConnectors();
        for (Map.Entry<Direction, ConnectorEnum> entry : connectors.entrySet()) {
            row = i;
            col = j;

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

            if (entry.getValue() == ConnectorEnum.ZERO || !(table[row][col].getComponent() instanceof Cabin)) {
                continue;
            }

            if (c.isValid(table[row][col].getComponent(), entry.getKey())) {
                Cabin comp = (Cabin) table[row][col].getComponent();
                try {
                    comp.removeSupport((LifeSupport) c);
                } catch (Exception e) {
                    updateParametersRemove(c);
                }
            }
        }
    }

    // For
    private void updateLifeSupportAdded(Component c) {
        //Find if is there a Cabin connceted to the LifeSupport
        int row, col, i, j;
        i = 0;
        j = 0;
        outer_loop:
        for (i = 0; i < getRows(); i++) {
            for (j = 0; j < getCols(); j++) {
                if (table[i][j].getComponent() == c) {
                    break outer_loop;
                }
            }
        }
        //Finded the lifeSupport
        Map<Direction, ConnectorEnum> connectors = c.getConnectors();
        for (Map.Entry<Direction, ConnectorEnum> entry : connectors.entrySet()) {
            row = i;
            col = j;

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

            if (entry.getValue() == ConnectorEnum.ZERO || !(table[row][col].getComponent() instanceof Cabin)) {
                continue;
            }

            if (c.isValid(table[row][col].getComponent(), entry.getKey())) {
                Cabin comp = (Cabin) table[row][col].getComponent();
                try {
                    comp.addSupport((LifeSupport) c);
                } catch (Exception e) {
                    updateParametersSet(c);
                }
            }
        }
    }

    /**
     * Function to add an alien to the ship
     * @param alien: the alien to be added
     * @param c: the cabin where the alien will be added
     * @throws IllegalArgumentException: the cabin cannot host the alien
     */
    public void addAlien(AlienColor alien, Cabin c) throws IllegalArgumentException {
        c.setAlien(alien);
        if (alien == AlienColor.BROWN){
            brownAlien = true;
        } else {
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
     */
    public void addComponent(Component c, int row, int col){
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            setComponentAt( c, row, col);
            updateParametersSet(c);
        }
        else
            throw new IllegalArgumentException("Invalid position");
    }

    public void removeAlien(Cabin c) {
        if(!c.getAlien()) {
            throw new IllegalArgumentException("No alien in the cabin");
        }
        unloadCrew(c);
    }

    /**
     * Updates ship parameters when components are removed
     * @param c Component being removed
     */
    @Override
    protected void updateParametersRemove(Component c){
        if(c instanceof Cannon){
            if(((Cannon) c).getOrientation()==Direction.UP){
                if(((Cannon) c).getPower() == 1){
                    singleCannonsPower--;
                }else{
                    doubleCannonsPower -= 2;
                }
            }else{
                if(((Cannon) c).getPower() == 1) {
                    doubleCannons--;
                    doubleCannonsPower--;
                }else if(((Cannon) c).getPower() == 0.5f){
                    singleCannonsPower -= 0.5f;
                }
            }
        }else if(c instanceof Engine){
            if(((Engine) c).getDoublePower()){
                doubleEngines--;
            }else{
                singleEngines--;
            }
        }else if(c instanceof Battery){
            totalEnergy -= ((Battery) c).getAvailableEnergy();
        } else if (c instanceof Cabin) {
            if (((Cabin) c).getAlien()) {
                if (((Cabin) c).getAlienColor() == AlienColor.BROWN) {
                    brownAlien = false;
                } else {
                    purpleAlien = false;
                }
                ((Cabin) c).unloadAlien();
            } else {
                astronauts -= ((Cabin) c).getAstronauts();
                ((Cabin) c).setAstronauts(0);
            }
        } else if (c instanceof CargoHold) {
            ((CargoHold) c).getCargoHeld().forEach((k, v) -> {
                Integer current = cargos.getOrDefault(k, 0);
                cargos.put(k, current - v);
            });
        } else if (c instanceof LifeSupport) {
            updateLifeSupportRemoved(c);
        }
    }

    @Override
    protected void updateParametersSet(Component c){
        if(c instanceof Cannon){
            if(((Cannon) c).getOrientation()==Direction.UP){
                if(((Cannon) c).getPower() == 1){
                    singleCannonsPower += 1;
                }else{
                    doubleCannonsPower += 2;
                }
            }else{
                if(((Cannon) c).getPower() == 1) {
                    doubleCannons += 1;
                    doubleCannonsPower += 1;
                }else if(((Cannon) c).getPower() == 0.5f){
                    singleCannonsPower += 0.5f;
                }
            }
        }else if(c instanceof Engine){
            if(((Engine) c).getDoublePower()){
                doubleEngines += 1;
            }else{
                singleEngines += 1;
            }
        }else if(c instanceof Battery) {
            ((Battery) c).fillBattery();
            totalEnergy += ((Battery) c).getSlots();
        }else if(c instanceof Cabin) {
            //Find if is there a LifeSupport connceted to the Cabin
            int row, col, i, j;
            i = 0;
            j = 0;
            outer_loop:
            for (i = 0; i < getRows(); i++) {
                for (j = 0; j < getCols(); j++) {
                    if (table[i][j].getComponent() == c) {
                        break outer_loop;
                    }
                }
            }
            //Finded the Cabin
            Map<Direction, ConnectorEnum> connectors = c.getConnectors();
            for (Map.Entry<Direction, ConnectorEnum> entry : connectors.entrySet()) {
                row = i;
                col = j;

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

                if (entry.getValue() == ConnectorEnum.ZERO || !(table[row][col].getComponent() instanceof LifeSupport)) {
                    continue;
                }

                if (c.isValid(table[row][col].getComponent(), entry.getKey())) {
                    Cabin comp = (Cabin) table[i][j].getComponent();
                    try {
                        comp.addSupport((LifeSupport) c);
                    } catch (Exception e) {
                        updateParametersSet(c);
                    }
                }
            }
        } else if (c instanceof LifeSupport) {
            updateLifeSupportAdded(c);
        }
        // cargoHolds and shields are not counted in the updateParametersSet
    }
}