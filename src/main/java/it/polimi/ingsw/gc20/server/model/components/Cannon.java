package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewCannon;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

public class Cannon extends Component {
    private int power;

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

    @Override
    public void updateParameter(Ship s, int sign) {
        if (!(rotation == Direction.UP)){
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
    public ViewComponent createViewComponent() {
        ViewCannon viewCannon = new ViewCannon();
        viewCannon.power = power;
        viewCannon.rotation = rotation.getValue();
        initializeViewComponent(viewCannon);
        return viewCannon;
    }

    @Override
    public Boolean isValid (Component c, Direction d) {
        if (d == rotation && c != null){
            return false;
        }
        return super.isValid(c, d);
    }


}