package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.TUI.MenuState;
import it.polimi.ingsw.gc20.client.view.TUI.TUI;
import it.polimi.ingsw.gc20.client.view.TUI.ValidationMenu;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record ValidateShipPhase() implements Message {
    @Override
    public String toString() {
        return "your ship is invalid";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().validationMenu();
    }
}
