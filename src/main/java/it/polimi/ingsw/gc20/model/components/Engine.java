package it.polimi.ingsw.gc20.model.components;

import it.polimi.ingsw.gc20.model.ship.Ship;

public class Engine extends Component {

    private boolean doublePower;
    private Direction orientation = Direction.DOWN;

    public Engine() {}
    /**
     * Function that sets the engine as double.
     * @return true if the engine is double, false otherwise
     */
    public boolean getDoublePower() {
        return doublePower;
    }

    /**
     * Function that sets the engine as double.
     * @param isDouble the boolean value to set the engine as double
     */
    public void setDoublePower(boolean isDouble) {
        this.doublePower = isDouble;
    }

    /**
     * Function that returns the orientation of the engine.
     * @return the orientation of the engine
     */
    public Direction getOrientation() {
        return this.orientation;
    }

    /**
     * Function that sets the orientation of the engine.
     * @param orientation the orientation of the engine
     */
    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
    }

    /**
     * This function rotates the component clockwise by 90 degrees
     * */
    @Override
    public void rotateClockwise() {
        super.rotateClockwise();

        switch (orientation) {
            case UP: orientation = Direction.RIGHT; break;
            case RIGHT: orientation = Direction.DOWN; break;
            case DOWN: orientation = Direction.LEFT; break;
            case LEFT: orientation = Direction.UP; break;
        }
    }

    /**
     * This function rotates the component counterclockwise by 90 degrees
     * */
    @Override
    public void rotateCounterclockwise() {
        super.rotateCounterclockwise();

        switch (orientation) {
            case UP: orientation = Direction.LEFT; break;
            case RIGHT: orientation = Direction.UP; break;
            case DOWN: orientation = Direction.RIGHT; break;
            case LEFT: orientation = Direction.DOWN; break;
        }
    }

    /** Function that update the parameter of the ship.
     * @param s ship that is updating his parameter
     * @param sign integer that indicate if the parameter is increasing or decreasing
     */
    @Override
    public void updateParameter (Ship s, int sign){
        if (this.getDoublePower()) {
            s.addDoubleEngines(sign);
        } else
            s.addSingleEngines(sign);
    }

    /**
     * Function that returns true if the component has valid orientation
     * @return true if the component has valid orientation, false otherwise
     */
    @Override
    public boolean hasValidOrientation(Direction d){
        return orientation == Direction.DOWN && d == Direction.DOWN;
    }
}