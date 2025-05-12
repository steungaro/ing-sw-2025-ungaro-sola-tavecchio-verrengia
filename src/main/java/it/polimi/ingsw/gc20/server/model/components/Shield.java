package it.polimi.ingsw.gc20.server.model.components;


import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewShield;

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
     */
    public void setCoveredSides(Direction[] coveredSides) {
        this.coveredSides = coveredSides;
    }

    /**
        * Rotate the shield counterclockwise
        * The connectors are rotated and the covered sides are updated
     */
    @Override
    public void rotateClockwise() {
        super.rotateClockwise();
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
    public void rotateCounterclockwise() {
        super.rotateCounterclockwise();
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


    /**
     * Function that return true if the shield is in the direction passed as parameter
     * @param dir direction ti verify
     * @return true if the shield is in the direction passed as parameter
     */
    @Override
    public boolean shieldIn (Direction dir) {
        for (Direction d : coveredSides) {
            if (d == dir) {
                return true;
            }
        }
        return false;
    }


    @Override
    public ViewComponent createViewComponent() {
        ViewShield viewShield = new ViewShield();
        for (Direction d: coveredSides) {
            if (d == Direction.UP) {
                viewShield.up = true;
            } else if (d == Direction.DOWN) {
                viewShield.down = true;
            } else if (d == Direction.LEFT) {
                viewShield.left = true;
            } else if (d == Direction.RIGHT) {
                viewShield.right = true;
            }
        }
        initializeViewComponent(viewShield);
        return viewShield;
    }
}