package it.polimi.ingsw.gc20.model.components;

import java.util.*;

public class Cannon extends Component {

    private Integer power;

    public Cannon(
            Integer ID,
            Integer power,
            Map<Direction, ConnectorEnum> conn
    ) {
        this.setIDComponent(ID);
        this.power = power;
        this.setConnectors(new HashMap<Direction, ConnectorEnum>());
        this.getConnectors().put(Direction.UP, conn.get(Direction.UP));
        this.getConnectors().put(Direction.LEFT, conn.get(Direction.LEFT));
        this.getConnectors().put(Direction.DOWN, conn.get(Direction.DOWN));
        this.getConnectors().put(Direction.RIGHT, conn.get(Direction.RIGHT));
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }
}