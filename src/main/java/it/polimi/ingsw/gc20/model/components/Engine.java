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
        ConnectorEnum conn = connectors.get(Direction.UP);
        connectors.put(Direction.UP, connectors.get(Direction.LEFT));
        connectors.put(Direction.LEFT, connectors.get(Direction.DOWN));
        connectors.put(Direction.DOWN, connectors.get(Direction.RIGHT));
        connectors.put(Direction.RIGHT, conn);

        if(orientation == Direction.UP){
            orientation = Direction.RIGHT;
        } else if(orientation == Direction.RIGHT){
            orientation = Direction.DOWN;
        } else if(orientation == Direction.DOWN){
            orientation = Direction.LEFT;
        } else if(orientation == Direction.LEFT){
            orientation = Direction.UP;
        }
    }

    /**
     * This function rotates the component counterclockwise by 90 degrees
     * */
    @Override
    public void rotateCounterclockwise() {
        ConnectorEnum conn = connectors.get(Direction.UP);
        connectors.put(Direction.UP, connectors.get(Direction.RIGHT));
        connectors.put(Direction.RIGHT, connectors.get(Direction.DOWN));
        connectors.put(Direction.DOWN, connectors.get(Direction.LEFT));
        connectors.put(Direction.LEFT, conn);

        if(orientation == Direction.UP){
            orientation = Direction.LEFT;
        } else if(orientation == Direction.RIGHT){
            orientation = Direction.UP;
        } else if(orientation == Direction.DOWN){
            orientation = Direction.RIGHT;
        } else if(orientation == Direction.LEFT){
            orientation = Direction.DOWN;
        }
    }

    @Override
    public void updateParameter (Ship s, int sign){
        if (this.doublePower) {
            s.addDoubleEngines(sign);
        } else
            s.addSingleEngines(sign);
    }

    @Override
    public boolean hasValidOrientation(Direction d){
        return orientation == Direction.DOWN && d == Direction.DOWN;
    }
}