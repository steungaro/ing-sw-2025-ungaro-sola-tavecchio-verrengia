package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;

import java.util.List;

@SuppressWarnings( "unused")
public class ViewCombatZone1 extends ViewAdventureCard {
    int lostDays;
    int lostCargo;
    List<Projectile> projectiles;

    /**
     * Constructs a new instance of {@code ViewCombatZone1} using the provided {@code AdventureCard}.
     * The method initializes the view representation of the adventure card by setting its type,
     * along with specific attributes such as lost cargo, projectiles, and lost days.
     *
     * @param adventureCard the {@code AdventureCard} object used to initialize the view and extract its attributes
     */
    public ViewCombatZone1(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.lostCargo = adventureCard.getLostCargo();
        this.projectiles = adventureCard.getProjectiles();
        this.lostDays = adventureCard.getLostDays();
    }

    public ViewCombatZone1() {
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
                LATERAL + "Less Cannons: \u001B[31m-" + lostDays +  " days\u001B[0m " + LATERAL + "\n" +
                LATERAL + "Less Engines: \u001B[31m-" + lostCargo + " cargo\u001B[0m" + LATERAL + "\n" +
                LATERAL + "      Less Crew:      "+ LATERAL + "\n" +
                LATERAL + "\u001B[33m" + " ".repeat(10 - cannonFires().length()/2) + cannonFires() + "\u001B[0m" + " ".repeat(10 - cannonFires().length()/2 + (cannonFires().length() % 2 == 0 ? 0 : 1)) + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }

    /**
     * Constructs a string representation of the sequence of cannon projectiles fired.
     * Each projectile is represented by its fire type and direction.
     * Fire types are denoted as "H" for heavy fire and "L" for light fire.
     * Directions are represented as arrows: "↑" for DOWN, "↓" for UP, "←" for RIGHT, and "→" for LEFT.
     * A space separates the representations of consecutive projectiles.
     *
     * @return A formatted string summarizing the fire types and directions of all projectiles fired.
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


    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 4, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "\u001B[1m     Combat Zone      \u001B[22m" + LATERAL;
            case 5 -> LATERAL + "Less Cannons: \u001B[31m-" + lostDays +  " days\u001B[0m " + LATERAL;
            case 6 -> LATERAL + "Less Engines: \u001B[31m-" + lostCargo + " cargo\u001B[0m" + LATERAL;
            case 7 -> LATERAL + "      Less Crew:      "+ LATERAL;
            case 8 -> LATERAL + "\u001B[33m" + " ".repeat(10 - cannonFires().length()/2) + cannonFires() + "\u001B[0m" + " ".repeat(10 - cannonFires().length()/2 + (cannonFires().length() % 2 == 0 ? 0 : 1)) + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }
}
