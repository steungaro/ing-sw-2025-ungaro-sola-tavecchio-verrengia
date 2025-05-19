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

    public ViewAbandonedStation() {
        super();
    }
    
    protected ViewAbandonedStation(AdventureCard adventureCard) {
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
        return switch (i) {
            case 0 -> UP;
            case 1, 3, 7, 8, 9 ->LATERAL + EMPTY_ROW + LATERAL;
            case 2 ->LATERAL + "  Abandoned Station   " + LATERAL;
            case 4 ->LATERAL + "    Crew needed: " + crew + "    " + LATERAL;
            case 5 ->LATERAL + " ".repeat((14-reward().length())/2) + "Reward: " + reward() +  " ".repeat((14-reward().length())/2) + (reward().length() % 2 == 0 ? "" : " ") + LATERAL;
            case 6 ->LATERAL + "     Lost days: " + lostDays + "     " + LATERAL;
            case 10 ->DOWN;
            default -> "";
        };
    }

    @Override
    public String toString() {
        return
                UP + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "  Abandoned Station   " + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "    Crew needed: " + crew + "    " + LATERAL + "\n" +
                LATERAL + " ".repeat((14-reward().length())/2) + "Reward: " + reward() +  " ".repeat((14-reward().length())/2) + (reward().length() % 2 == 0 ? "" : " ") + LATERAL + "\n" +
                LATERAL + "     Lost days: " + lostDays + "     " + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                DOWN;
    }

    private String reward(){
        StringBuilder result = new StringBuilder();
        int j = 0;
        for(int i = 0; i < redCargo; i++){
            if(i==0)
                result.append("R");
            else
                result.append(" R");
            j++;
        }
        for(int i = 0; i < yellowCargo; i++){
            if(j==0)
                result.append("Y");
            else
                result.append(" Y");
            j++;
        }
        for(int i = 0; i < greenCargo; i++){
            if(j==0)
                result.append("G");
            else
                result.append(" G");
            j++;
        }
        for(int i = 0; i < blueCargo; i++){
            if(j==0)
                result.append("B");
            else
                result.append(" B");
            j++;
        }
        return result.toString();
    }
}
