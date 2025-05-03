package it.polimi.ingsw.gc20.client.view.common;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.common.interfaces.ViewInterface;

public abstract class View implements ViewInterface {
    private static View instance;
    private boolean loggedIn;
    private String username;

    protected Client client;

    protected View() {
        this.loggedIn = false;
        this.username = null;
        this.client = null;
    }

    public static View getInstance() {
        return instance;
    }

    public static void setInstance(View instance) {
        View.instance = instance;
    }
}
