package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

public class StartingCabin extends Cabin {
    public String toString() {
        String occupants;

        occupants = "     " + (astronauts > 0 ? astronauts :  "-") + "/2" + "     ";

        return firstRow() + "\n" +
                leftCol(0) + "StartingCabin" + rightCol(0) + "\n" +
                leftCol(1) + EMPTY_ROW + rightCol(1) + "\n" +
                leftCol(2) + occupants + rightCol(2) + "\n" +
                lastRow();
    }

    public String toLine(int i) {
        return switch (i) {
            case 0 -> firstRow();
            case 1 -> leftCol(0) + "StartingCabin" + rightCol(0);
            case 2 -> leftCol(1) + EMPTY_ROW + rightCol(1);
            case 3 -> leftCol(2) + "     " + (astronauts > 0 ? astronauts : "-") + "/2" + "     " + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }

    public static void main(String[] args) {
        StartingCabin startingCabin = new StartingCabin();
        startingCabin.astronauts = 0;
        System.out.println(startingCabin);
    }
    public StartingCabin() {}
}