package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;

import java.util.List;

@SuppressWarnings( "unused")
public class ViewCombatZone0 extends ViewAdventureCard {
    int lostDays;
    int lostCrew;
    List<Projectile> projectiles;

    /**
     * Constructs a new ViewCombatZone0 object by initializing it with the given AdventureCard.
     * This constructor sets the lostCrew, lostDays, and projectiles fields based on the properties
     * of the provided AdventureCard.
     *
     * @param adventureCard the AdventureCard instance containing the data used to initialize this object
     */
    public ViewCombatZone0(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.lostCrew = adventureCard.getCrew();
        this.lostDays = adventureCard.getLostDays();
        this.projectiles = adventureCard.getProjectiles();
    }

    public ViewCombatZone0() {
        super();
    }

    @Override
    public String toString() {
        return
                UP + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "\u001B[1m     Combat Zone      \u001B[22m" + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "  Less Crew: \u001B[31m-" + lostDays + " days\u001B[0m  " + LATERAL + "\n" +
                LATERAL + " Less Engines:\u001B[31m-" + lostCrew + " crew\u001B[0m " + LATERAL + "\n" +
                LATERAL + "     Less Cannons:    "+ LATERAL + "\n" +
                LATERAL + " ".repeat(10 - cannonFires().length()/2) + "\u001B[33m" + cannonFires() + "\u001B[0m" +  " ".repeat(10 - cannonFires().length()/2 + (cannonFires().length() % 2 == 0 ? 0 : 1)) + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 4, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "\u001B[1m     Combat Zone      \u001B[22m" + LATERAL;
            case 5 -> LATERAL + "  Less Crew: \u001B[31m-" + lostDays + " days\u001B[0m  " + LATERAL;
            case 6 -> LATERAL + " Less Engines:\u001B[31m-" + lostCrew + " crew\u001B[0m " + LATERAL;
            case 7 -> LATERAL + "     Less Cannons:    "+ LATERAL;
            case 8 -> LATERAL + " ".repeat(10 - cannonFires().length()/2) + "\u001B[33m" + cannonFires() + "\u001B[0m" +  " ".repeat(10 - cannonFires().length()/2 + (cannonFires().length() % 2 == 0 ? 0 : 1)) + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }

    /**
     * Constructs a string representation of the cannon fires based on the projectiles' fire types
     * and directions. Each projectile is represented in the string by its fire type ('H' for heavy fire,
     * 'L' for light fire), followed by the direction ('↑' for down, '↓' for up, '←' for right, '→' for left).
     * A space separates consecutive projectiles.
     *
     * @return the string representation of cannon fires including fire types and directions
     */
    private String cannonFires(){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < projectiles.size(); i++) {
            if (i == 0) {
                switch(projectiles.get(i).getFireType()) {
                    case HEAVY_FIRE -> result.append("H");
                    case LIGHT_FIRE -> result.append("L");
                }

            }
            else{
                switch(projectiles.get(i).getFireType()) {
                    case HEAVY_FIRE -> result.append(" H");
                    case LIGHT_FIRE -> result.append(" L");
                }

            }
            switch(projectiles.get(i).getDirection()) {
                case DOWN -> result.append("↑");
                case UP -> result.append("↓");
                case RIGHT -> result.append("←");
                case LEFT -> result.append("→");
            }
        }
        return result.toString();
    }

    // ↑→↓←
}
