package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import java.util.List;

public class ViewPlanets extends ViewAdvetnureCard{

    List<int[]> planets; // red, yellow, green, blue
    int lostDays;

    @Override
    public String toString(){
        return
        up() + "\n" +
                lateral() + "  Planets             " + lateral() + "\n" +
                lateral() + EMPTY_ROW + lateral() + "\n" +
                planets() +
                lateral() + "  LostDays: " + lostDays + "         " + lateral() + "\n" +
                emprtyRows() +
                down();
    }

    private String planets(){
        String result = "";

        for(int i=0; i<planets.size(); i++){
            result += lateral() + "  ";
            result += "P" + i + ": ";

            result += reward(planets.get(i)[0], planets.get(i)[1], planets.get(i)[2], planets.get(i)[3]);
            result += spaces(16 - rewardSize(planets.get(i)[0], planets.get(i)[1], planets.get(i)[2], planets.get(i)[3]));
            result += lateral() + "\n";
        }


        return result;
    }

    private String reward(int redCargo, int yellowCargo, int greenCargo, int blueCargo){
        String result = "";
        int j = 0;
        for(int i = 0; i < redCargo; i++){
            if(i==0)
                result += "R";
            else
                result += " R";
            j++;
        }
        for(int i = 0; i < yellowCargo; i++){
            if(j==0)
                result += "Y";
            else
                result += " Y";
            j++;
        }
        for(int i = 0; i < greenCargo; i++){
            if(j==0)
                result += "G";
            else
                result += " G";
            j++;
        }
        for(int i = 0; i < blueCargo; i++){
            if(j==0)
                result += "B";
            else
                result += " B";
            j++;
        }
        return result;
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

    private String emprtyRows(){
        String result = "";
        for(int i=0; i<5-planets.size(); i++){
            result += lateral() + EMPTY_ROW + lateral() + "\n";
        }
        return result;
    }
}
