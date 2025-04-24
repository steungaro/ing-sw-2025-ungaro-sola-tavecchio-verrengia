package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.List;

public class PreDrawState extends State{

    List<Player> nextRound;

    public PreDrawState(GameController controller) {
        super(controller);
    }

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
