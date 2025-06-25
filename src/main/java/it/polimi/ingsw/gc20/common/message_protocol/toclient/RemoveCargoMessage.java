package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to inform them about the need to remove some cargo from their ship.
 */
public record RemoveCargoMessage(
        int cargoNum
) implements Message {
    @Override
    public String toString() {
        return "Select cargo to remove: " + cargoNum;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().cargoMenu(cargoNum);
    }
}
