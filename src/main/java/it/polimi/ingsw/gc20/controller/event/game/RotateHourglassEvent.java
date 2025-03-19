package it.polimi.ingsw.gc20.controller.event.game;

import it.polimi.ingsw.gc20.controller.event.Event;
import it.polimi.ingsw.gc20.model.components.Component;
import java.io.Serializable;

// Event that is generated when a player rotates the hourglass
public record RotateHourglassEvent(

) implements Event, Serializable {
    @Override
    public String toString() {
        return "RotateHourglassEvent";
    }
}
