package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.TUI.CargoMenu;
import it.polimi.ingsw.gc20.client.view.TUI.MenuState;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

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
