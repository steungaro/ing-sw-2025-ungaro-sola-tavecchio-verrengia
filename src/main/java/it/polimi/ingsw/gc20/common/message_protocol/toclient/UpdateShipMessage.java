package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;

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
        viewShip.setBooked(0, componentsBooked[0]);
        viewShip.setBooked(1, componentsBooked[1]);
        viewShip.setValid(isValid);
        ClientGameModel.getInstance().setShip(username, viewShip);
    }

}
