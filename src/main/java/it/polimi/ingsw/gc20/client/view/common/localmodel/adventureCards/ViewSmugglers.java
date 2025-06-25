package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;

import java.util.List;

@SuppressWarnings( "unused")
public class ViewSmugglers extends ViewAdventureCard {
    int firePower;
    int lostCargo;
    int redCargo = 0;
    int yellowCargo = 0;
    int greenCargo = 0;
    int blueCargo = 0;
    int lostDays;
    int size;

    /**
     * Constructs a ViewSmugglers object by initializing its fields based on the data
     * from the specified {@code AdventureCard}.
     *
     * @param adventureCard the {@code AdventureCard} object whose attributes
     *                      are used to initialize the fields of this ViewSmugglers instance
     */
    public ViewSmugglers(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.firePower = adventureCard.getFirePower();
        this.lostCargo = adventureCard.getLostCargo();

        List<CargoColor> reward = adventureCard.getReward();
        for (CargoColor cargo : reward) {
            switch (cargo) {
                case RED:
                    redCargo++;
                    break;
                case YELLOW:
                    yellowCargo++;
                    break;
                case GREEN:
                    greenCargo++;
                    break;
                case BLUE:
                    blueCargo++;
                    break;
            }
        }
        this.lostDays = adventureCard.getLostDays();
    }

    public ViewSmugglers() {
        super();
    }

    @Override
    public String toLine(int i) {
        reward();
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 6, 9 -> LATERAL + EMPTY_ROW + LATERAL;
            case 2 -> LATERAL + "\u001B[1m      Smugglers       \u001B[0m" + LATERAL;
            case 4 -> LATERAL + "     FirePower: \u001B[31m" + firePower + "\u001B[0m     " + LATERAL;
            case 5 -> LATERAL + "     LostCargo: \u001B[31m" + lostCargo + "\u001B[0m     " + LATERAL;
            case 7 -> LATERAL + " ".repeat(7 - size/2) + "Reward: " + reward() +  " ".repeat(6-size/2 + (size % 2 == 0 ? 1 : 0)) + LATERAL;
            case 8 -> LATERAL + "     Lost days: \u001B[31m" + lostDays + "\u001B[0m     " + LATERAL;
            case 10 -> DOWN;
            default -> "";
        };
    }

    @Override
    public String toString() {
        reward();
        return
                UP + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "\u001B[1m      Smugglers       \u001B[0m" + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + "     FirePower: \u001B[31m" + firePower + "\u001B[0m     " + LATERAL + "\n" +
                        LATERAL + "     LostCargo: \u001B[31m" + lostCargo + "\u001B[0m     " + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        LATERAL + " ".repeat(7 - size/2) + "Reward: " + reward() +  " ".repeat(6-size/2 + (size % 2 == 0 ? 1 : 0)) + LATERAL + "\n" +
                        LATERAL + "     Lost days: \u001B[31m" + lostDays + "\u001B[0m     " + LATERAL + "\n" +
                        LATERAL + EMPTY_ROW + LATERAL + "\n" +
                        DOWN;
    }

    /**
     * Generates and returns a string representation of cargo based on specified amounts
     * of red, yellow, green, and blue cargo. Each cargo type is represented by a colored
     * symbol (R, Y, G, B) with appropriate spacing. The colors are added using ANSI escape codes.
     *
     * @return A string representation of cargo with symbols for red, yellow, green,
     *         and blue cargo, formatted with ANSI colors and appropriate spacing.
     */
    private String reward(){
        StringBuilder result = new StringBuilder();
        size = 0;
        int j = 0;
        for(int i = 0; i < redCargo; i++){
            if(i==0) {
                result.append("\u001B[31mR\u001B[0m");
                size++;
            }
            else {
                result.append("\u001B[31m R\u001B[0m");
                size+=2;
            }
            j++;
        }
        for(int i = 0; i < yellowCargo; i++){
            if(j==0) {
                result.append("\u001B[33mY\u001B[0m");
                size++;
            }else {
                result.append("\u001B[33m Y\u001B[0m");
                size+=2;
            }
            j++;
        }
        for(int i = 0; i < greenCargo; i++){
            if(j==0) {
                result.append("\u001B[32mG\u001B[0m");
                size++;
            }else {
                result.append("\u001B[32m G\u001B[0m");
                size+=2;
            }j++;
        }
        for(int i = 0; i < blueCargo; i++){
            if(j==0) {
                result.append("\u001B[34mB\u001B[0m");
                size++;
            }else {
                result.append("\u001B[34m B\u001B[0m");
                size+=2;
            }
            j++;
        }
        return result.toString();
    }
}
