package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.util.Map;

public record CombatZoneCannonMessage(
        Map<String, Float> declaredCannons
) implements Message {
    @Override
    public String toString() {
        return "CombatZoneCannonMessage{" +
                "declaredCannons=" + declaredCannons +
                '}';
    }

    @Override
    public void handleMessage() {

    }
}
