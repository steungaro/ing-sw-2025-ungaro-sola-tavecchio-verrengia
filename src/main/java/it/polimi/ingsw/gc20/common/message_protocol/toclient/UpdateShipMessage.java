package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.components.Component;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

public record UpdateShipMessage(
        String username,
        Component[][] components,
        String action, // can be "used energies", "movedCargo", "removed component"
        float baseFirePower,
        int baseEnginePower,
        int astronauts,
        AlienColor aliens,
        boolean isLearner

        ) implements Message {
    @Override
    public String toString() {
        return username + " has " + action;
    }

    /**
     * Costruttore statico factory per creare un messaggio partendo dalla nave
     *
     * @param username nome dell'utente che sta assemblando la nave
     * @param componentInHand componente attualmente in mano al giocatore
     * @param ship nave del giocatore da cui estrarre la tabella di componenti
     * @return una nuova istanza di UpdateShipMessage
     */
    public static UpdateShipMessage fromShip(String username, Component componentInHand, Ship ship, String action) {
        Component[][] components = new Component[ship.getRows()][ship.getCols()];

        for (int i = 0; i < ship.getRows(); i++) {
            for (int j = 0; j < ship.getCols(); j++) {
                components[i][j] = ship.getComponentAt(i, j);
            }
        }
        try {
            return new UpdateShipMessage(username, components, action, ship.firePower(null, 0), ship.enginePower(0), ship.getAstronauts(), ship.getAliens(), !ship.isNormal());
        } catch (Exception _){
            return null;
        }
    }

    @Override
    public void handleMessage() {
        //View.getInstance().getShip(username).updateShip(components, action, baseFirePower, baseEnginePower, astronauts, aliens, isLearner);
        //TODO
    }

}
