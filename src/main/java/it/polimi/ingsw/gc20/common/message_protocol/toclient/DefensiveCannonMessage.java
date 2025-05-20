package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.FireType;

public record DefensiveCannonMessage(
        FireType fireType,
        int direction, // 0 = up, 1 = right, 2 = down, 3 = left
        int line
) implements Message {
    @Override
    public String toString(){
        String directions = switch (direction) {
            case 0 -> "up";
            case 1 -> "right";
            case 2 -> "down";
            case 3 -> "left";
            default -> "invalid direction";
        };

        return "DefensiveCannonMessage{" +
                "fireType=" + fireType.getFireType() +
                ", direction=" + directions +
                ", line=" + line +
                '}';
    }

    @Override
    public void handleMessage(){
        ClientGameModel.getInstance().cannonsMenu(toString());
    }
}
