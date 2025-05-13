package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

public class ViewSlavers extends ViewAdvetnureCard {
    public int firePower;
    public int lostCrew;
    public int credits;
    public int lostDays;

    @Override
    public String toString() {
        return
                        up() + "\n" +
                        lateral() + "  Slavers           " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  FirePower: " + firePower + "      " + lateral() + "\n" +
                        lateral() + "  LostCrew: " + lostCrew + "       " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  Credits: " + credits + "$       " + lateral() + "\n" +
                        lateral() + "  LostDays: " + lostDays + "       " + lateral() + "\n" +
                        down();
    }

}
