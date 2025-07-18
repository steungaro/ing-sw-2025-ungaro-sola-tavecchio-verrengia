package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client when a component is placed in the player's hand
 * during the assembling phase of the game.
 */
public record AssemblingMessage(
        ViewComponent component
) implements Message {
    @Override
    public String toString() {
        return "Place component";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().setComponentInHand(component);
        ClientGameModel.getInstance().assemblingStateMenu();
    }
}
