package it.polimi.ingsw.gc20.model.components;
import it.polimi.ingsw.gc20.model.bank.Astronaut;

import java.util.ArrayList;
import java.util.List;

public class StartingCabin extends Cabin {

    private List<Astronaut> astronauts;

    public StartingCabin() {
        astronauts = new ArrayList<Astronaut>();
    }

    /**
     * function to set the astronauts
     * @param a the astronauts to set
     */
    public void setAstronauts(List<Astronaut> a) {
        this.astronauts = a;
    }

    /**
     * function to get the astronauts
     * @return the astronauts
     */
    public List<Astronaut> getAstronauts() {
        return astronauts;
    }

    /**
     * function to unload the astronauts
     * @param a the astronaut to unload
     */
    public void unloadAstronauts(Astronaut a) {
        astronauts.remove(a);
    }

    /**
     * function to add an astronaut
     * @param a the astronaut to add
     */
    public void addAstronaut(Astronaut a) {
        astronauts.add(a);
        return;
    }

}