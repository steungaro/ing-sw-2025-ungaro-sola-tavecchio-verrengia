package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

public class ViewBattery extends ViewComponent {
    public int availableEnergy;
    public int totalSlots;

    @Override
    public String toString() {
        String energyRow;
        String result;

        energyRow = "     " +availableEnergy + "/" + (totalSlots) + "     ";

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
                    leftCol(2) + "     " + availableEnergy + "/" + (totalSlots) + "     " + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }

    public static void main(String[] args) {
        ViewBattery battery = new ViewBattery();
        battery.upConnectors = 3;
        battery.downConnectors = 2;
        battery.leftConnectors = 1;
        battery.rightConnectors = 0;
        battery.availableEnergy = 2;
        battery.totalSlots = 3;
        System.out.println(battery);
    }

    public ViewBattery() {}

    public boolean isBattery() {
        return true;
    }
}