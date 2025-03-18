package it.polimi.ingsw.gc20.controller.event.game;

import it.polimi.ingsw.gc20.model.components.Component;
import it.polimi.ingsw.gc20.controller.event.Event;
import java.io.Serializable;

public record TakeComponentEvent(
        String username,
        Component component
) implements Event, Serializable {
    @Override
    public String toString() {
        return "TakeComponentEvent {username = " + username + ", component = " + component + "}";
    }
}
