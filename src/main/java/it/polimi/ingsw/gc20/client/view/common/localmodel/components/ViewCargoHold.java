package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;

import java.util.List;

public class ViewCargoHold extends ViewComponent {
    public int red;
    public int green;
    public int blue;
    public int yellow;
    public int free;

    @Override
    public String toString() {
        String row0;
        String row2;

        row0 = "\u001B[31mR \u001B[0m".repeat(red) + "\u001B[32mG \u001B[0m".repeat(green) + "\u001B[34mB \u001B[0m".repeat(blue) + "\u001B[33mY \u001B[0m".repeat(yellow);
        row2 = " ".repeat((14 - (2*red + 2*green + 2*blue + 2*yellow + 2*free))/2) + "E ".repeat(free) + row0 + " ".repeat((13 - (2*red + 2*yellow + 2*green + 2*blue + 2*free))/2);

        return
                firstRow() + "\n" +
                leftCol(0) + "  CargoHold  " + rightCol(0) + "\n" +
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
            case 1 -> leftCol(0) + "  CargoHold  " + rightCol(0);
            case 2 -> leftCol(1) + EMPTY_ROW + rightCol(1);
            case 3 -> leftCol(2) + row2 + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }


    public ViewCargoHold() {}

    @Override
    public boolean isCargoHold() {
        return true;
    }

    /**
     * Constructs and retrieves the cargo hold as a list of cargo units,
     * represented by their respective colors. The cargo includes all
     * red, green, blue, and yellow cargo units based on the corresponding
     * counts of each color.
     *
     * @return a list of CargoColor objects representing the cargo hold
     */
    public List<CargoColor> getCargo() {
        List<CargoColor> cargo = new java.util.ArrayList<>();
        for (int i = 0; i < red; i++) {
            cargo.add(CargoColor.RED);
        }
        for (int i = 0; i < green; i++) {
            cargo.add(CargoColor.GREEN);
        }
        for (int i = 0; i < blue; i++) {
            cargo.add(CargoColor.BLUE);
        }
        for (int i = 0; i < yellow; i++) {
            cargo.add(CargoColor.YELLOW);
        }
        return cargo;
    }

    /**
     * Calculates and returns the total size of the cargo hold,
     * which is the sum of all colored cargo units and free space.
     *
     * @return the total size of the cargo hold as an integer
     */
    public int getSize() {
        return red + green + blue + yellow + free;
    }
}