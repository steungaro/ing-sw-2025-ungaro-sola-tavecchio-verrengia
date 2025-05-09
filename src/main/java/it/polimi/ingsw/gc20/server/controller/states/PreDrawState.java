package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PreDrawState extends State{

    List<Player> nextRound;

    public PreDrawState(GameController controller) {
        super(controller);
        getController().preDrawConnect();
    }

    public void nextRound(Player player){
        nextRound.add(player);
        if(nextRound.size() == getController().getOnlinePlayers()){
            getModel().getGame().sortPlayerByPosition();
            for (int i = 1; i< getModel().getGame().getPlayers().size(); i++){
                if (getModel().getGame().getPlayers().getFirst().getPosition()-getModel().getGame().getPlayers().get(i).getPosition() >= getModel().getGame().getBoard().getSpaces()){
                    getModel().getGame().getPlayers().get(i).setGameStatus(false);
                }
                if (getModel().getGame().getPlayers().get(i).getShip().getAstronauts()==0){
                    getModel().getGame().getPlayers().get(i).setGameStatus(false);
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
