package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

public class ViewAbandonedShip extends ViewAdvetnureCard {
    int lostCrew;
    int credits;
    int lostDays;

    @Override
    public String toString() {
        return
                up() + "\n" +
                        lateral() + "  Abandoned Ship      " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  LostCrew: " + lostCrew + "         " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + "  Credits: " + credits + "$         " + lateral() + "\n" +
                        lateral() + "  LostDays: " + lostDays + "         " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        down();
    }
}
