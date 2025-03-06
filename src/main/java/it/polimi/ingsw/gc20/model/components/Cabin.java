package it.polimi.ingsw.gc20.model.components;

import java.util.*;

public class Cabin extends Component {

    private Integer astronauts;
    private Integer purpleAliens;
    private Integer brownAliens;
    private Integer[] support = new Integer[2]; // 0 = purple, 1 = brown
 // 0 = purple, 1 = brown

    public Cabin() {
    }

    public Integer getAstronauts() {
        return astronauts;
    }

    public void setAstronauts(Integer astronauts) {
        this.astronauts = astronauts;
    }

    public Integer getPurpleAliens() {
        return purpleAliens;
    }

    public void setPurpleAliens(Integer purpleAliens) {
        this.purpleAliens = purpleAliens;
    }

    public Integer getBrownAliens() {
        return brownAliens;
    }

    public void setBrownAliens(Integer brownAliens) {
        this.brownAliens = brownAliens;
    }

    public Integer[] getSupport() {
        return support;
    }

    public void setSupport(Integer[] support) {
        this.support = support;
    }

    /*
        * Function that unloads the astronauts from the cabin.
        * @param n the number of astronauts to unload
     */
    public void unloadAstronauts(Integer n) {
        if(getAstronauts()-n > 0)
            setAstronauts(getAstronauts() - n);
        else
            setAstronauts(0);
    }

    /*
        * Function that unloads the aliens from the cabin.
        * @param color the color of the aliens to unload
        * @param n the number of aliens to unload
     */
    public void unloadAliens(Boolean color, Integer n) {
        if(!color) {
            if(getPurpleAliens()-n > 0)
                setPurpleAliens(getPurpleAliens() - n);
            else
                setPurpleAliens(0);
        } else {
            if(getBrownAliens()-n > 0)
                setBrownAliens(getBrownAliens() - n);
            else
                setBrownAliens(0);
        }
    }

    /*
        * Function that adds support to the cabin.
        * @param color the color of the support to add
     */
    public void addSupport(Boolean color) {
        if(!color) {
            support[0]++;
        } else {
            support[1]++;
        }
    }

    /*
        * Function that removes support from the cabin.
        * @param color the color of the support to remove
     */
    public void removeSupport(Boolean color) {
        if(!color) {
            support[0]--;
        }
        else {
            support[1]--;
        }
    }

}