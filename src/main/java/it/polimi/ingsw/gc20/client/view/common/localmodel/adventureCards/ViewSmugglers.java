package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

public class ViewSmugglers extends ViewAdvetnureCard {
    int firePower;
    int lostCargo;
    int redCargo;
    int yellowCargo;
    int greenCargo;
    int blueCargo;

    @Override
    public String toString() {
        return
                up() + "\n" +
                        lateral() + "  Smugglers         " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  FirePower: " + firePower + "      " + lateral() + "\n" +
                        lateral() + "  LostCargo: " + lostCargo + "      " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  Reward: " +   "$        " + lateral() + "\n" +
                        lateral() + "  LostDays: " +  "       " + lateral() + "\n" +
                        down();
    }

    private String rewars(){
        String result = "";
        for(int i = 0; i < redCargo; i++){
            if(i==0)
                result += "R";
            else
                result += " R";
        }
        for(int i = 0; i < yellowCargo; i++){
            if(i==0)
                result += "Y";
            else
                result += " Y";
        }
        for(int i = 0; i < greenCargo; i++){
            if(i==0)
                result += "G";
            else
                result += " G";
        }
        for(int i = 0; i < blueCargo; i++){
            if(i==0)
                result += "B";
            else
                result += " B";
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
