package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;

import java.util.List;

public class ViewPirates extends ViewAdventureCard {
    int firePower;
    List<Projectile> projectiles;
    int credits;
    int lostDays;

    protected ViewPirates(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.firePower = adventureCard.getFirePower();
        this.projectiles = adventureCard.getProjectiles();
        this.credits = adventureCard.getCredits();
        this.lostDays = adventureCard.getLostDays();
    }

    public ViewPirates() {
        super();
    }

    @Override
    public String toString() {
        return
                UP + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "       Pirates        " + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "     FirePower: " + firePower + "     " + LATERAL + "\n" +
                        LATERAL + " ".repeat((22 - 7 - cannonFires().length())/2) + "Fires: " + cannonFires() + " ".repeat((22 - 7 - cannonFires().length())/2 + (cannonFires().length() % 2 == 0 ? 1 : 0)) + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "      Credits: " + credits + "      " + LATERAL + "\n" +
                        LATERAL + "     Lost days: " + lostDays + "     " + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 6, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "       Pirates        " + LATERAL;
            case 4 -> LATERAL + "     FirePower: " + firePower + "     " + LATERAL;
            case 5 -> LATERAL + " ".repeat((22 - 7 - cannonFires().length())/2) + "Fires: " + cannonFires() + " ".repeat((22 - 7 - cannonFires().length())/2 + (cannonFires().length() % 2 == 0 ? 1 : 0)) + LATERAL;
            case 7 -> LATERAL + "      Credits: " + credits + "      " + LATERAL;
            case 8 -> LATERAL + "     Lost days: " + lostDays + "     " + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
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
}
