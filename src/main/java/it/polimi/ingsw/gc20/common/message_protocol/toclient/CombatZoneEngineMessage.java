package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.util.Map;

public record CombatZoneEngineMessage(
        Map<String, Integer> declaredEngines
) implements Message {
    @Override
    public String toString() {
        return "CombatZoneEngineMessage{" +
                "declaredEngines=" + declaredEngines +
                '}';
    }

    @Override
    public void handleMessage() {

    }
}
