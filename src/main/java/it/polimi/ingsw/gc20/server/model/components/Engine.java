package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewEngine;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

/**
 * Represents an Engine, which is a type of Component that can update ship parameters,
 * maintain orientation, and handle specific functionality like double power mode.
 * This class provides methods to configure and check the engine's state and behavior.
 */
public class Engine extends Component {

    private boolean doublePower;

    public Engine() {
    }
    /**
     * Retrieves the doublePower status of the engine.
     *
     * @return true if the engine has double power enabled, false otherwise
     */
    public boolean getDoublePower() {
        return doublePower;
    }

    /**
     * Sets the double power status of the engine.
     *
     * @param isDouble true to enable double power mode, false to disable it
     */
    public void setDoublePower(boolean isDouble) {
        this.doublePower = isDouble;
    }

    /**
     * Determines the orientation of this engine based on its current rotation.
     *
     * @return the corresponding opposite direction based on the current rotation:
     *         DOWN for UP, LEFT for RIGHT, UP for DOWN, and RIGHT for LEFT.
     */
    public Direction getOrientation() {
        return switch (rotation) {
            case UP -> Direction.DOWN;
            case RIGHT -> Direction.LEFT;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
        };
    }

    /**
     * Updates the engine parameters of the given ship based on the engine's power mode.
     * If the engine has double power enabled, it adds double engines. Otherwise, it adds single engines.
     *
     * @param s the ship whose engine parameters will be updated
     * @param sign the value indicating the modification to be applied to the engines;
     *             typically, a positive or negative value to increment or decrement the engines
     */
    @Override
    public void updateParameter (Ship s, int sign){
        if (this.getDoublePower()) {
            s.addDoubleEngines(sign);
        } else
            s.addSingleEngines(sign);
    }

    /**
     * Verifies if the provided direction is a valid orientation for the engine.
     * A direction is considered valid if it is not DOWN, or if it matches the engine's current orientation.
     *
     * @param d the direction to be evaluated
     * @return true if the direction is valid, false otherwise
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