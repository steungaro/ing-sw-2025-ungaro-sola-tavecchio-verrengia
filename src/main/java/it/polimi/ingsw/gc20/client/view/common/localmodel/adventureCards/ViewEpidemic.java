package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

public class ViewEpidemic extends ViewAdvetnureCard{

    @Override
    public String toString() {
        return
                up() + "\n" +
                        lateral() + "  Epidemic           " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        down();
    }
}
