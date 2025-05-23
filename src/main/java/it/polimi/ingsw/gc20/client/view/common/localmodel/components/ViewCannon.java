package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

public class ViewCannon extends ViewComponent {
    public int power;

    public String toString(){
        char direction;
        switch (rotation) {
            case 0 -> direction = '↑';
            case 1 -> direction = '→';
            case 2 -> direction = '↓';
            case 3 -> direction = '←';
            default -> direction = ' ';
        }
        return
                firstRow() + "\n" +
                leftCol(0) + (power == 1 ? "Single" : "Double") + " Cannon" + rightCol(0) + "\n" +
                leftCol(1) + EMPTY_ROW + rightCol(1) + "\n" +
                leftCol(2) + "      " + direction + "      " + rightCol(2) + "\n" +
                lastRow();
    }

    public String toLine(int i) {
        char direction;
        switch (rotation) {
            case 0 -> direction = '↑';
            case 1 -> direction = '→';
            case 2 -> direction = '↓';
            case 3 -> direction = '←';
            default -> direction = ' ';
        }
        return switch (i) {
            case 0 -> firstRow();
            case 1 -> leftCol(0) + (power == 1 ? "Single" : "Double") + " Cannon" + rightCol(0);
            case 2 -> leftCol(1) + EMPTY_ROW + rightCol(1);
            case 3 -> leftCol(2) + "      \u001B[35m" + direction + "\u001B[0m      " + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }

    public static void main(String[] args) {
        ViewCannon cannon = new ViewCannon();
        cannon.power = 2;
        cannon.rotation = 3;
        System.out.println(cannon);
    }

    public ViewCannon() {
    }

    public boolean isCannon() {
        return true;
    }
}