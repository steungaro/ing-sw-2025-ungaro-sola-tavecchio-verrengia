package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.FireType;

public record DefensiveCannonMessage(
        FireType fireType,
        int direction, //0-3
        int line
) implements Message {
    @Override
    public String toString(){
        return "DefensiveCannonMessage{" +
                "fireType=" + fireType +
                ", direction=" + direction +
                ", line=" + line +
                '}';
    }

    @Override
    public void handleMessage(){
        ClientGameModel.getInstance().cannonsMenu(toString());
    }
}
