package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

public class ViewSmugglers extends ViewAdvetnureCard {
    int firePower;
    int lostCargo;
    int redCargo = 0;
    int yellowCargo = 0;
    int greenCargo = 0;
    int blueCargo = 0;
    int lostDays;

    @Override
    public String toString() {
        return
                up() + "\n" +
                        lateral() + "  Smugglers           " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  FirePower: " + firePower + "        " + lateral() + "\n" +
                        lateral() + "  LostCargo: " + lostCargo + "        " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  Reward: " + reward() +  spaces(12-rewardSize()) + lateral() + "\n" +
                        lateral() + "  LostDays: " + lostDays + "         " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        down();
    }

    private String reward(){
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

    private int rewardSize(){
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
}
