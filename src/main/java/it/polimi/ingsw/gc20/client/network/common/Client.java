package it.polimi.ingsw.gc20.client.network.common;

import it.polimi.ingsw.gc20.common.interfaces.GameControllerInterface;
import it.polimi.ingsw.gc20.common.interfaces.MatchControllerInterface;

public interface Client extends GameControllerInterface, MatchControllerInterface {
    void start();

    void stop();

    boolean isConnected();

    void login(String username);

    @Deprecated
    void logout(String username);
}
