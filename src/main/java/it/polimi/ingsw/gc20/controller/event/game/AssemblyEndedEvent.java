package it.polimi.ingsw.gc20.controller.event.game;

import it.polimi.ingsw.gc20.controller.event.Event;

import java.io.Serializable;

// Event that is generated when a player finishes the assembly
public record AssemblyEndedEvent(
        String username
) implements Event, Serializable {
    @Override
    public String toString() {
        return "AssemblyEndedEvent {username = " + username + " finished the assembly}";
    }
}
