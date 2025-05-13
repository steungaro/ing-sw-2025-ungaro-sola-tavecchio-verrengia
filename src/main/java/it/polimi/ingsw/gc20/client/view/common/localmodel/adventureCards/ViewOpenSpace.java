package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

public class ViewOpenSpace extends ViewAdvetnureCard{

    @Override
    public String toString() {
        return
                up() + "\n" +
                        lateral() + "  OpenSpace           " + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        lateral() + spaces(10) + "/\\" + spaces(10) + lateral() + "\n" +
                        lateral() + spaces(9) + "|__|" + spaces(9) + lateral() + "\n" +
                        lateral() + spaces(9) + "|  |" + spaces(9) + lateral() + "\n" +
                        lateral() + spaces(9) + "|__|" + spaces(9) + lateral() + "\n" +
                        lateral() + spaces(9) + "^^^^" + spaces(9) + lateral() + "\n" +
                        lateral() + EMPTY_ROW + lateral() + "\n" +
                        down();
    }

}
