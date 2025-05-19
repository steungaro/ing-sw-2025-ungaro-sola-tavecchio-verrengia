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
        // MenuState state = new CargoMenu(ClientGameModel.getInstance().getTerminal(), );
        // NOTE: CargoMenu stampa 2 volre i cargo da rimuovere, sia nel Message che successivamente

        // ClientGameModel.getInstance().cargoMenu();
    }
}
