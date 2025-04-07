package it.polimi.ingsw.gc20.model.cards;

import java.util.*;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.states.PlanetsState;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

/**
 * @author GC20
 */
public class Planets extends AdventureCard {
    private List<Planet> planets;
    private int lostDays;

    /**
     * Default constructor
     */
    public Planets() {
        super();
        planets = new ArrayList<>();
        lostDays = 0;
    }

    @Override
    public void setState(GameController controller, GameModel model) {
        controller.setState(new PlanetsState(controller, model, planets, lostDays));
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public Planet getPlanet(int index) {
        return planets.get(index);
    }

    public int getLostDays() {
        return lostDays;
    }

    public void setLostDays(int lostDays) {
        this.lostDays = lostDays;
    }
}