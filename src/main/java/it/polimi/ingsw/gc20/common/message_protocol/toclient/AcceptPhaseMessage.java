package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.TUI.MenuState;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record AcceptPhaseMessage(
        String decision
) implements Message {
    @Override
    public String toString() {
        return decision;
    }

    @Override
    public void handleMessage() {
        MenuState state = new CardAcceptanceMenu();
        ClientGameModel.display(state);

    }

}
