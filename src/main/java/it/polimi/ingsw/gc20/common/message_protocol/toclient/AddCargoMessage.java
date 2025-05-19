package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;

import java.util.List;

public record AddCargoMessage(
        List<CargoColor> reward
) implements Message {
    @Override
    public String toString() {
        return "Add cargo";
    }

    @Override
    public void handleMessage() {
        //TODO
    }

}
