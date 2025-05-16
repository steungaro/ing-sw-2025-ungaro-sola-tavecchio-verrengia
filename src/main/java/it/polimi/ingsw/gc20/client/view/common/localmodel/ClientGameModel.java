package it.polimi.ingsw.gc20.client.view.common.localmodel;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
// Import GamePhaseType, ViewPlayer, etc.
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClientGameModel {
    private static final Logger LOGGER = Logger.getLogger(ClientGameModel.class.getName());

    private ViewShip playerShip;
    private ViewLobby currentLobby;
    private GamePhase currentPhase;
    private List<ViewPlayer> players;
    private String errorMessage;

    private final List<GameModelListener> listeners = new ArrayList<>();

    public ClientGameModel() {
        // Initialize default state if necessary
        this.players = new ArrayList<>();
    }

    public void addListener(GameModelListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(GameModelListener listener) {
        listeners.remove(listener);
    }

    // --- Updaters that notify listeners ---
    public void updatePlayerShip(ViewShip ship) {
        this.playerShip = ship;
        LOGGER.fine("Player ship updated in model.");
        for (GameModelListener listener : listeners) {
            listener.onShipUpdated(this.playerShip);
        }
    }

    public void updateLobby(ViewLobby lobby) {
        this.currentLobby = lobby;
        LOGGER.fine("Lobby updated in model: " + (lobby != null ? lobby.getID() : "null"));
        for (GameModelListener listener : listeners) {
            listener.onLobbyUpdated(this.currentLobby);
        }
    }

    public void updateGamePhase(GamePhase newPhase) {
        this.currentPhase = newPhase;
        LOGGER.fine("Game phase updated in model to: " + newPhase);
        for (GameModelListener listener : listeners) {
            listener.onPhaseChanged(this.currentPhase);
        }
    }
    
    public void setErrorMessage(String message) {
        this.errorMessage = message;
        LOGGER.warning("Error message set in model: " + message);
        for (GameModelListener listener: listeners) {
            listener.onErrorMessageReceived(message);
        }
    }

    // --- Getters ---
    public ViewShip getPlayerShip() { return playerShip; }
    public ViewLobby getCurrentLobby() { return currentLobby; }
    public GamePhase getCurrentPhase() { return currentPhase; }
    public List<ViewPlayer> getPlayers() { return players; }
    public String getErrorMessage() { return errorMessage; }
}