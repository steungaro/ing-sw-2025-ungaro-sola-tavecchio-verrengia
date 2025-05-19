package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record RemoveBatteryMessage(
        int batteryNum
) implements Message {
    @Override
    public String toString() {
        return "you ended tour cargo, select battery to remove: " + batteryNum;
    }

    @Override
    public void handleMessage() {
        // TODO
    }
}
