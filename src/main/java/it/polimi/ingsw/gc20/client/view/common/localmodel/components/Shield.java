package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

public class Shield extends Component {
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    public String toString() {
        String sides;

        if (up && left) {
            sides = "     ← ↑     ";
        } else if (up && right) {
            sides = "     ↑ →     ";
        } else if (down && left) {
            sides = "     ← ↓     ";
        } else if (down && right) {
            sides = "     ↓ →     ";
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

    public String toLine(int i) {
        String sides;

        if (up && left) {
            sides = "     ← ↑     ";
        } else if (up && right) {
            sides = "     ↑ →     ";
        } else if (down && left) {
            sides = "     ← ↓     ";
        } else if (down && right) {
            sides = "     ↓ →     ";
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

    public static void main(String[] args) {
        Shield shield = new Shield();
        shield.up = true;
        shield.down = false;
        shield.left = true;
        shield.right = false;
        System.out.println(shield);

        shield.up = false;
        shield.down = true;
        shield.left = false;
        shield.right = true;
        System.out.println(shield);
    }
    public Shield() {
    }

    public boolean isShield() {
        return true;
    }
}