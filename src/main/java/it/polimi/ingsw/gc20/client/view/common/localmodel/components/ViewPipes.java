package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

public class ViewPipes extends ViewComponent {
    @Override
    public String toString() {
        return
                firstRow() + "\n" +
                leftCol(0) + "    Pipes    " + rightCol(0) + "\n" +
                leftCol(1) + EMPTY_ROW + rightCol(1) + "\n" +
                leftCol(2) + EMPTY_ROW + rightCol(2) + "\n" +
                lastRow();
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> firstRow();
            case 1 -> leftCol(0) + "    Pipes    " + rightCol(0);
            case 2 -> leftCol(1) + EMPTY_ROW + rightCol(1);
            case 3 -> leftCol(2) + EMPTY_ROW + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }


    public ViewPipes() {
    }

    @Override
    public boolean isPipes() {
        return true;
    }
}