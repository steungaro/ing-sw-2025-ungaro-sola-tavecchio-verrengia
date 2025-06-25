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

    @Override
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

    public ViewBattery() {}

    @Override
    public boolean isBattery() {
        return true;
    }
}