package it.polimi.ingsw.gc20.model.ship;

import java.util.*;

import it.polimi.ingsw.gc20.model.bank.Alien;
import it.polimi.ingsw.gc20.model.bank.Astronaut;
import it.polimi.ingsw.gc20.model.bank.Crew;
import it.polimi.ingsw.gc20.model.components.*;
/**
 * @author GC20
 */
public class NormalShip extends Ship {

    /**
     * Default constructor
     */
    public NormalShip() {
        super();
        brownAlien = false;
        purpleAlien = false;
        table[0][0].setAvailability(false);
        table[0][1].setAvailability(false);
        table[1][0].setAvailability(false);
        table[0][3].setAvailability(false);
        table[0][6].setAvailability(false);
        table[0][5].setAvailability(false);
        table[1][6].setAvailability(false);
        table[4][3].setAvailability(false);
        table[2][3].setAvailability(false);
        Component sc = new StartingCabin();
        table[2][3].addComponent(sc);
    }

    /**
     * Matrx of tiles representing the ship
     */
    private Tile[][] table = new Tile[7][5];

    /**
     *  Components that the player is holding in hand
     */
    private Component[] booked = new Component[2];


    /**
     * if a brown/purple alien is present in the ship
     */
    private Boolean brownAlien;

    private Boolean purpleAlien;

    private AlienColor colorHostable;

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
     * @param c
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
        }
    }

    /**
     * @return the number of rows of the ship
     */
    public Integer getRows(){
        return 7;
    }

    /**
     * @return the number of columns of the ship
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
    protected Component getComponentAt(int row, int col) {
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
            power += cannon.getPower();
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

    /**
     * Special version of update to also update the aliens
     * @param c: the component to be added or removed to the ship
     * @param add: 1 if the component is added, -1 if the component is removed
     */
    @Override
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
            //kill all the astronauts and aliens inside the cabin
            astronauts -= ((Cabin) c).getAstronauts().size();
        } else if (c instanceof CargoHold && add == -1) {
            ((CargoHold) c).getCargoHeld().forEach(k -> cargo.remove(k));
            ((CargoHold) c).cleanCargo();
        } else if(c instanceof LifeSupport){
            updateLifeSupport(c, add);
        }
    }

    @Override
    public void unloadCrew(Crew c) {
        if (c instanceof Alien){
            c.getCabin().unloadAlien((Alien)c);
            if (((Alien) c).getColor() == AlienColor.PURPLE) {
                purpleAlien = false;
            } else {
                brownAlien = false;
            }
        } else {
            c.getCabin().unloadAstronaut((Astronaut)c);
            astronauts--;
        }
    }

    /**
     * Function to update the life support of the ship, we check the components that are connected to the life support if they are a cabin we update the color of the cabin
     * @param c: the component that was added or removed from the ship
     * @param add: if the component was added or removed
     */
    private void updateLifeSupport(Component c, Integer add) {
        int row, col, i=0, j=0;
        outerloop:
        for(i = 0; i < getRows(); i++){
            for(j = 0; j < getCols(); j++){
                if(table[i][j].getComponent()== c){
                    break outerloop;
                }
            }
        }
        Map<Direction, ConnectorEnum> connectors = c.getConnectors();
        for (Map.Entry<Direction, ConnectorEnum> entry : connectors.entrySet()) {
            row = i;
            col = j;
            if (entry.getValue() == ConnectorEnum.ZERO || !(table[i][j].getComponent() instanceof  Cabin)) {
                continue;
            }
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
            if(c.isValid(table[row][col].getComponent(), entry.getKey())){
                Cabin comp = (Cabin)table[row][col].getComponent();
                if(add==1){
                    comp.addSupport((LifeSupport) c);
                }else{
                    try {
                        comp.removeSupport((LifeSupport) c);
                    } catch (Exception e) {
                        super.updateParameters(c, -1);
                    }
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
    public void addAlien(Alien alien, Component c) throws IllegalArgumentException {
        if(((Cabin) c).getColor() != alien.getColor() && ((Cabin) c).getColor() != AlienColor.BOTH)
            throw new IllegalArgumentException("this cabin cannot host this alien");
        ((Cabin) c).setAlien(alien);
        if(alien.getColor() == AlienColor.BROWN){
            brownAlien = true;
        }else{
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

    /*
     * Function to remove aliens from the ship
     */
    public void removeAlien(Alien alien){
        if(alien.getColor() == AlienColor.BROWN){
            brownAlien = false;
        }else{
            purpleAlien = false;
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

    public void addAlien(Cabin c, Alien a) {
        if (c.getOccupants() != 0) {
            throw new IllegalArgumentException("Cannot add alien to cabin already occupied by astronauts/alien");
        }
        if (c.getColor() != a.getColor() && c.getColor() != AlienColor.BOTH) {
            throw new IllegalArgumentException("Cannot add " + a.getColor().toString() + " alien to " + c.getColor().toString() + " cabin");
        }
        c.setAlien(a);
        this.updateParameters(c, 1);
    }

    public void removeAlien(Cabin c, Alien a) {
        if (c.getAlien() == null) {
            throw new IllegalArgumentException("Cannot remove alien from empty cabin");
        }
        if (c.getAlien() != a) {
            throw new IllegalArgumentException("Cannot remove alien from cabin if it is not the alien in the cabin");
        }
        c.setAlien(null);
        this.updateParameters(c, -1);
    }

    public void removeAlien(Cabin c) {
        if (c.getAlien() == null) {
            throw new IllegalArgumentException("Cannot remove alien from empty cabin");
        }
        c.setAlien(null);
        this.updateParameters(c, -1);
    }
}