package it.polimi.ingsw.gc20.model.components;
import it.polimi.ingsw.gc20.model.gamesets.Cargo;


import java.util.*;

public class CargoHold extends Component {

    private List<Cargo> cargoHeld;

    private Integer space;

    public CargoHold() {
    }

    public List<Cargo> getCargoHeld() {
        return cargoHeld;
    }

    public Integer getSpace() {
        return space;
    }

    public void setSpace(Integer space) {
        this.space = space;
    }

    public void setCargoHeld(List<Cargo> cargoHeld) {
        this.cargoHeld = cargoHeld;
    }

    /*
        * This method is used to load a cargo in the cargo hold
        * @param g the cargo to be loaded
     */
    public void loadCargo(Cargo g) {
        cargoHeld.add(g);
    }

    /*
        * This method is used to unload a cargo from the cargo hold
        * @param i the index of the cargo to be unloaded
     */
    public void unloadCargo(Integer i) {
        cargoHeld.remove(i);
        //Scroll the list to the left
        for (int j = i; j < cargoHeld.size(); j++) {
            cargoHeld.set(j, cargoHeld.get(j + 1));
        }
    }

}