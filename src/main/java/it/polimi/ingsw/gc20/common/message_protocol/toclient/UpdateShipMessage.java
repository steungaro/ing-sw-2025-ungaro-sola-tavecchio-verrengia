package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.components.Component;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

import java.util.List;

public record UpdateShipMessage(
        String username,
        ViewComponent[][] components,
        String action, // can be "used some energies", "moved piece of cargo", "removed a component",
        // "added to the ship", "rotated", "took from booked", "added to booked"
        float baseFirePower,
        int baseEnginePower,
        int astronauts,
        AlienColor aliens,
        boolean isLearner,
        boolean isValid,
        ViewComponent[] componentsBooked, // components in the booked area null if not used
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
     */
    public UpdateShipMessage(String username, Ship ship, String action) {
        ViewComponent[][] components = new ViewComponent[ship.getRows()][ship.getCols()];
        List<ViewComponent> waste = ship.getWaste().stream().map(Component::createViewComponent).toList();
        for (int i = 0; i < ship.getRows(); i++) {
            for (int j = 0; j < ship.getCols(); j++) {
                components[i][j] = ship.getComponentAt(i, j).createViewComponent();
            }
        }
        ViewComponent[] booked = new ViewComponent[2];
        if (ship.isNormal()){
            NormalShip normalShip = (NormalShip) ship;
            for (int i = 0; i < normalShip.getBooked().size(); i++) {
                if (normalShip.getBooked().get(i) != null) {
                    booked[i] = normalShip.getBooked().get(i).createViewComponent();
                } else {
                    booked[i] = null;
                }
            }
        }
        ViewComponent hand = null;

        this (username, components, action, ship.getSingleCannonsPower(), ship.getSingleEngines(), ship.getAstronauts(), ship.getAliens(), !ship.isNormal(), ship.isValid(), booked, waste);
    }
    @Override
    public void handleMessage() {
        ViewShip viewShip = View.getInstance().getShip(username);
        viewShip.aliens = aliens;
        viewShip.astronauts = astronauts;
        viewShip.baseEnginePower = baseEnginePower;
        viewShip.baseFirepower = baseFirePower;
        viewShip.setComponents(components);
        viewShip.isLearner = isLearner;
        viewShip.setWaste(waste);
        viewShip.setBooked(0, componentsBooked[0]);
        viewShip.setBooked(1, componentsBooked[1]);
        viewShip.setValid(isValid);
        // da capire dove mettere il component in hand
        View.getInstance().setShip(username, viewShip);

    }

}
