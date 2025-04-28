package it.polimi.ingsw.gc20.common.message_protocol.toserver;

import java.io.Serializable;

public interface Message extends Serializable {
    void handleMessage();
    String username();
}
