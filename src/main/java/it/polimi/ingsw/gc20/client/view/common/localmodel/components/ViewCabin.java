package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

import it.polimi.ingsw.gc20.server.model.components.AlienColor;

public class ViewCabin extends ViewComponent {
    public int astronauts;
    public boolean alien = false;
    public AlienColor alienColor = AlienColor.NONE;
    public AlienColor cabinColor = AlienColor.NONE;

    public int getOccupants(){
        return astronauts + (alien ? 1 : 0);
    }

    @Override
    public String toString() {
        String occupants;

        occupants = "     " + (astronauts > 0 ? astronauts : (alien ? alienColor.getColorChar() : "-")) + "/2" + "     ";

        return firstRow() + "\n" +
                leftCol(0) + "    Cabin    " + rightCol(0) + "\n" +
                leftCol(1) + EMPTY_ROW + rightCol(1) + "\n" +
                leftCol(2) + occupants + rightCol(2) + "\n" +
                lastRow();
    }

    @Override
    public String toLine(int i) {
        return switch (i) {
            case 0 -> firstRow();
            case 1 -> leftCol(0) + "    Cabin    " + rightCol(0);
            case 2 -> leftCol(1) + EMPTY_ROW + rightCol(1);
            case 3 ->
                    leftCol(2) + "     " + (astronauts > 0 ? astronauts : (alien ? alienColor.getColorChar() : "-")) + "/2" + "     " + rightCol(2);
            case 4 -> lastRow();
            default -> "";
        };
    }

    public ViewCabin() {
        super();
    }

    @Override
    public boolean isCabin() {
        return true;
    }
}