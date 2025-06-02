package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;

import java.util.List;

public class ViewAbandonedStation extends ViewAdventureCard {
    int crew;
    int redCargo = 0;
    int yellowCargo = 0;
    int greenCargo = 0;
    int blueCargo = 0;
    int lostDays;
    int size;

    public ViewAbandonedStation() {
        super();
    }
    
    public ViewAbandonedStation(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.crew = adventureCard.getCrew();
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

    @Override
    public String toLine(int i) {
        reward();
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 4, 8, 9 ->LATERAL + EMPTY_ROW + LATERAL;
            case 2 ->LATERAL + "\u001B[1m  Abandoned Station   \u001B[22m" + LATERAL;
            case 5 ->LATERAL + "    Crew needed: " + crew + "    " + LATERAL;
            case 6 ->LATERAL + " ".repeat((14-size)/2) + "Reward: " + reward() +  " ".repeat((14-size)/2) + (size % 2 == 0 ? "" : " ") + LATERAL;
            case 7 ->LATERAL + "     Lost days: \u001B[31m" + lostDays + "\u001B[0m     " + LATERAL;
            case 10 ->DOWN;
            default -> "";
        };
    }

    @Override
    public String toString() {
        reward();
        return
                UP + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "\u001B[1m  Abandoned Station   \u001B[22m" + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "    Crew needed: " + crew + "    " + LATERAL + "\n" +
                LATERAL + " ".repeat((14-size)/2) + "Reward: " + reward() +  " ".repeat((14-size)/2) + (size % 2 == 0 ? "" : " ") + LATERAL + "\n" +
                LATERAL + "     Lost days: \u001B[31m" + lostDays + "\u001B[0m     " + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }

    private String reward(){
        StringBuilder result = new StringBuilder();
        size = 0;
        int j = 0;
        for(int i = 0; i < redCargo; i++){
            if(i==0) {
                result.append("\u001B[31mR\u001B[0m");
                size++;
            }else {
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
            }
            j++;
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
