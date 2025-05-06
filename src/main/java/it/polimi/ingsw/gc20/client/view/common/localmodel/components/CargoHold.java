package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

public class CargoHold extends Component {
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

        row0 = "R ".repeat(red) + "G ".repeat(green) + "B ".repeat(blue) + "Y ".repeat(yellow);
        row1 = " ".repeat((14 - row0.length())/2) + row0 + " ".repeat((13 - row0.length())/2);
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

        row0 = "R ".repeat(red) + "G ".repeat(green) + "B ".repeat(blue) + "Y ".repeat(yellow);
        row1 = " ".repeat((14 - row0.length())/2) + row0 + " ".repeat((13 - row0.length())/2);
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
        CargoHold cargoHold = new CargoHold();
        cargoHold.red = 0;
        cargoHold.green = 1;
        cargoHold.blue = 1;
        cargoHold.yellow = 0;
        cargoHold.free = 2;
        System.out.println(cargoHold);
    }

    public CargoHold() {}
}