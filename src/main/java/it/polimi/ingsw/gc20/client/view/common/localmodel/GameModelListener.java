package it.polimi.ingsw.gc20.client.view.common.localmodel; // Or your chosen package

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;

import java.util.List;

public interface GameModelListener {
    void onShipUpdated(ViewShip ship);
    void onLobbyUpdated(ViewLobby lobby);
    void onErrorMessageReceived(String message);
    void onComponentInHandUpdated(ViewComponent component);
    void onCurrentCardUpdated(ViewAdventureCard currentCard);
    void onBoardUpdated(ViewBoard board);
}