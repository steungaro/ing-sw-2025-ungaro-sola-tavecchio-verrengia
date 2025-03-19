package it.polimi.ingsw.gc20.controller.event.game;

import it.polimi.ingsw.gc20.model.components.Component;
import it.polimi.ingsw.gc20.controller.event.Event;
import java.io.Serializable;

// Event that is generated when a player takes a component from the pile or from booked components
public record TakeComponentEvent(
        String username,
        Integer componentID
) implements Event, Serializable {
    @Override
    public String toString() {
        return "TakeComponentEvent {username = " + username + ", component = " + componentID + "}";
    }
}
