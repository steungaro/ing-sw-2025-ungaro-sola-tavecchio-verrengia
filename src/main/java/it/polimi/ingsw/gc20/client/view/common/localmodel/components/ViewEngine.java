package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

public class ViewEngine extends ViewComponent {
    public int power;

    @Override
    public String toString() {
        char direction =
                switch (rotation) {
                    case 0 -> '↑';
                    case 1 -> '→';
                    case 2 -> '↓';
                    case 3 -> '←';
                    default -> ' ';
                };
        return
                firstRow() + "\n" +
                leftCol(0) + (power == 1 ? "Single" : "Double") + " Engine" + rightCol(0) + "\n" +
                leftCol(1) + EMPTY_ROW + rightCol(1) + "\n" +
                leftCol(2) + "      \u001B[33m" + direction + "\u001B[0m      " + rightCol(2) + "\n" +
                lastRow();
    }

    @Override
    public String toLine(int i) {
        char direction =
        switch (rotation) {
            case 0 -> '↑';
            case 1 -> '→';
            case 2 -> '↓';
            case 3 -> '←';
            default -> ' ';
        };
        return switch (i) {
            case 0 -> firstRow();
            case 1 -> leftCol(0) + (power == 1 ? "Single" : "Double") + " Engine" + rightCol(0);
            case 2 -> leftCol(1) + EMPTY_ROW + rightCol(1);
            case 3 -> leftCol(2) + "      \u001B[33m" + direction + "\u001B[0m      " + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }


    public ViewEngine() {
    }

    @Override
    public boolean isEngine() {
        return true;
    }
}