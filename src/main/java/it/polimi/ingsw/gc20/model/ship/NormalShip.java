package it.polimi.ingsw.gc20.model.ship;

import java.util.*;

import it.polimi.ingsw.gc20.model.bank.Alien;
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

        //initialize table
        for (int i=0; i<7; i++) {
            for (int j=0; j<5; j++) {
                table[i][j] = new Tile();
            }
        }

        table[0][0].setAvailability(false);
        table[0][1].setAvailability(false);
        table[1][0].setAvailability(false);
        table[3][0].setAvailability(false);
        table[6][0].setAvailability(false);
        table[0][5].setAvailability(false);//
        table[6][1].setAvailability(false);
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
     * @param c
     * @return
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
        } else if (c instanceof Cabin) {
            //kill all the astronauts and aliens inside the cabin
            astronauts += ((Cabin) c).getAstronauts().size()*add;
            if(((Cabin) c).getAlien() != null){
                if(((Cabin) c).getAlien().getColor() == AlienColor.BROWN){
                    brownAlien = (add == 1);
                }else{
                    purpleAlien = (add == 1);
                }
            }
        } else if (c instanceof CargoHold && add == -1) {
            ((CargoHold) c).getCargoHeld().forEach(k -> cargo.remove(k));
            ((CargoHold) c).cleanCargo();
        }
    }

    /**
     * Function to add an alien to the ship
     * @param alien: the alien to be added
     * @param c: the cabin where the alien will be added
     * @throws IllegalArgumentException: the cabin cannot host the alien
     */
    public void addAlien(Alien alien, Component c) throws IllegalArgumentException {
        if(((Cabin) c).getColor() != alien.getColor())
            throw new IllegalArgumentException("this cabin cannot host this alien");
        ((Cabin) c).setAliens(alien);
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
}