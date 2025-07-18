package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;

import java.util.List;

@SuppressWarnings( "unused")
public class ViewPirates extends ViewAdventureCard {
    int firePower;
    List<Projectile> projectiles;
    int credits;
    int lostDays;

    /**
     * Constructs a ViewPirates instance by initializing its fields
     * using the provided AdventureCard object.
     *
     * @param adventureCard the AdventureCard object used to initialize the ViewPirates instance
     */
    public ViewPirates(AdventureCard adventureCard) {
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
                        LATERAL + "\u001B[1m       Pirates        \u001B[0m" + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "     FirePower: \u001B[31m" + firePower + "\u001B[0m     " + LATERAL + "\n" +
                        LATERAL + " ".repeat((22 - 7 - cannonFires().length())/2) + "Fires: \u001B[33m" + cannonFires() + "\u001B[0m" + " ".repeat((22 - 7 - cannonFires().length())/2 + (cannonFires().length() % 2 == 0 ? 1 : 0)) + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "      Credits: \u001B[32m" + credits + "\u001B[0m      " + LATERAL + "\n" +
                        LATERAL + "     Lost days: \u001B[31m" + lostDays + "\u001B[0m     " + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 6, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "\u001B[1m       Pirates        \u001B[0m" + LATERAL;
            case 4 -> LATERAL + "     FirePower: \u001B[31m" + firePower + "\u001B[0m     " + LATERAL;
            case 5 -> LATERAL + " ".repeat((22 - 7 - cannonFires().length())/2) + "Fires: \u001B[33m" + cannonFires() + "\u001B[0m" + " ".repeat((22 - 7 - cannonFires().length())/2 + (cannonFires().length() % 2 == 0 ? 1 : 0)) + LATERAL;
            case 7 -> LATERAL + "      Credits: \u001B[32m" + credits + "\u001B[0m      " + LATERAL;
            case 8 -> LATERAL + "     Lost days: \u001B[31m" + lostDays + "\u001B[0m     " + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }

    /**
     * Constructs a string representation of the cannons firing projectiles.
     * Each projectile is represented by a combination of its fire type and direction.
     * Fire types are represented by "H" for heavy fire and "L" for light fire.
     * Directions are represented by specific arrow symbols:
     * "↑" for DOWN, "↓" for UP, "←" for RIGHT, and "→" for LEFT.
     * The representation adds a space between subsequent projectiles.
     *
     * @return A string representing the sequence of projectiles fired, with their fire types
     *         and directions in order.
     */
    private String cannonFires(){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < projectiles.size(); i++) {
            if (i == 0) {
                switch(projectiles.get(i).getFireType()) {
                    case HEAVY_FIRE -> result.append("H");
                    case LIGHT_FIRE -> result.append("L");
                }

                switch(projectiles.get(i).getDirection()) {
                    case DOWN -> result.append("↑");
                    case UP -> result.append("↓");
                    case RIGHT -> result.append("←");
                    case LEFT -> result.append("→");
                }
            }
            else{
                switch(projectiles.get(i).getFireType()) {
                    case HEAVY_FIRE -> result.append(" H");
                    case LIGHT_FIRE -> result.append(" L");
                }

                switch(projectiles.get(i).getDirection()) {
                    case DOWN -> result.append("↑");
                    case UP -> result.append("↓");
                    case RIGHT -> result.append("←");
                    case LEFT -> result.append("→");
                }
            }
        }
        return result.toString();
    }

    // ↑→↓←
}
