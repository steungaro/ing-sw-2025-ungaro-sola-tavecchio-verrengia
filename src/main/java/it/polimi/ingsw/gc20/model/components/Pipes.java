package it.polimi.ingsw.gc20.model.components;

import java.util.*;

public class Pipes extends Component {

    public Pipes(Integer ID) {
        this.setIDComponent(ID);
        this.setConnectors(new HashMap<Direction, ConnectorEnum>());
        this.getConnectors().put(Direction.UP, ConnectorEnum.U);
        this.getConnectors().put(Direction.LEFT, ConnectorEnum.U);
        this.getConnectors().put(Direction.DOWN, ConnectorEnum.U);
        this.getConnectors().put(Direction.RIGHT, ConnectorEnum.U);
    }

}