package it.polimi.ingsw.gc20.server.model.cards;

import java.io.Serializable;
import java.util.*;

import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.player.Player;

/**
 * @author GC20
 */
public class Planet implements Serializable {
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reward:");
        for (CargoColor color : reward) {
            sb.append(" ").append(color);
        }
        sb.append(" Available: ").append(available);
        if (player != null) {
            sb.append(" Player: ").append(player.getUsername());
        }
        return sb.toString();
    }

}