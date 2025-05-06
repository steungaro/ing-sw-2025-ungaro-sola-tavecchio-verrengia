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

    @Override
    public void nextRound(Player player){
        nextRound.add(player);
        if(nextRound.size() == getController().getOnlinePlayers()){
            getController().drawCard();
        }
    }

    @Override
    public String toString() {
        return "PreDrawState";
    }

}
