package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record LandOnPlanetMessage(
        String username
        // da capire come gestire il planet
) implements Message {
    @Override
    public String toString() {
        return "LandOnPlanetMessage{" +
                "username='" + username + '\'' +
                '}';
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
