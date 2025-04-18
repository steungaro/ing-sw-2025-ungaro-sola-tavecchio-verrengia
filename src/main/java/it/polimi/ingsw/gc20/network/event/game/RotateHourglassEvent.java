package it.polimi.ingsw.gc20.network.event.game;

import it.polimi.ingsw.gc20.network.event.Event;

import java.io.Serializable;

// Event that is generated when a player rotates the hourglass
public record RotateHourglassEvent(

) implements Event, Serializable {
    @Override
    public String toString() {
        return "RotateHourglassEvent";
    }
}
