package it.polimi.ingsw.gc20.model.bank;

import it.polimi.ingsw.gc20.model.components.CargoHold;
import it.polimi.ingsw.gc20.model.components.SpecialCargoHold;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public class Cargo {

    private CargoHold cargoHold;
    private CargoColor color;

    /**
     * Default constructor
     */
    public Cargo() {
        this.color = null;
        this.cargoHold = null;
    }

    /** get function for the color of the cargo
     * @return CargoColor color of the cargo
     */
    public void setColor(CargoColor color) {
        this.color = color;
    }

    /** get function for the color of the cargo
     * @return CargoColor color of the cargo
     */
    public CargoColor getColor() {
        return this.color;
    }
    /** set function for the storage of the cargo
     * @param ch storage of the cargo
     * throws IllegalArgumentException if the cargo is red and it trying to be stored in a non special cargo hold
     */
    public void setCargoHold(CargoHold ch) throws IllegalArgumentException {
        if (this.color == RED) {
            if (!ch instanceof SpecialCargoHold) {
                throw new IllegalArgumentException("red cargo can only be stored in a special cargo hold");
            } else {
                this.cargoHold = ch;
            }
        } else {
            this.cargoHold = ch;
        }
    }

    /** get function for the storage of the cargo
     * @return cargoHold storage of the cargo
     */
    public CargoHold getCargoHold() {
        return this.cargoHold;
    }

}