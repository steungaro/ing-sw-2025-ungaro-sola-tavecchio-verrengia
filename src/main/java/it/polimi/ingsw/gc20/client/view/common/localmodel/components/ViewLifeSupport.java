package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

import it.polimi.ingsw.gc20.server.model.components.AlienColor;

public class ViewLifeSupport extends ViewComponent {
    public AlienColor color;

    @Override
    public String toString() {
        return
                firstRow() + "\n" +
                leftCol(0) + " LifeSupport " + rightCol(0) + "\n" +
                leftCol(1) + EMPTY_ROW + rightCol(1) + "\n" +
                leftCol(2) + "      " +color.getColorChar() + "      " + rightCol(2) + "\n" +
                lastRow();
    }

    public String toLine(int i) {
        return switch (i) {
            case 0 -> firstRow();
            case 1 -> leftCol(0) + " LifeSupport " + rightCol(0);
            case 2 -> leftCol(1) + EMPTY_ROW + rightCol(1);
            case 3 -> leftCol(2) + "      " + color.getColorChar() + "      " + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }

    public static void main(String[] args) {
        ViewLifeSupport lifeSupport = new ViewLifeSupport();
        lifeSupport.color = AlienColor.PURPLE;
        System.out.println(lifeSupport);
        lifeSupport.color = AlienColor.BROWN;
        System.out.println(lifeSupport);
    }

    public ViewLifeSupport() {
        this.color = AlienColor.NONE;
    }

    public boolean isLifeSupport() {
        return true;
    }
}