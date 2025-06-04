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
        List<ViewComponent> viewed,
        String action // can be "taken from viewed", taken from unviewed", "added to viewed"
        ) implements Message {

    public static PileUpdateMessage fromComponent(String username, int unviewedSize, List<Component> viewed, String action) {
        List<ViewComponent> viewComponents = new ArrayList<>();
        if(viewed!=null){
            for (Component component : viewed) {
                if (component != null) {
                    viewComponents.add(component.createViewComponent());
                }
            }
        }
        return new PileUpdateMessage(username, unviewedSize, viewComponents, action);
    }
    @Override
    public String toString() {
        return username + "has" + action + "a component";
    }
    @Override
    public void handleMessage() {
        ViewBoard viewBoard = ClientGameModel.getInstance().getBoard();
        viewBoard.unviewedPile = unviewedSize;
        viewBoard.viewedPile = viewed;
         if (ClientGameModel.getInstance().getCurrentMenuState() != null) {
             ClientGameModel.getInstance().setCurrentMenuState(ClientGameModel.getInstance().getCurrentMenuState());
         }
    }
}
