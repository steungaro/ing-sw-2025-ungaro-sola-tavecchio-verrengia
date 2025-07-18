package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to inform them that their ship is invalid after receiving a projectile.
 */
public record ChooseBranchMessage() implements Message {
    @Override
    public String toString() {
        return "after the projectile your ship is invalid, choose a branch";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().branchMenu();
    }
}
