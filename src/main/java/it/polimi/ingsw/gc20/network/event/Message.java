package it.polimi.ingsw.gc20.network.event;

import java.io.Serializable;

public interface Message extends Serializable {
    void handleMessage(MessageHandler messageHandler);
}
