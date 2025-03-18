package it.polimi.ingsw.gc20.controller.event.game;

import it.polimi.ingsw.gc20.controller.event.Event;
import it.polimi.ingsw.gc20.model.components.Component;
import java.io.Serializable;

public record PlaceComponentEvent(
        String username,
        int componentID,
        int column,
        int row
) implements Event, Serializable {
    @Override
    public String toString() {
        return "PlaceComponentEvent {username = " + username + ", component = " + componentID + ", in column = " + column + ", in row = " + row + "}";
    }
}
