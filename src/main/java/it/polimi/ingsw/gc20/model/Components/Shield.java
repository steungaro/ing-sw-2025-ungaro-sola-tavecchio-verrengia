package it.polimi.ingsw.gc20.model.Components;

import java.io.*;
import java.util.*;

public class Shield extends Component {

    private Direction[] coveredSides;

    public Shield(Integer ID, Map<Direction, ConnectorEnum> conn, Direction[] coveredSides) {
        this.setIDComponent(ID);
        this.setConnectors(new HashMap<Direction, ConnectorEnum>());
        this.getConnectors().put(Direction.UP, conn.get(Direction.UP));
        this.getConnectors().put(Direction.LEFT, conn.get(Direction.LEFT));
        this.getConnectors().put(Direction.DOWN, conn.get(Direction.DOWN));
        this.getConnectors().put(Direction.RIGHT, conn.get(Direction.RIGHT));
        this.coveredSides = coveredSides;
    }

    public Direction[] getCoveredSides() {
        return coveredSides;
    }

    public void setCoveredSides(Direction[] coveredSides) {
        this.coveredSides = coveredSides;
    }

    public void rotateCounterclockwise() {
        ConnectorEnum conn = getConnectors().get(Direction.UP);
        Map<Direction, ConnectorEnum> newConnectors = new HashMap<>();

        newConnectors.put(Direction.UP, getConnectors().get(Direction.RIGHT));
        newConnectors.put(Direction.RIGHT, getConnectors().get(Direction.DOWN));
        newConnectors.put(Direction.DOWN, getConnectors().get(Direction.LEFT));
        newConnectors.put(Direction.LEFT, conn);
        setConnectors(newConnectors);

        // Rotate the covered sides
        for (int i = 0; i < coveredSides.length; i++) {
            if (coveredSides[i] == Direction.UP) {
                coveredSides[i] = Direction.RIGHT;
            } else if (coveredSides[i] == Direction.RIGHT) {
                coveredSides[i] = Direction.DOWN;
            } else if (coveredSides[i] == Direction.DOWN) {
                coveredSides[i] = Direction.LEFT;
            } else if (coveredSides[i] == Direction.LEFT) {
                coveredSides[i] = Direction.UP;
            }
        }
    }

    public void rotateClockwise() {
        ConnectorEnum conn = getConnectors().get(Direction.UP);
        getConnectors().put(Direction.UP, getConnectors().get(Direction.LEFT));
        getConnectors().put(Direction.LEFT, getConnectors().get(Direction.DOWN));
        getConnectors().put(Direction.DOWN, getConnectors().get(Direction.RIGHT));
        getConnectors().put(Direction.RIGHT, conn);

        // Rotate the covered sides
        for (int i = 0; i < coveredSides.length; i++) {
            if (coveredSides[i] == Direction.UP) {
                coveredSides[i] = Direction.LEFT;
            } else if (coveredSides[i] == Direction.LEFT) {
                coveredSides[i] = Direction.DOWN;
            } else if (coveredSides[i] == Direction.DOWN) {
                coveredSides[i] = Direction.RIGHT;
            } else if (coveredSides[i] == Direction.RIGHT) {
                coveredSides[i] = Direction.UP;
            }
        }
    }
}