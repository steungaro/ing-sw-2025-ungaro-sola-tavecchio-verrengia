package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client to inform them about the decision made during the card acceptance phase.
 * It contains the decision string about the decision to be made.
 */
public record AcceptPhaseMessage(
        String decision
) implements Message {
    @Override
    public String toString() {
        return decision;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().cardAcceptanceMenu(decision);
    }

}
