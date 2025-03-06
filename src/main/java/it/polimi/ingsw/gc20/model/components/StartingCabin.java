package it.polimi.ingsw.gc20.model.components;

public class StartingCabin {

    private Integer astronauts;

    public StartingCabin() {
    }

    public void setAstronauts(Integer a) {
        astronauts = a;
        return;
    }

    public Integer getAstronauts() {
        return astronauts;
    }

    public void unloadAstronauts(Integer a) {
        setAstronauts(getAstronauts() - a);
        return;
    }

}