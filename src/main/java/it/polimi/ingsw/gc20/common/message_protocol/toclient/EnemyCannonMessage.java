package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record EnemyCannonMessage(
        int power
) implements Message {
    @Override
    public String toString() {
        return "Fighting an enemy with power="  + power;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().cannonsMenu(toString());
    }
}
