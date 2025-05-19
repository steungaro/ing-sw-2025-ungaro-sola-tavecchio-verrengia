package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record PlaceComponentMessage(
        ViewComponent component
) implements Message {
    @Override
    public String toString() {
        return "Place component";
    }

    @Override
    public void handleMessage() {
        // TODO: non mi dice dove, non posso aggiornare il model
    }
}
