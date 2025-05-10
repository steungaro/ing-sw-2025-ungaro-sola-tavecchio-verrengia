package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.components.Component;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

public record AssemblingShipUpdateMessage(
        String username,
        Component componentInHand,
        Component[][] components,
        String action// can be "added to the ship", "rotated", "took from booked", "added to booked"

) implements Message {
    @Override
    public String toString() {
        return username + " has " + action + " the component: " + componentInHand;
    }


    /**
     * Costruttore statico factory per creare un messaggio partendo dalla nave
     *
     * @param username nome dell'utente che sta assemblando la nave
     * @param componentInHand componente attualmente in mano al giocatore
     * @param ship nave del giocatore da cui estrarre la tabella di componenti
     * @return una nuova istanza di AssemblingShipUpdateMessage
     */
    public static AssemblingShipUpdateMessage fromShip(String username, Component componentInHand, Ship ship, String action) {
        Component[][] components = new Component[ship.getRows()][ship.getCols()];

        for (int i = 0; i < ship.getRows(); i++) {
            for (int j = 0; j < ship.getCols(); j++) {
                components[i][j] = ship.getComponentAt(i, j);
            }
        }

        return new AssemblingShipUpdateMessage(username, componentInHand, components, action);
    }

    @Override
    public void handleMessage() {
        //TODO
    }

}
