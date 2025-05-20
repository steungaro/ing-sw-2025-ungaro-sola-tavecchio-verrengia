package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record LoseCrewMessage(
        int crewNum
) implements Message {
    @Override
    public String toString() {
        return "Select crew to lose: " + crewNum;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().loseCrewMenu(crewNum);
    }
}
