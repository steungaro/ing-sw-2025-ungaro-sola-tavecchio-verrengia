package it.polimi.ingsw.gc20.model.components;

import java.util.*;

public class LifeSupport extends Component {

    private AlienColor type;

    public LifeSupport(Integer ID, AlienColor type, Map<Direction, ConnectorEnum> connectors) {
    }

    public AlienColor getType() {
        return type;
    }

    public void setType(AlienColor type) {
        this.type = type;
    }
}