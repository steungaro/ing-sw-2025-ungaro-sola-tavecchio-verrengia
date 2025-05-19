package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.components.Component;

import java.util.ArrayList;
import java.util.List;

public record PileUpdateMessage(
        String username,
        int unviewedSize,
        List<Component> viewed,
        String action // can be "taken from viewed", taken from unviewed", "added to viewed"
        ) implements Message {
    @Override
    public String toString() {
        return username + "has" + action + "a component";
    }
    @Override
    public void handleMessage() {
        ViewBoard viewBoard = ClientGameModel.getInstance().getBoard();
        if (viewBoard.unviewedPile != null && viewBoard.unviewedPile.size() > unviewedSize) {
            viewBoard.unviewedPile = viewBoard.unviewedPile.subList(viewBoard.unviewedPile.size() - unviewedSize, viewBoard.unviewedPile.size()); // first out
        }
        List<ViewComponent> viewedPile = new ArrayList<>();
        if(viewed!=null){
            for (Component component : viewed) {
                if (component != null) {
                    viewedPile.add(component.createViewComponent());
                }
            }
        }
        viewBoard.viewedPile = viewedPile;
    }
}
