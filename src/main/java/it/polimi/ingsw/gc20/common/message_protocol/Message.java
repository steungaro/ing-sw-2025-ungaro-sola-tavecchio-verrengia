package it.polimi.ingsw.gc20.common.message_protocol;

import java.io.Serializable;

public interface Message extends Serializable {
    void handleMessage();
}
