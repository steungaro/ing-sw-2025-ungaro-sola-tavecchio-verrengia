package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.PlayerUpdateMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.network.NetworkService;

import java.util.ArrayList;
import java.util.List;

public class PreDrawState extends State{

    List<Player> nextRound = new ArrayList<>();

    public PreDrawState(GameController controller) {
        super(controller);
        getController().preDrawConnect();
    }

    @Override
    public void nextRound(Player player){
        nextRound.add(player);
        if(nextRound.size() == getController().getOnlinePlayers()){
            getModel().getGame().sortPlayerByPosition();
            for (int i = 1; i< getModel().getGame().getPlayers().size(); i++){
                if (getModel().getGame().getPlayers().getFirst().getPosition()-getModel().getGame().getPlayers().get(i).getPosition() >= getModel().getGame().getBoard().getSpaces()){
                    Player p = getModel().getGame().getPlayers().get(i);
                    p.setGameStatus(false);
                    for (String username : getController().getInGameConnectedPlayers()) {
                        NetworkService.getInstance().sendToClient(username, new PlayerUpdateMessage(p.getUsername(), 0, p.isInGame(), p.getColor(), p.getPosition() % getModel().getGame().getBoard().getSpaces()));
                    }
                }
                if (getModel().getGame().getPlayers().get(i).getShip().getAstronauts()==0){
                    Player p = getModel().getGame().getPlayers().get(i);
                    p.setGameStatus(false);
                    for (String username : getController().getInGameConnectedPlayers()) {
                        NetworkService.getInstance().sendToClient(username, new PlayerUpdateMessage(p.getUsername(), 0, p.isInGame(), p.getColor(), p.getPosition() % getModel().getGame().getBoard().getSpaces()));
                    }
                }
            }
            getController().drawCard();
        }
    }

    @Override
    public String toString() {
        return "PreDrawState";
    }

}
