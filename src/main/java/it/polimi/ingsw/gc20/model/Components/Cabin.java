package it.polimi.ingsw.gc20.model.Components;

import java.io.*;
import java.util.*;

public class Cabin extends Component {

    private Integer astronauts;
    private List aliens;
    private Boolean canHostAliens;
    private AlienColor[] LifeSupport = new AlienColor[2];

    public Cabin() {
    }

    public Integer getAstronauts() {
        return astronauts;
    }

    public void setAstronauts(Integer astronauts) {
        this.astronauts = astronauts;
    }

    public Integer getAliens() {
        return aliens;
    }

    public void setAliens(Integer aliens) {
        this.aliens = aliens;
    }

    public Boolean getCanHostAliens() {
        return canHostAliens;
    }

    public void setCanHostAliens(Boolean canHostAliens) {
        this.canHostAliens = canHostAliens;
    }

    public AlienColor getColor() {
        return color;
    }

    public void setColor(AlienColor color) {
        this.color = color;
    }

    public void unloadAstronauts(Integer n) {
        if(getAstronauts()-n > 0)
            setAstronauts(getAstronauts() - n);
        else
            setAstronauts(0);
    }

    public void unloadAliens(Integer n) {
        if(getAstronauts()-n > 0)
            setAliens(getAliens() - n);
        else
            setAliens(0);
    }

    public void addSupport(AlienColor c) {
        this.setColor(c);
    }

    public void removeSupport() {
        // TODO implement here
    }
}