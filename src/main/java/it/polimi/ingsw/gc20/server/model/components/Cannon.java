package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.server.model.ship.Ship;

public class Cannon extends Component {
    private int power;
    private Direction orientation =  Direction.UP;

    public Cannon() {}

    /**
     * Function that returns the power of the cannon.
     * @return the power of the cannon
     */
    public float getPower() {
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

    @Override
    public void updateParameter(Ship s, int sign) {
        if (!(orientation == Direction.UP)){
             if (power == 1) {
                 s.addSingleCannonsPower(0.5f*sign);
             } else{
                    s.addDoubleCannonsPower(sign);
             }
        } else {
            if (power == 1) {
                s.addSingleCannonsPower(sign);
            } else{
                s.addDoubleCannonsPower(2*sign);
            }
        }
    }

    @Override
    public boolean isCannon() {
        return true;
    }

    @Override
    public Boolean isValid (Component c, Direction d) {
        if (d == orientation && c != null){
            return false;
        }
        return super.isValid(c, d);
    }
}