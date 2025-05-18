package it.polimi.ingsw.gc20.common.message_protocol.toclient;

public record StandbyMessage(
        String situation
) implements Message{
    @Override
    public String toString() {
        return situation;
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
