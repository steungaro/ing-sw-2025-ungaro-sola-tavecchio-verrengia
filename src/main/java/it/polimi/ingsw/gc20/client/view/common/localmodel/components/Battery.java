package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

public class Battery extends Component {
    public int availableEnergy;
    public int availableSlots;

    @Override
    public String toString() {
        String energyRow;
        String result;

        energyRow = "     " +availableEnergy + "/" + (availableSlots+availableEnergy) + "     ";

        result =
                firstRow() + "\n" +
                leftCol(0) + "   Battery   " + rightCol(0) + "\n" +
                leftCol(1) + EMPTY_ROW + rightCol(1) + "\n" +
                leftCol(2) + energyRow + rightCol(2) + "\n" +
                lastRow();
        return result;
    }

    public String toLine(int i) {
        return switch (i) {
            case 0 -> firstRow();
            case 1 -> leftCol(0) + "   Battery   " + rightCol(0);
            case 2 -> leftCol(1) + EMPTY_ROW + rightCol(1);
            case 3 ->
                    leftCol(2) + "     " + availableEnergy + "/" + (availableSlots + availableEnergy) + "     " + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }

    public static void main(String[] args) {
        Battery battery = new Battery();
        battery.upConnectors = 3;
        battery.downConnectors = 2;
        battery.leftConnectors = 1;
        battery.rightConnectors = 0;
        battery.availableEnergy = 2;
        battery.availableSlots = 3;
        System.out.println(battery);
    }

    public Battery() {}

    public boolean isBattery() {
        return true;
    }
}