package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewEngine;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

public class Engine extends Component {

    private boolean doublePower;

    public Engine() {
    }
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
     * Function that returns the orientation of the engine. return the opposite direction of the rotation attribute.
     * @return the orientation of the engine
     */
    public Direction getOrientation() {
        return switch (rotation) {
            case UP -> Direction.DOWN;
            case RIGHT -> Direction.LEFT;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
        };
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
        if (d!= Direction.DOWN){
            return true;
        }
        return getOrientation() == d;
    }

    @Override
    public Boolean isValid (Component c, Direction d) {
        if (!hasValidOrientation(d)) {
            return false;
        }
        if (Direction.DOWN == d && getOrientation() == Direction.DOWN && c!=null) {
            return false;
        }
        return super.isValid(c, d);
    }

    @Override
    public ViewComponent createViewComponent() {
        ViewEngine viewEngine = new ViewEngine();
        viewEngine.power = doublePower ? 2 : 1;
        viewEngine.rotation = getOrientation().getValue();
        initializeViewComponent(viewEngine);
        return viewEngine;
    }

    @Override
    public boolean isEngine() {
        return true;
    }
}