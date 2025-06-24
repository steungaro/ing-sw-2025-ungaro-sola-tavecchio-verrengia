package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;

import java.util.List;

/**
 * This message is sent to the client to inform them about the cargo they have gained.
 * It contains a list of CargoColor representing the cargo colors that the player has received.
 */
public record AddCargoMessage(
        List<CargoColor> reward
) implements Message {
    @Override
    public String toString() {
        return "Add cargo";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().cargoMenu("Load the cargo you gained to your ship", 0, reward, false);
    }

}
