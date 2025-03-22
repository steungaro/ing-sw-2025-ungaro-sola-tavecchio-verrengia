package it.polimi.ingsw.gc20.model.cards;

import java.util.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.gamesets.*;

/**
 * @author GC20
 */
public class Planet {
    private List<CargoColor> reward;
    private Boolean available;
    private Player player = null;

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
    
    public void setUnavailable() {
        this.available = false;
    }
    
    public List<CargoColor> land (Player player) {
        this.player = player;
        this.setUnavailable();
        return reward;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}