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
     * 
     */
    private Tile[][] table = new Tile[7][5];

    /**
     * 
     */
    private Component[] booked = new Component[2];

    /**
     * 
     */

    /**
     * 
     */
    private Boolean brownAlien;

    private Boolean purpleAlien;


    /**
     * @return
     */
    public void addBookedToWaste() {
        trash.addAll(Arrays.asList(booked));
        booked[0]=null;
        booked[1]=null;
    }

    /**
     * @param c
     * @return
     * @throws IllegalArgumentException Component not valid, not in booked
     */
    public void removeBooked(Component c) {
        if (booked[0] == c) {
            booked[0]=null;
        } else if (booked[1] == c) {
            booked[1]=null;
        }else{
            throw new IllegalArgumentException("Component not found");
        }
    }

    /**
     * @return
     */
    public List<Component> getBooked() {
        return Arrays.asList(booked);
    }

    /**
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


    public Integer getRows(){
        return 7;
    }

    public Integer getCols(){
        return 5;
    }

    @Override
    protected Component getComponentAt(int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            return table[row][col].getComponent();
        }
        return null;
    }

    @Override
    protected void setComponentAt(Component c, int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            table[row][col].addComponent(c);
        }
    }

    @Override
    public float firePower(Set<Cannon> cannons) {
        if(cannons.size()>totalEnergy)
            throw new IllegalArgumentException("cannon size too large");
        float power  = singleCannonsPower;
        for(Cannon cannon : cannons){
            power += cannon.getPower();
        }
        return power + (purpleAlien ? 2 : 0);
    }

    @Override
    public Integer enginePower(Integer doubleEnginesActivated) {
        return doubleEnginesActivated * 2 + singleEngines + (brownAlien ? 2 : 0);
    }

    @Override
    /**
     * Special version of update to also update the aliens
     * @param c: the component to be added or removed to the ship
     * @param add: 1 if the component is added, -1 if the component is removed
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
}