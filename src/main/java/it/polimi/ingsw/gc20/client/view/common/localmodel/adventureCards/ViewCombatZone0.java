package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;

import java.util.List;

public class ViewCombatZone0 extends ViewAdventureCard {
    int lostDays;
    int lostCrew;
    List<Projectile> projectiles;

    @Override
    protected void initialize(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.lostCrew = adventureCard.getCrew();
        this.lostDays = adventureCard.getLostDays();
        this.projectiles = adventureCard.getProjectiles();
    }

    @Override
    public String toString() {
        return
                up() + "\n" +
                        lateral() + "  Combat Zone         " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  Crew: " + lostDays + " LostDays    " + lateral() + "\n" +
                        lateral() + "  EnPow: " + lostCrew + " LostCrew   " + lateral() + "\n" +
                        lateral() + "  Fires: " + cannonFires() + spaces(13-firesSize()) + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
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
