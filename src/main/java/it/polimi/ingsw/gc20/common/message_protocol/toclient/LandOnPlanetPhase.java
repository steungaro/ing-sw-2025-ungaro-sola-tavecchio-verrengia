package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record LandOnPlanetPhase() implements Message {
    @Override
    public String toString() {
        return "LandOnPlanetPhase{}";
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
