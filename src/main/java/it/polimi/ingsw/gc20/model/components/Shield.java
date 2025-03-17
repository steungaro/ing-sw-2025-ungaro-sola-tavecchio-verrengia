package it.polimi.ingsw.gc20.model.components;

import java.util.*;

public class Shield extends Component {

    private Direction[] coveredSides = new Direction[2];

    /**
     * This function returns the covered sides of the shield
     * @return the covered sides of the shield
     */
    public Direction[] getCoveredSides() {
        return coveredSides;
    }

    public Shield() {
        super();
    }

    /**
        * This function returns the covered sides of the shield
        * @return the covered sides of the shield
     */
    public void setCoveredSides(Direction[] coveredSides) {
        this.coveredSides = coveredSides;
    }

    /**
        * Rotate the shield counterclockwise
        * The connectors are rotated and the covered sides are updated
     */
    @Override
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

    /**
        * Rotate the shield clockwise
        * The connectors are rotated and the covered sides are updated
     */
    @Override
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