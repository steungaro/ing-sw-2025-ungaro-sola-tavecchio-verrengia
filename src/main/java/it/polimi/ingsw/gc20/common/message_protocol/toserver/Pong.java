package it.polimi.ingsw.gc20.common.message_protocol.toserver;

public record Pong(
        String username
) implements Message {
    @Override
    public String toString() {
        return "Pong{" +
                "username =" + username + "}";
    }

    @Override
    public void handleMessage() {
        // Handled by the queue manager
    }
}
