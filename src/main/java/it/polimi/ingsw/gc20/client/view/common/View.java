package it.polimi.ingsw.gc20.client.view.common;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.interfaces.ViewInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class View implements ViewInterface {
    private static View instance;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(View.class.getName());

    protected boolean loggedIn;
    protected String username;
    protected Client client;
    private ViewBoard board;
    private Map<String, ViewShip> ships;
    private ViewComponent componentInHand;

    public ViewComponent getComponentInHand() {
        return componentInHand;
    }

    public void setComponentInHand(ViewComponent componentInHand) {
        this.componentInHand = componentInHand;
    }

    public ViewBoard getBoard() {
        return board;
    }

    public void setBoard(ViewBoard board) {
        this.board = board;
    }

    public Map<String, ViewShip> getShips() {
        return ships;
    }

    public void setShips(Map<String, ViewShip> ships) {
        this.ships = ships;
    }

    public ViewShip getShip (String username) {
        return ships.get(username);
    }

    public void setShip (String username, ViewShip ship) {
        ships.put(username, ship);
    }

    protected View() {
        this.loggedIn = false;
        this.username = null;
        this.client = null;
    }

    public void ping() {
        LOGGER.info("Ping received from server, ponging back.");
        client.pong(username);
    }

    public static View getInstance() {
        return instance;
    }

    public static void setInstance(View instance) {
        View.instance = instance;
    }

    @Override
    public void updateView(Message message) throws RemoteException {
        // Submit message handling to the executor for asynchronous processing
        executor.submit(() -> {
            try {
                message.handleMessage();
            } catch (Exception e) {
                LOGGER.warning("Error while handling message: " + e.getMessage());
            }
        });
    }
}
