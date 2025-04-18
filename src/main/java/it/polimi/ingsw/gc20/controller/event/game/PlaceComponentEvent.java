package it.polimi.ingsw.gc20.controller.event.game;

import it.polimi.ingsw.gc20.controller.event.Event;

import java.io.Serializable;

// Event that is generated when a player places a component onto their ship
public record PlaceComponentEvent(
        String username,
        int componentID,
        int column,
        int row)
        implements Event, Serializable {
    @Override
    public String toString() {
        return "PlaceComponentEvent {username = " + username + ", component = " + componentID + ", in column = " + column + ", in row = " + row + "}";
    }
}
