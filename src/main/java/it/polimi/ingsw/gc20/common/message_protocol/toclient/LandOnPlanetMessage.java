package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record LandOnPlanetMessage(
        String username,
        int planetIndex
        // da capire come gestire il planet
) implements Message {
    @Override
    public String toString() {
        return username + "Landed on: " + planetIndex;

    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
