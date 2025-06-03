package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;

import java.util.List;

@SuppressWarnings( "unused")
public class ViewMeteorSwarm extends ViewAdventureCard {
    List<Projectile> projectiles;

    /**
     * Constructs a {@code ViewMeteorSwarm} and initializes it with the specified {@code AdventureCard}.
     * This constructor sets up the view representation for the Meteor Swarm adventure card
     * by initializing its internal state based on the provided card and associating the relevant projectiles.
     *
     * @param adventureCard the {@code AdventureCard} used to initialize the view representation and retrieve projectiles
     */
    public ViewMeteorSwarm(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.projectiles = adventureCard.getProjectiles();
    }
    public ViewMeteorSwarm() {
        super();
    }

    @Override
    public String toString(){
        return
        UP + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "\u001B[1m     Meteor Swarm     \u001B[0m" + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "       Meteors:       " + LATERAL + "\n" +
                LATERAL + "\u001B[33m" + " ".repeat(10 - meteorFires().length()/2 + 1) + meteorFires() + " ".repeat(10 - meteorFires().length()/2 + (projectiles.size() % 2 == 0 ? 0 : 1)) + "\u001B[0m" + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 4, 7, 8, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "\u001B[1m     Meteor Swarm     \u001B[0m" + LATERAL;
            case 5 -> LATERAL + "       Meteors:       " + LATERAL;
            case 6 -> LATERAL + "\u001B[33m" + " ".repeat(10 - meteorFires().length()/2 + 1) + meteorFires() + " ".repeat(10 - meteorFires().length()/2 + (projectiles.size() % 2 == 0 ? 0 : 1)) + "\u001B[0m" + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }

    /**
     * Builds a string representation of the meteoric projectiles, indicating their
     * fire type (e.g., heavy or light) and direction (e.g., up, down, left, or right).
     * The projectiles are separated by spaces, and the first entry is not preceded by a space.
     *
     * @return a string representation of the meteoric projectiles with fire types and directions.
     */
    private String meteorFires(){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < projectiles.size(); i++) {
            if (i == 0) {
                switch(projectiles.get(i).getFireType()) {
                    case HEAVY_METEOR -> result.append("H");
                    case LIGHT_METEOR -> result.append("L");
                }

            }
            else{
                switch(projectiles.get(i).getFireType()) {
                    case HEAVY_METEOR -> result.append(" H");
                    case LIGHT_METEOR -> result.append(" L");
                }

            }
            switch(projectiles.get(i).getDirection()) {
                case UP -> result.append("↑");
                case DOWN -> result.append("↓");
                case LEFT -> result.append("←");
                case RIGHT -> result.append("→");
            }
        }
        return result.toString();
    }
}
