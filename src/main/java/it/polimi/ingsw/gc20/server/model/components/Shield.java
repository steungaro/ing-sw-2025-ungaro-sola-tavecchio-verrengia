package it.polimi.ingsw.gc20.server.model.components;


import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewShield;

/**
 * Represents a Shield component that can cover specific sides and perform rotational actions.
 * The Shield maintains a record of covered sides and updates them when rotated.
 * It also provides functionality to check if a shield is present in a specific direction
 * and to create a visual representation of the shield.
 */
public class Shield extends Component {

    private Direction[] coveredSides = new Direction[2];

    /**
     * Retrieves the directions covered by the shield.
     *
     * @return an array of directions (Direction[]) representing the sides covered by the shield.
     */
    public Direction[] getCoveredSides() {
        return coveredSides;
    }

    public Shield() {
        super();
    }

    /**
     * Sets the directions covered by the shield.
     *
     * @param coveredSides an array of Direction values representing the sides that are covered
     *                     by the shield.
     */
    public void setCoveredSides(Direction[] coveredSides) {
        this.coveredSides = coveredSides;
    }

    /**
     * Rotates the shield 90 degrees clockwise.
     * <p>
     * This method updates the rotation of the shield, ensuring that each direction
     * in the {@code coveredSides} array transitions to its corresponding clockwise direction:
     * - UP becomes RIGHT
     * - RIGHT becomes DOWN
     * - DOWN becomes LEFT
     * - LEFT becomes UP
     * <p>
     * It overrides the parent class's {@code rotateClockwise} method to handle the
     * specific behavior of the shield's covered sides, while also maintaining
     * the parent class rotational behavior.
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
     * Rotates the shield 90 degrees counterclockwise.
     * <p>
     * This method adjusts the directions covered by the shield, updating the
     * {@code coveredSides} array to reflect the new orientation. Each direction
     * in the array transitions as follows:
     * - UP becomes LEFT
     * - LEFT becomes DOWN
     * - DOWN becomes RIGHT
     * - RIGHT becomes UP
     * <p>
     * Additionally, this method overrides the parent class's
     * {@code rotateCounterclockwise} method to ensure the shield's specific
     * behavior is handled properly, while maintaining the general rotational
     * behavior defined in the superclass.
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
     * Checks if the shield covers the given direction.
     *
     * @param dir the direction to check (of type {@code Direction}).
     * @return {@code true} if the shield covers the specified direction; {@code false} otherwise.
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