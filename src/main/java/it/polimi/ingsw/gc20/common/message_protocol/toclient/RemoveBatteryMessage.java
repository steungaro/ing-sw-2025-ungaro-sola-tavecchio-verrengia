package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to inform them about the need to remove batteries.
 */
public record RemoveBatteryMessage(
        int batteryNum
) implements Message {
    @Override
    public String toString() {
        return "You lost all your cargo, you'll now have to lose energy instead, please select " + batteryNum + " batteries";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().removeBatteryMenu(batteryNum);
    }
}
