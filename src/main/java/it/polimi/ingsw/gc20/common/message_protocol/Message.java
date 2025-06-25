package it.polimi.ingsw.gc20.common.message_protocol;

import java.io.Serializable;

public interface Message extends Serializable {
    /**
     * Handles the message by performing the necessary actions based on its type.
     */
    void handleMessage();
}
