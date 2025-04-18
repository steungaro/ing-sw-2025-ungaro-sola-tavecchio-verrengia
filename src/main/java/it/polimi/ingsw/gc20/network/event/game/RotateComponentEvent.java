package it.polimi.ingsw.gc20.network.event.game;

import it.polimi.ingsw.gc20.network.event.Event;

import java.io.Serializable;

// Event that is generated when a player rotates a component
public record RotateComponentEvent(
        String username,
        int componentID,
        int side // 0 = clockwise, 1 = counterclockwise
) implements Event, Serializable {
    @Override
    public String toString() {
        return "RotateComponentEvent {username = " + username + ", component = " + componentID + ", rotated" + "}";
    }
}
