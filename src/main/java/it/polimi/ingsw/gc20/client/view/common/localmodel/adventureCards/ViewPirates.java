package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;

import java.util.List;

public class ViewPirates extends ViewAdvetnureCard {
    int firePower;
    List<Projectile> projectiles;
    int credits;
    int lostDays;

    @Override
    protected void initialize(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.firePower = adventureCard.getFirePower();
        this.projectiles = adventureCard.getProjectiles();
        this.credits = adventureCard.getCredits();
        this.lostDays = adventureCard.getLostDays();
    }

    @Override
    public String toString() {
        return
                up() + "\n" +
                        lateral() + "  Pirates             " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  FirePower: " + firePower + "        " + lateral() + "\n" +
                        lateral() + "  Fires: " + cannonFires() + spaces(13-firesSize()) + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  Credits: " + credits + "$         " + lateral() + "\n" +
                        lateral() + "  LostDays: " + lostDays + "         " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        down();
    }

    private String cannonFires(){
        String result = "";
        for(int i = 0; i < projectiles.size(); i++) {
            if (i == 0) {
                switch(projectiles.get(i).getFireType()) {
                    case HEAVY_FIRE -> result += "H";
                    case LIGHT_FIRE -> result += "L";
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
                    case HEAVY_FIRE -> result += " H";
                    case LIGHT_FIRE -> result += " L";
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

    // ↑→↓←

    private int firesSize(){
        int result = projectiles.size();
        return (result-1)*3 + 2;
    }
}
