package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

public class ViewSpecialCargoHold extends ViewCargoHold {
    @Override
    public String toString() {
        String row0;
        String row2;

        row0 = "\u001B[31mR \u001B[0m".repeat(red) + "\u001B[32mG \u001B[0m".repeat(green) + "\u001B[34mB \u001B[0m".repeat(blue) + "\u001B[33mY \u001B[0m".repeat(yellow);
        row2 = " ".repeat((14 - (2*red + 2*green + 2*blue + 2*yellow + 2*free))/2) + "E ".repeat(free) + row0 + " ".repeat((13 - (2*red + 2*yellow + 2*green + 2*blue + 2*free))/2);

        return
                firstRow() + "\n" +
                        leftCol(0) + " SpecialHold " + rightCol(0) + "\n" +
                        leftCol(1) + EMPTY_ROW + rightCol(1) + "\n" +
                        leftCol(2) + row2 + rightCol(2) + "\n" +
                        lastRow();
    }

    @Override
    public String toLine(int i) {
        String row0;
        String row2;

        row0 = "\u001B[31mR \u001B[0m".repeat(red) + "\u001B[32mG \u001B[0m".repeat(green) + "\u001B[34mB \u001B[0m".repeat(blue) + "\u001B[33mY \u001B[0m".repeat(yellow);
        row2 = " ".repeat((14 - (2*red + 2*green + 2*blue + 2*yellow + 2*free))/2) + "E ".repeat(free) + row0 + " ".repeat((13 - (2*red + 2*yellow + 2*green + 2*blue + 2*free))/2);

        return switch (i) {
            case 0 -> firstRow();
            case 1 -> leftCol(0) + " SpecialHold " + rightCol(0);
            case 2 -> leftCol(1) + EMPTY_ROW + rightCol(1);
            case 3 -> leftCol(2) + row2 + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }


    public ViewSpecialCargoHold() {
    }

    @Override
    public boolean isSpecialCargoHold() {
        return true;
    }
}