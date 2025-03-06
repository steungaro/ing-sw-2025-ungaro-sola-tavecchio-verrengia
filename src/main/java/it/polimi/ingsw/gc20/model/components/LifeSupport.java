package it.polimi.ingsw.gc20.model.components;

import java.util.*;

public class LifeSupport extends Component {

    private AlienColor type;

    public LifeSupport(Integer ID, AlienColor type, Map<Direction, ConnectorEnum> connectors) {
        this.setIDComponent(ID);
        this.type = type;
        this.setConnectors(new HashMap<Direction, ConnectorEnum>());
        this.getConnectors().put(Direction.UP, connectors.get(Direction.UP));
        this.getConnectors().put(Direction.LEFT, connectors.get(Direction.LEFT));
        this.getConnectors().put(Direction.DOWN, connectors.get(Direction.DOWN));
        this.getConnectors().put(Direction.RIGHT, connectors.get(Direction.RIGHT));
    }

    public AlienColor getType() {
        return type;
    }

    public void setType(AlienColor type) {
        this.type = type;
    }
}