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
        String row1;
        String row2;

        row0 = "\u001B[31mR \u001B[0m".repeat(red) + "\u001B[32mG \u001B[0m".repeat(green) + "\u001B[34mB \u001B[0m".repeat(blue) + "\u001B[33mY \u001B[0m".repeat(yellow);
        row1 = " ".repeat((14 - (2*red + 2*green + 2*blue + 2*yellow))/2) + row0 + " ".repeat((13 - (2*red + 2*yellow + 2*green + 2*blue))/2);
        row2 = " ".repeat(7 - free) + "F ".repeat(free) + " ".repeat(6 - free);


        return
                firstRow() + "\n" +
                leftCol(0) + "  CargoHold  " + rightCol(0) + "\n" +
                leftCol(1) + row1 + rightCol(1) + "\n" +
                leftCol(2) + row2 + rightCol(2) + "\n" +
                lastRow();
    }

    public String toLine(int i) {
        String row0;
        String row1;
        String row2;

        row0 = "\u001B[31mR \u001B[0m".repeat(red) + "\u001B[32mG \u001B[0m".repeat(green) + "\u001B[34mB \u001B[0m".repeat(blue) + "\u001B[33mY \u001B[0m".repeat(yellow);
        row1 = " ".repeat((14 - (2*red + 2*green + 2*blue + 2*yellow))/2) + row0 + " ".repeat((13 - (2*red + 2*yellow + 2*green + 2*blue))/2);
        row2 = " ".repeat(7 - free) + "F ".repeat(free) + " ".repeat(6 - free);

        return switch (i) {
            case 0 -> firstRow();
            case 1 -> leftCol(0) + "  CargoHold  " + rightCol(0);
            case 2 -> leftCol(1) + row1 + rightCol(1);
            case 3 -> leftCol(2) + row2 + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }

    public static void main(String[] args) {
        ViewCargoHold cargoHold = new ViewCargoHold();
        cargoHold.red = 1;
        cargoHold.green = 1;
        cargoHold.blue = 1;
        cargoHold.yellow = 0;
        cargoHold.free = 2;
        System.out.println(cargoHold);
    }

    public ViewCargoHold() {}

    @Override
    public boolean isCargoHold() {
        return true;
    }

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
}