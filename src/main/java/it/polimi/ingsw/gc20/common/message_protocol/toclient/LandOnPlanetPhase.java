package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.Planet;

import java.util.List;

public record LandOnPlanetPhase(
        List<Planet> planets
) implements Message {
    @Override
    public String toString() {
        return "LandOnPlanetPhase{}";
    }

    @Override
    public void handleMessage() {
            ClientGameModel.getInstance().planetMenu(planets);
    }
}
