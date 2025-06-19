package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

public class ViewShield extends ViewComponent {
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    @Override
    public String toString() {
        String sides;

        if (up && left) {
            sides = "\u001B[32m     ← ↑     \u001B[0m";
        } else if (up && right) {
            sides = "\u001B[32m     ↑ →     \u001B[0m";
        } else if (down && left) {
            sides = "\u001B[32m     ← ↓     \u001B[0m";
        } else if (down && right) {
            sides = "\u001B[32m     ↓ →     \u001B[0m";
        } else {
            sides = EMPTY_ROW;
        }
        return
                firstRow() + "\n" +
                leftCol(0) + "   Shields   " + rightCol(0) + "\n" +
                leftCol(1) + EMPTY_ROW + rightCol(1) + "\n" +
                leftCol(2) + sides + rightCol(2) + "\n" +
                lastRow();
    }

    @Override
    public String toLine(int i) {
        String sides;

        if (up && left) {
            sides = "\u001B[32m     ← ↑     \u001B[0m";
        } else if (up && right) {
            sides = "\u001B[32m     ↑ →     \u001B[0m";
        } else if (down && left) {
            sides = "\u001B[32m     ← ↓     \u001B[0m";
        } else if (down && right) {
            sides = "\u001B[32m     ↓ →     \u001B[0m";
        } else {
            sides = EMPTY_ROW;
        }
        return switch (i) {
            case 0 -> firstRow();
            case 1 -> leftCol(0) + "   Shields   " + rightCol(0);
            case 2 -> leftCol(1) + EMPTY_ROW + rightCol(1);
            case 3 -> leftCol(2) + sides + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }

    public ViewShield() {
    }

    @Override
    public boolean isShield() {
        return true;
    }
}