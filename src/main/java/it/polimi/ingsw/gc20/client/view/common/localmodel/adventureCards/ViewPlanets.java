package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Planet;

import java.util.List;

public class ViewPlanets extends ViewAdventureCard {

    List<int[]> planets; // red, yellow, green, blue
    int lostDays;

    public ViewPlanets(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.planets = adventureCard.getPlanets().stream().map(planet -> {
            int[] planetReward = new int[4];
            for (int i = 0; i < planet.getReward().size(); i++) {
                switch (planet.getReward().get(i)) {
                    case RED:
                        planetReward[0]++;
                        break;
                    case YELLOW:
                        planetReward[1]++;
                        break;
                    case GREEN:
                        planetReward[2]++;
                        break;
                    case BLUE:
                        planetReward[3]++;
                        break;
                }
            }
            return planetReward;
        }).toList();
        this.lostDays = adventureCard.getLostDays();
    }

    public ViewPlanets() {
        super();
    }

    @Override
    public String toLine(int i) {
        String string = toString();
        String[] lines = string.split("\n");
        if (i < 0 || i >= lines.length) {
            return "";
        }
        return lines[i];
    }

    @Override
    public String toString(){
        return
        UP + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "\u001B[1m       Planets        \u001B[0m" + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                planets() +
                LATERAL + "  LostDays: \u001B[31m" + lostDays + "\u001B[0m         " + LATERAL + "\n" +
                emptyRows() +
                DOWN;
    }

    private String planets(){
        StringBuilder result = new StringBuilder();

        for(int i=0; i<planets.size(); i++){
            result.append(LATERAL + "  ");
            result.append("\u001B[36mP").append(i+1).append("\u001B[0m: ");

            result.append(reward(planets.get(i)[0], planets.get(i)[1], planets.get(i)[2], planets.get(i)[3]));
            result.append(" ".repeat(16 - rewardSize(planets.get(i)[0], planets.get(i)[1], planets.get(i)[2], planets.get(i)[3])));
            result.append(LATERAL + "\n");
        }


        return result.toString();
    }

    private String reward(int redCargo, int yellowCargo, int greenCargo, int blueCargo){
        StringBuilder result = new StringBuilder();
        int j = 0;
        for(int i = 0; i < redCargo; i++){
            if(i==0)
                result.append("\u001B[31mR\u001B[0m");
            else
                result.append("\u001B[31m R\u001B[0m");
            j++;
        }
        for(int i = 0; i < yellowCargo; i++){
            if(j==0)
                result.append("\u001B[33mY\u001B[0m");
            else
                result.append("\u001B[33m Y\u001B[0m");
            j++;
        }
        for(int i = 0; i < greenCargo; i++){
            if(j==0)
                result.append("\u001B[32mG\u001B[0m");
            else
                result.append("\u001B[32m G\u001B[0m");
            j++;
        }
        for(int i = 0; i < blueCargo; i++){
            if(j==0)
                result.append("\u001B[34mB\u001B[0m");
            else
                result.append("\u001B[34m B\u001B[0m");
            j++;
        }
        return result.toString();
    }

    private int rewardSize(int redCargo, int yellowCargo, int greenCargo, int blueCargo){
        int result = 0;
        if(redCargo > 0)
            result++;
        if(yellowCargo > 0)
            result++;
        if(greenCargo > 0)
            result++;
        if(blueCargo > 0)
            result++;

        return (result-1)*2 + 1;
    }

    private String emptyRows(){
        return (LATERAL + EMPTY_ROW + LATERAL + "\n").repeat(Math.max(0, 5 - planets.size()));
    }
}
