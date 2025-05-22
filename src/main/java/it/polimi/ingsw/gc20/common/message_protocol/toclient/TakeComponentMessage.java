package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record TakeComponentMessage() implements Message {
    @Override
    public String toString() {
        return "You can take a component";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().setComponentInHand(null);
        ClientGameModel.getInstance().takeComponentMenu();
    }
}
