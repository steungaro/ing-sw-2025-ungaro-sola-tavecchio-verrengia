package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import java.util.ArrayList;
import java.util.List;

public class ViewMeteorSwarm extends ViewAdvetnureCard{
    List<Projectile> projectiles;

    @Override
    public String toString(){
        return
        up() + "\n" +
                lateral() + "  Meteor Swarm        " + lateral() + "\n" +
                lateral() + EMPTY_ROW + lateral() + "\n" +
                lateral() + EMPTY_ROW + lateral() + "\n" +
                lateral() + "  Fires: " + meteorFires() + spaces(13-firesSize()) + lateral() + "\n" +
                lateral() + EMPTY_ROW + lateral() + "\n" +
                lateral() + EMPTY_ROW + lateral() + "\n" +
                lateral() + EMPTY_ROW + lateral() + "\n" +
                lateral() + EMPTY_ROW + lateral() + "\n" +
                down();
    }

    private String meteorFires(){
        String result = "";
        for(int i = 0; i < projectiles.size(); i++) {
            if (i == 0) {
                switch(projectiles.get(i).getFireType()) {
                    case HEAVY_METEOR -> result += "H";
                    case LIGHT_METEOR -> result += "L";
                }

                switch(projectiles.get(i).getDirection()) {
                    case UP -> result += "↑";
                    case DOWN -> result += "↓";
                    case LEFT -> result += "←";
                    case RIGHT -> result += "→";
                }
            }
            else{
                switch(projectiles.get(i).getFireType()) {
                    case HEAVY_METEOR -> result += " H";
                    case LIGHT_METEOR -> result += " L";
                }

                switch(projectiles.get(i).getDirection()) {
                    case UP -> result += "↑";
                    case DOWN -> result += "↓";
                    case LEFT -> result += "←";
                    case RIGHT -> result += "→";
                }
            }
        }
        return result;
    }

    private int firesSize(){
        int result = projectiles.size();
        return (result-1)*3 + 2;
    }

}
