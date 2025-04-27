package it.polimi.ingsw.gc20.server.model.cards;

import java.util.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.player.Player;

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

    
    public List<CargoColor> land (Player player) {
        this.player = player;
        this.setAvailable(false);
        return reward;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}