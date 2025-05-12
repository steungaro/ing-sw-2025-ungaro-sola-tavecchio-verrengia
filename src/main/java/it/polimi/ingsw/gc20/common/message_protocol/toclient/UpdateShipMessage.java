package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.components.Component;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

import java.util.List;

public record UpdateShipMessage(
        String username,
        ViewComponent[][] components,
        String action, // can be "used some energies", "moved piece of cargo", "removed a component"
        float baseFirePower,
        int baseEnginePower,
        int astronauts,
        AlienColor aliens,
        boolean isLearner,
        boolean isValid,
        List<ViewComponent> waste

        ) implements Message {
    @Override
    public String toString() {
        return username + " has " + action;
    }

    /**
     * Costruttore statico factory per creare un messaggio partendo dalla nave
     *
     * @param username nome dell'utente che sta assemblando la nave
     * @param ship nave del giocatore da cui estrarre la tabella di componenti
     * @return una nuova istanza di UpdateShipMessage
     */
    public UpdateShipMessage(String username, Ship ship, String action) {
        ViewComponent[][] components = new ViewComponent[ship.getRows()][ship.getCols()];
        List<ViewComponent> waste = ship.getWaste().stream().map(Component::createViewComponent).toList();
        for (int i = 0; i < ship.getRows(); i++) {
            for (int j = 0; j < ship.getCols(); j++) {
                components[i][j] = ship.getComponentAt(i, j).createViewComponent();
            }
        }
        this (username, components, action, ship.getSingleCannonsPower(), ship.getSingleEngines(), ship.getAstronauts(), ship.getAliens(), !ship.isNormal(), ship.isValid(), waste );

    }

    @Override
    public void handleMessage() {
        ViewShip viewShip = new ViewShip();
        viewShip.aliens = aliens;
        viewShip.astronauts = astronauts;
        viewShip.baseEnginePower = baseEnginePower;
        viewShip.baseFirepower = baseFirePower;
        viewShip.setComponents(components);
        viewShip.isLearner = isLearner;
        viewShip.setWaste(waste);
        View.getInstance().setShip(username, viewShip);
    }

}
