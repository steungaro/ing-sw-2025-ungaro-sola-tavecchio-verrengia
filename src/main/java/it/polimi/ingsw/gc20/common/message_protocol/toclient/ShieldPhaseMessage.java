package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.TUI.MenuState;
import it.polimi.ingsw.gc20.client.view.TUI.ShieldsMenu;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.FireType;

public record ShieldPhaseMessage(
        FireType fireType,
        int direction, // 0 = up, 1 = right, 2 = down, 3 = left
        int line
) implements Message {
    @Override
    public String toString() {
        return "ShieldPhaseMessage{}";
    }

    @Override
    public void handleMessage() {
        // String[] directions = {"UP", "RIGHT", "DOWN", "LEFT"};
        // String Message = fireType + " from " + directions[direction]  +  " at line " + line;
        // MenuState state = new ShieldsMenu(ClientGameModel.getInstance().getTerminal(), Message);
        // ClientGameModel.display(state);

        // ClientGameModel.getInstance().shieldsMenu(fireType, direction, line);
    }
}
