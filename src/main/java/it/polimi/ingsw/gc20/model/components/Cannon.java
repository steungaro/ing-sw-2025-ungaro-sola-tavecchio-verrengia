package it.polimi.ingsw.gc20.model.components;

public class Cannon extends Component {
    private int power;
    private Direction orientation =  Direction.UP;

    public Cannon() {}

    /**
     * Function that returns the power of the cannon.
     * @return the power of the cannon
     */
    public int getPower() {
        return power;
    }

    /**
     * Function that sets the power of the cannon.
     * @param power the power of the cannon
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * Function that returns the orientation of the cannon.
     * @return the orientation of the cannon
     */
    public Direction getOrientation() {
        return orientation;
    }

    /**
     * Function that sets the orientation of the cannon.
     * @param orientation the orientation of the cannon
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
}