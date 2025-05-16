package it.polimi.ingsw.gc20.client.view.common.localmodel; // Or your chosen package

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;

import java.util.List;
// Import other relevant model parts

public interface GameModelListener {
    void onPhaseChanged(GamePhase newPhase);
    void onShipUpdated(ViewShip ship);
    void onLobbyUpdated(ViewLobby lobby);
    void onPlayerListUpdated(List<ViewPlayer> players);
    void onAvailableActionsChanged(List<GameAction> actions);
    void onErrorMessageReceived(String message);

}