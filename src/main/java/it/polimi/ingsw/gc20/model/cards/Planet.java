package it.polimi.ingsw.gc20.model.cards;

import java.io.*;
import java.util.*;
import it.polimi.ingsw.gc20.model.bank.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.gamesets.*;

/**
 * @author GC20
 */
public class Planet {
    private List<CargoColor> reward;
    private Boolean available;
    private Player player;

    /**
     * Default constructor
     */
    public Planet() {
        reward = new ArrayList<>();
        available = true;
    }

    public List<CargoColor> getReward() {
        return reward;
    }

    public void setReward(List<CargoColor> reward) {
        this.reward = reward;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
    
    public void setReward(CargoColor reward) {
        this.reward.add(reward);
    }
    
    public void setAvailable() {
        this.available = true;
    }
    
    public void setUnavailable() {
        this.available = false;
    }
    
    public List<Cargo> land (Game g, Player player) {
        this.player = player;
        this.setUnavailable();
        return reward.stream().filter(color -> g.getCargoAvailable(color) > 0)
                .peek(g::removeCargoAvailable)
                .map(Cargo::new)
                .toList();
    }

    public void leave() {
        this.player = null;
        this.setAvailable();
    }
}