package it.polimi.ingsw.gc20.network.message_protocol.toserver;

import it.polimi.ingsw.gc20.controller.MatchController;

import java.io.Serializable;

public interface Message extends Serializable {
    void handleMessage();
}
